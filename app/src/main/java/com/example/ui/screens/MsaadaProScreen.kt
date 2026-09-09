package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.ui.theme.MsaadaIcons
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaProGold
import com.example.ui.theme.MsaadaProGoldLight
import com.example.ui.theme.MsaadaTeal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

@Composable
fun MsaadaProScreen(
    isSwahili: Boolean,
    isProUser: Boolean,
    photosUsed: Int,
    onTogglePro: (Boolean) -> Unit,
    onActivatePro: (Long) -> Unit,
    onClose: () -> Unit
) {
    var selectedPlan by remember { mutableIntStateOf(0) }
    var customerPhone by remember { mutableStateOf("") }
    var transactionId by remember { mutableStateOf("") }
    var submitting by remember { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var activationCode by remember { mutableStateOf("") }
    var activationPhone by remember { mutableStateOf("") }
    var redeeming by remember { mutableStateOf(false) }
    var activationMessage by remember { mutableStateOf<String?>(null) }
    var activated by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val benefits = if (isSwahili) listOf(
        "Tengeneza picha za Passport na ID bila kikomo",
        "Violezo vyote vya ankara, risiti na hati bila kikomo",
        "Hakuna watermark kwenye nyaraka zilizopakuliwa",
        "Weka nembo (logo) na saini ya biashara yako",
        "Hifadhi nakala za nyaraka bila kikomo kwenye kifaa",
        "Ufikiaji wa haraka wa zana zote mpya za MSAADA"
    ) else listOf(
        "Unlimited Passport & ID photo generations",
        "Unlock all invoice, receipt & document templates",
        "No watermarks on exported PDF documents",
        "Add your custom business logo and signature",
        "Unlimited offline document storage on device",
        "Priority access to all upcoming MSAADA tools"
    )

    fun openWhatsApp() {
        val uri = Uri.parse("https://wa.me/255742259683")
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(
                onClick = onClose,
                modifier = Modifier.clip(CircleShape).background(MaterialTheme.colorScheme.surface)
                    .size(36.dp).testTag("pro_screen_close")
            ) {
                Icon(MsaadaIcons.Close, contentDescription = "Close")
            }
        }

        Box(
            modifier = Modifier.size(72.dp).clip(CircleShape).background(MsaadaProGoldLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(MsaadaIcons.Pro, contentDescription = null, tint = MsaadaProGold, modifier = Modifier.size(40.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "MSAADA PRO",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Black, letterSpacing = 1.5.sp,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            if (isSwahili) "Lipa kwa simu, tuma taarifa ya malipo, kisha tutakuthibitishia PRO."
            else "Pay by mobile money, submit your payment details, then we will verify your PRO access.",
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        if (isSwahili) "Matumizi ya bure ya picha" else "Free photo generations",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        if (isProUser) (if (isSwahili) "Bila Kikomo (PRO)" else "Unlimited (PRO)")
                        else "$photosUsed / 5 ${if (isSwahili) "zimetumika" else "used"}",
                        style = MaterialTheme.typography.bodySmall.copy(color = MsaadaTeal)
                    )
                }
                if (isProUser) {
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color(0xFFD1FAE5))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("ACTIVE", style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold, color = Color(0xFF065F46)
                        ))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Card(
                modifier = Modifier.weight(1f).clip(RoundedCornerShape(18.dp)).border(
                    if (selectedPlan == 0) 2.dp else 1.dp,
                    if (selectedPlan == 0) MsaadaTeal else MaterialTheme.colorScheme.outline,
                    RoundedCornerShape(18.dp)
                ).clickable { selectedPlan = 0 },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedPlan == 0) MsaadaTeal.copy(alpha = .08f) else MaterialTheme.colorScheme.surface
                )
            ) {
                Column(Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(if (isSwahili) "Mwezi 1" else "1 Month", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Text("TSh 5,000", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                    Text(if (isSwahili) "/ mwezi" else "/ month", style = MaterialTheme.typography.labelSmall)
                }
            }
            Card(
                modifier = Modifier.weight(1f).clip(RoundedCornerShape(18.dp)).border(
                    if (selectedPlan == 1) 2.dp else 1.dp,
                    if (selectedPlan == 1) MsaadaProGold else MaterialTheme.colorScheme.outline,
                    RoundedCornerShape(18.dp)
                ).clickable { selectedPlan = 1 },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedPlan == 1) MsaadaProGoldLight.copy(alpha = .3f) else MaterialTheme.colorScheme.surface
                )
            ) {
                Column(Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("SAVE 25%", style = MaterialTheme.typography.labelSmall.copy(
                        color = MsaadaProGold, fontWeight = FontWeight.Bold
                    ))
                    Spacer(Modifier.height(4.dp))
                    Text(if (isSwahili) "Mwaka 1" else "1 Year", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text("TSh 45,000", fontWeight = FontWeight.Black, color = Color(0xFFB45309))
                    Text(if (isSwahili) "/ mwaka" else "/ year", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    if (isSwahili) "Una activation code?" else "Already received an activation code?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    if (isSwahili) "Weka namba ya simu uliyolipia na code uliyopewa baada ya malipo kuthibitishwa."
                    else "Enter the phone number used for payment and the code you received after payment was verified.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedTextField(
                    value = activationPhone,
                    onValueChange = { activationPhone = it.take(20) },
                    modifier = Modifier.fillMaxWidth().testTag("activation_phone"),
                    label = { Text(if (isSwahili) "Namba ya simu" else "Payment phone number") },
                    placeholder = { Text("07XXXXXXXX") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = activationCode,
                    onValueChange = { activationCode = it.take(40).uppercase() },
                    modifier = Modifier.fillMaxWidth().testTag("activation_code"),
                    label = { Text(if (isSwahili) "Activation Code" else "Activation Code") },
                    placeholder = { Text("MSD-XXXX-XXXX") },
                    singleLine = true
                )
                Button(
                    enabled = !redeeming && activationPhone.trim().length >= 9 && activationCode.trim().length >= 8,
                    onClick = {
                        redeeming = true
                        activationMessage = null
                        CoroutineScope(Dispatchers.IO).launch {
                            val result = runCatching {
                                val endpoint = BuildConfig.MSAADA_ACTIVATION_API_URL.trim()
                                require(endpoint.startsWith("https://")) { "Activation API must use HTTPS." }
                                val connection = (URL(endpoint).openConnection() as HttpURLConnection).apply {
                                    requestMethod = "POST"
                                    connectTimeout = 10_000
                                    readTimeout = 15_000
                                    doOutput = true
                                    setRequestProperty("Content-Type", "application/json")
                                    setRequestProperty("Accept", "application/json")
                                }
                                val body = JSONObject()
                                    .put("code", activationCode.trim())
                                    .put("customerPhone", activationPhone.trim())
                                    .put("appVersion", BuildConfig.VERSION_NAME)
                                    .toString()
                                connection.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
                                val status = connection.responseCode
                                val stream = if (status in 200..299) connection.inputStream else connection.errorStream
                                val response = stream?.bufferedReader()?.use { it.readText() }.orEmpty()
                                connection.disconnect()
                                val json = JSONObject(response.ifBlank { "{}" })
                                if (status !in 200..299 || !json.optBoolean("active", false)) {
                                    error(json.optString("message", "Activation failed."))
                                }
                                json
                            }
                            launch(Dispatchers.Main) {
                                redeeming = false
                                result.fold(
                                    onSuccess = { json ->
                                        val expiry = json.optString("expiryTime", "")
                                        val expiryMillis = runCatching {
                                            val formatter = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", java.util.Locale.US)
                                            formatter.parse(expiry)?.time ?: 0L
                                        }.getOrElse { 0L }
                                        onActivatePro(expiryMillis)
                                        activated = true
                                        activationMessage = if (isSwahili) "PRO imewashwa kikamilifu." else "MSAADA PRO has been activated successfully."
                                    },
                                    onFailure = { err ->
                                        activationMessage = err.message ?: if (isSwahili) "Code si sahihi." else "Invalid activation code."
                                    }
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp).testTag("redeem_activation_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MsaadaTeal)
                ) {
                    Text(if (redeeming) (if (isSwahili) "Inathibitisha..." else "Activating...") else (if (isSwahili) "WASHA PRO" else "ACTIVATE PRO"), fontWeight = FontWeight.Bold)
                }
                activationMessage?.let {
                    Text(it, color = if (activated) MsaadaTeal else MaterialTheme.colorScheme.error, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                benefits.forEach { benefit ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(24.dp).clip(CircleShape).background(MsaadaTeal),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(MsaadaIcons.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(benefit, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MsaadaNavy)
        ) {
            Column(Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    if (isSwahili) "Jinsi ya kulipa" else "How to pay",
                    color = Color.White, fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    if (isSwahili)
                        "Lipa TSh 5,000 kwa namba ya MSAADA ya malipo, kisha weka Transaction ID hapa chini."
                    else
                        "Pay TSh 5,000 to the MSAADA payment number, then enter your Transaction ID below.",
                    color = Color.White.copy(alpha = .9f), textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    "Payment number: ${BuildConfig.MSAADA_PAYMENT_NUMBER.ifBlank { "Weka namba ya malipo" }}",
                    color = Color.White, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(10.dp))
                OutlinedButton(onClick = { openWhatsApp() }) {
                    Icon(Icons.Outlined.Phone, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (isSwahili) "Msaada kupitia WhatsApp" else "Contact via WhatsApp")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = customerPhone,
            onValueChange = { customerPhone = it.take(20) },
            modifier = Modifier.fillMaxWidth().testTag("payment_phone"),
            label = { Text(if (isSwahili) "Namba ya simu uliyolipia" else "Phone number used to pay") },
            placeholder = { Text("07XXXXXXXX") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = transactionId,
            onValueChange = { transactionId = it.take(80) },
            modifier = Modifier.fillMaxWidth().testTag("payment_transaction_id"),
            label = { Text("Transaction ID") },
            placeholder = { Text("Mfano: ABC123XYZ") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            enabled = !submitting && customerPhone.trim().length >= 9 && transactionId.trim().length >= 4,
            onClick = {
                submitting = true
                submitted = false
                message = null
                val amount = if (selectedPlan == 0) 5000 else 45000
                val plan = if (selectedPlan == 0) "monthly" else "yearly"
                val reference = "MSAADA-${UUID.randomUUID().toString().take(8).uppercase()}"
                CoroutineScope(Dispatchers.IO).launch {
                    val result = runCatching {
                        val endpoint = BuildConfig.MSAADA_PAYMENT_API_URL.trim()
                        require(endpoint.startsWith("https://")) { "Payment API must use HTTPS." }
                        val connection = (URL(endpoint).openConnection() as HttpURLConnection).apply {
                            requestMethod = "POST"
                            connectTimeout = 10_000
                            readTimeout = 15_000
                            doOutput = true
                            setRequestProperty("Content-Type", "application/json")
                            setRequestProperty("Accept", "application/json")
                        }
                        val body = JSONObject()
                            .put("reference", reference)
                            .put("plan", plan)
                            .put("amount", amount)
                            .put("currency", "TZS")
                            .put("customerPhone", customerPhone.trim())
                            .put("transactionId", transactionId.trim())
                            .put("appVersion", BuildConfig.VERSION_NAME)
                            .toString()
                        connection.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
                        val status = connection.responseCode
                        val stream = if (status in 200..299) connection.inputStream else connection.errorStream
                        val response = stream?.bufferedReader()?.use { it.readText() }.orEmpty()
                        connection.disconnect()
                        if (status !in 200..299) error("HTTP $status")
                        JSONObject(response)
                    }
                    launch(Dispatchers.Main) {
                        submitting = false
                        result.fold(
                            onSuccess = {
                                submitted = true
                                message = if (isSwahili)
                                    "Taarifa imetumwa. Tutakagua malipo yako na kukuthibitishia PRO."
                                else
                                    "Payment report sent. We will verify your payment and activate PRO."
                            },
                            onFailure = {
                                message = if (isSwahili)
                                    "Imeshindikana kutuma taarifa. Hakikisha una internet kisha jaribu tena."
                                else
                                    "Could not send the payment report. Check your internet connection and try again."
                            }
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(54.dp).testTag("submit_payment_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MsaadaNavy)
        ) {
            Text(
                if (submitting) (if (isSwahili) "Inatuma..." else "Sending...")
                else if (isSwahili) "TUMA TAARIFA YA MALIPO" else "SUBMIT PAYMENT REPORT",
                fontWeight = FontWeight.Bold
            )
        }

        message?.let {
            Spacer(Modifier.height(10.dp))
            Text(it, color = if (submitted) MsaadaTeal else MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
        }

        Spacer(Modifier.height(20.dp))

        OutlinedButton(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("pro_later_button"),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(if (isSwahili) "Baadaye" else "Maybe Later", fontWeight = FontWeight.SemiBold)
        }

        if (com.example.BuildConfig.DEBUG) {
            Spacer(Modifier.height(20.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .5f))
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            if (isSwahili) "Hali ya Kupima PRO (Demo Switch)" else "PRO Simulator Switch",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            if (isSwahili) "Kwa majaribio ya developer pekee." else "Developer testing only.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = isProUser,
                        onCheckedChange = onTogglePro,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MsaadaTeal,
                            checkedTrackColor = MsaadaTeal.copy(alpha = .4f)
                        )
                    )
                }
            }
        }
    }
}
