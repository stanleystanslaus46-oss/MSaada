package com.example.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.example.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.cancel
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * Google Play Billing + secure backend verification client for MSAADA PRO.
 *
 * The Android app never grants PRO solely because Google Play returned PURCHASED.
 * It sends the purchase token to the configured secure backend, which verifies
 * it with the Google Play Developer API before the entitlement is applied.
 */
class MsaadaBillingManager(
    context: Context,
    private val onVerifiedEntitlement: (active: Boolean, expiryTimeMillis: Long) -> Unit,
) {
    companion object {
        const val MONTHLY_PRODUCT_ID = "msaada_pro_monthly"
        const val YEARLY_PRODUCT_ID = "msaada_pro_yearly"
    }

    sealed interface BillingState {
        data object Idle : BillingState
        data object Connecting : BillingState
        data object Ready : BillingState
        data class LoadingProducts(val products: List<ProductDetails> = emptyList()) : BillingState
        data class PurchaseStarted(val productId: String) : BillingState
        data class Verifying(val productId: String) : BillingState
        data class Verified(val productId: String, val active: Boolean, val expiryTimeMillis: Long) : BillingState
        data class Error(val code: Int, val message: String) : BillingState
    }

    private val appContext = context.applicationContext
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val _state = kotlinx.coroutines.flow.MutableStateFlow<BillingState>(BillingState.Idle)
    val state: kotlinx.coroutines.flow.StateFlow<BillingState> = _state
    private val _products = kotlinx.coroutines.flow.MutableStateFlow<List<ProductDetails>>(emptyList())
    val products: kotlinx.coroutines.flow.StateFlow<List<ProductDetails>> = _products

    private val billingClient = BillingClient.newBuilder(appContext)
        .setListener { result, purchases -> handlePurchases(result, purchases) }
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()
        )
        .enableAutoServiceReconnection()
        .build()

    fun connect() {
        if (billingClient.isReady) {
            _state.value = BillingState.Ready
            queryProducts()
            queryExistingPurchases()
            return
        }
        _state.value = BillingState.Connecting
        billingClient.startConnection(object : BillingClient.BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    _state.value = BillingState.Ready
                    queryProducts()
                    queryExistingPurchases()
                } else {
                    _state.value = BillingState.Error(result.responseCode, result.debugMessage)
                }
            }
            override fun onBillingServiceDisconnected() = Unit
        })
    }

    private fun queryProducts() {
        val productList = listOf(MONTHLY_PRODUCT_ID, YEARLY_PRODUCT_ID).map {
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(it)
                .setProductType(ProductType.SUBS)
                .build()
        }
        _state.value = BillingState.LoadingProducts(_products.value)
        billingClient.queryProductDetailsAsync(
            QueryProductDetailsParams.newBuilder().setProductList(productList).build()
        ) { result, detailsResult ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                _products.value = detailsResult.productDetailsList
                _state.value = BillingState.Ready
            } else {
                _state.value = BillingState.Error(result.responseCode, result.debugMessage)
            }
        }
    }

    private fun queryExistingPurchases() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(ProductType.SUBS).build()
        ) { result, purchases ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                handlePurchases(result, purchases)
            }
        }
    }

    fun launchSubscription(activity: Activity, productId: String): BillingResult? {
        val product = _products.value.firstOrNull { it.productId == productId } ?: return null
        val offer = product.subscriptionOfferDetails?.firstOrNull() ?: return null
        val productParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(product)
            .setOfferToken(offer.offerToken)
            .build()
        val params = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productParams))
            .build()
        val result = billingClient.launchBillingFlow(activity, params)
        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
            _state.value = BillingState.PurchaseStarted(productId)
        } else {
            _state.value = BillingState.Error(result.responseCode, result.debugMessage)
        }
        return result
    }

    private fun handlePurchases(result: BillingResult, purchases: List<Purchase>?) {
        if (result.responseCode != BillingClient.BillingResponseCode.OK || purchases == null) return
        purchases.forEach { purchase ->
            val productId = purchase.products.firstOrNull() ?: return@forEach
            if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) return@forEach
            verifyPurchase(productId, purchase.purchaseToken)
        }
    }

    private fun verifyPurchase(productId: String, purchaseToken: String) {
        val endpoint = BuildConfig.BILLING_API_URL.trim()
        if (endpoint.isBlank()) {
            _state.value = BillingState.Error(-1, "MSAADA billing verification endpoint is not configured")
            return
        }
        val endpointUri = runCatching { android.net.Uri.parse(endpoint) }.getOrNull()
        if (endpointUri?.scheme != "https" || endpointUri.host.isNullOrBlank()) {
            _state.value = BillingState.Error(-1, "Billing verification endpoint must use HTTPS")
            return
        }
        _state.value = BillingState.Verifying(productId)
        scope.launch(Dispatchers.IO) {
            val result = runCatching {
                val connection = (URL(endpoint).openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    instanceFollowRedirects = false
                    connectTimeout = 10_000
                    readTimeout = 15_000
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty("Accept", "application/json")
                }
                val body = JSONObject()
                    .put("purchaseToken", purchaseToken)
                    .put("productId", productId)
                    .toString()
                connection.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
                val status = connection.responseCode
                val stream = if (status in 200..299) connection.inputStream else connection.errorStream
                val responseBody = stream?.bufferedReader()?.use { it.readText() }.orEmpty()
                connection.disconnect()
                if (status !in 200..299) error("Verification HTTP $status")
                JSONObject(responseBody)
            }
            scope.launch(Dispatchers.Main.immediate) {
                result.fold(
                    onSuccess = { json ->
                        val active = json.optBoolean("active", false)
                        val expiry = json.optString("expiryTime", "").let { parseIsoMillis(it) }
                        _state.value = BillingState.Verified(productId, active, expiry)
                        onVerifiedEntitlement(active, expiry)
                    },
                    onFailure = { error ->
                        _state.value = BillingState.Error(-2, error.message ?: "Verification failed")
                    }
                )
            }
        }
    }

    private fun parseIsoMillis(value: String): Long {
        if (value.isBlank()) return 0L
        return runCatching {
            java.time.Instant.parse(value).toEpochMilli()
        }.getOrDefault(0L)
    }

    fun endConnection() {
        billingClient.endConnection()
        scope.coroutineContext.cancel()
    }
}
