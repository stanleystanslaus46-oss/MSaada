package com.example.ui.screens.tools.business

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import com.example.ui.theme.MsaadaIcons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BiasharaProduct
import com.example.ui.MsaadaViewModel
import com.example.ui.components.EmptyState
import com.example.ui.components.MsaadaTopBar
import com.example.ui.components.ProBadge
import com.example.ui.components.ProFeatureGateDialog
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaTeal
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.ceil

@Composable
fun BiasharaCalculatorScreen(
    viewModel: MsaadaViewModel,
    isSwahili: Boolean,
    onBack: () -> Unit,
    onOpenPro: () -> Unit
) {
    val context = LocalContext.current
    val isProUser by viewModel.isProUser.collectAsState()
    val savedProducts by viewModel.biasharaProducts.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Hesabu Faida, 1: Kumbukumbu ya Bidhaa
    var showProGate by remember { mutableStateOf(false) }

    var productName by remember { mutableStateOf("T-Shirt za Pamba") }
    var buyingPriceText by remember { mutableStateOf("12000") }
    var transportCostText by remember { mutableStateOf("1000") }
    var packagingCostText by remember { mutableStateOf("500") }
    var otherCostText by remember { mutableStateOf("500") }
    var sellingPriceText by remember { mutableStateOf("20000") }
    var quantityText by remember { mutableStateOf("50") }
    var fixedCostsText by remember { mutableStateOf("0") }

    val buyingPrice = buyingPriceText.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val transport = transportCostText.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val packaging = packagingCostText.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val other = otherCostText.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val sellingPrice = sellingPriceText.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val quantity = quantityText.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val fixedCosts = fixedCostsText.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0

    // Calculations
    val totalCostPerUnit = buyingPrice + transport + packaging + other
    val totalInvestment = totalCostPerUnit * quantity
    val expectedRevenue = sellingPrice * quantity
    val profitPerUnit = sellingPrice - totalCostPerUnit
    val totalProfit = profitPerUnit * quantity

    val markupPercentage = if (totalCostPerUnit > 0) (profitPerUnit / totalCostPerUnit) * 100 else 0.0
    val profitMarginPercentage = if (sellingPrice > 0) (profitPerUnit / sellingPrice) * 100 else 0.0
    // True break-even quantity uses fixed costs / contribution margin.
    val contributionMarginPerUnit = sellingPrice - totalCostPerUnit
    val breakEvenUnits = if (fixedCosts > 0 && contributionMarginPerUnit > 0) {
        ceil(fixedCosts / contributionMarginPerUnit).toInt()
    } else {
        0
    }

    fun formatTsh(amount: Double): String {
        return "TSh " + NumberFormat.getNumberInstance(Locale.US).format(amount.toLong())
    }

    fun buildSummaryText(): String {
        return """
        MSAADA BIASHARA CALCULATOR
        Bidhaa: $productName
        Idadi ya Bidhaa: ${quantity.toInt()}
        
        GHARAMA:
        - Bei ya Kununua: ${formatTsh(buyingPrice)}
        - Usafiri & Ufungashaji: ${formatTsh(transport + packaging + other)}
        - Gharama Halisi kwa Kipande: ${formatTsh(totalCostPerUnit)}
        - Jumla ya Mtaji Uliowekezwa: ${formatTsh(totalInvestment)}
        
        MAUZO & FAIDA:
        - Bei ya Kuuzia: ${formatTsh(sellingPrice)}
        - Mauzo Yote Yatarajiwayo: ${formatTsh(expectedRevenue)}
        - Faida kwa Kipande: ${formatTsh(profitPerUnit)}
        - JUMLA YA FAIDA: ${formatTsh(totalProfit)}
        - Kiwango cha Faida (Margin): ${String.format(Locale.US, "%.1f", profitMarginPercentage)}%
        - Markup: ${String.format(Locale.US, "%.1f", markupPercentage)}%
        - Gharama za Kudumu: ${formatTsh(fixedCosts)}
        - Break-even: ${if (breakEvenUnits > 0) "Uza vipande $breakEvenUnits" else "Weka gharama za kudumu na hakikisha margin ni chanya"}
        """.trimIndent()
    }

    Scaffold(
        topBar = {
            MsaadaTopBar(
                title = if (isSwahili) "Biashara Calculator" else "Business Calculator",
                onBack = onBack,
                actions = {
                    if (isProUser) {
                        ProBadge(modifier = Modifier.padding(end = 12.dp))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (isSwahili) "Hesabu Faida" else "Profit Calculator",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = if (isSwahili) "Bidhaa Zilizohifadhiwa (${savedProducts.size})" else "Saved Products (${savedProducts.size})",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }

            if (selectedTab == 0) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Result Overview Card
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("biashara_result_card"),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MsaadaNavy)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text(
                                    text = if (isSwahili) "Jumla ya Faida ya Biashara" else "Total Net Profit",
                                    style = MaterialTheme.typography.titleSmall.copy(color = Color.White.copy(alpha = 0.8f))
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = formatTsh(totalProfit),
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = if (totalProfit >= 0) Color.White else Color(0xFFEF4444)
                                    )
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = if (isSwahili) "Faida kwa Kipande" else "Profit per Unit",
                                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f))
                                        )
                                        Text(
                                            text = formatTsh(profitPerUnit),
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = if (isSwahili) "Kiwango cha Faida" else "Profit Margin",
                                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f))
                                        )
                                        Text(
                                            text = String.format(Locale.US, "%.1f%%", profitMarginPercentage),
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = MsaadaTeal
                                            )
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = if (isSwahili) "Break-Even" else "Break-Even",
                                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f))
                                        )
                                        Text(
                                            text = "$breakEvenUnits / ${quantity.toInt()}",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Product & Buying Price Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (isSwahili) "Gharama za Manunuzi" else "Cost of Purchase",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = productName,
                                    onValueChange = { productName = it },
                                    label = { Text(if (isSwahili) "Jina la Bidhaa / Huduma" else "Product Name") },
                                    modifier = Modifier.fillMaxWidth().testTag("biashara_product_name"),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = buyingPriceText,
                                        onValueChange = { buyingPriceText = it },
                                        label = { Text(if (isSwahili) "Bei ya Kununua (TSh)" else "Buying Price") },
                                        modifier = Modifier.weight(1f).testTag("biashara_buying_price"),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true
                                    )

                                    OutlinedTextField(
                                        value = quantityText,
                                        onValueChange = { quantityText = it },
                                        label = { Text(if (isSwahili) "Idadi ya Bidhaa" else "Quantity") },
                                        modifier = Modifier.weight(1f).testTag("biashara_quantity"),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = if (isSwahili) "Gharama Nyinginezo kwa Kipande (TSh):" else "Additional Costs per Unit (TSh):",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    OutlinedTextField(
                                        value = transportCostText,
                                        onValueChange = { transportCostText = it },
                                        label = { Text(if (isSwahili) "Usafiri" else "Transport") },
                                        modifier = Modifier.weight(1f),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true
                                    )
                                    OutlinedTextField(
                                        value = packagingCostText,
                                        onValueChange = { packagingCostText = it },
                                        label = { Text(if (isSwahili) "Mifuko" else "Package") },
                                        modifier = Modifier.weight(1f),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true
                                    )
                                    OutlinedTextField(
                                        value = otherCostText,
                                        onValueChange = { otherCostText = it },
                                        label = { Text(if (isSwahili) "Nyingine" else "Other") },
                                        modifier = Modifier.weight(1f),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = if (isSwahili) "Gharama Halisi kwa Kipande:" else "Total Cost per Unit:",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = formatTsh(totalCostPerUnit),
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Selling Price & Target Margin Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (isSwahili) "Bei ya Kuuzia & Malengo ya Faida" else "Selling Price & Target",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = sellingPriceText,
                                    onValueChange = { sellingPriceText = it },
                                    label = { Text(if (isSwahili) "Bei ya Kuuzia kwa Mteja (TSh)" else "Customer Selling Price") },
                                    modifier = Modifier.fillMaxWidth().testTag("biashara_selling_price"),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = fixedCostsText,
                                    onValueChange = { fixedCostsText = it },
                                    label = { Text(if (isSwahili) "Gharama za Kudumu (TSh)" else "Fixed Costs (TSh)") },
                                    supportingText = { Text(if (isSwahili) "Mfano: kodi, mishahara, leseni" else "Example: rent, salaries, licenses") },
                                    modifier = Modifier.fillMaxWidth().testTag("biashara_fixed_costs"),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = if (isSwahili) "Weka Haraka Asilimia ya Faida (Markup):" else "Quick Markup Percentage:",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    listOf(20, 30, 40, 50).forEach { percent ->
                                        FilterChip(
                                            selected = false,
                                            onClick = {
                                                if (totalCostPerUnit > 0) {
                                                    val suggested = totalCostPerUnit * (1 + percent / 100.0)
                                                    sellingPriceText = suggested.toLong().toString()
                                                }
                                            },
                                            label = { Text("+$percent%", fontSize = 11.sp) }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Actions
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (!isProUser) {
                                        showProGate = true
                                        return@Button
                                    }
                                    if (productName.isBlank() || quantity <= 0 || buyingPrice < 0 || transport < 0 || packaging < 0 || other < 0 || sellingPrice <= 0) {
                                        Toast.makeText(context, if (isSwahili) "Weka thamani sahihi za bidhaa." else "Enter valid product values.", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    val item = BiasharaProduct(
                                        productName = productName,
                                        buyingPrice = buyingPrice,
                                        transportCost = transport,
                                        packagingCost = packaging,
                                        otherCost = other,
                                        sellingPrice = sellingPrice,
                                        quantity = quantity,
                                        totalCostPerUnit = totalCostPerUnit,
                                        totalInvestment = totalInvestment,
                                        expectedRevenue = expectedRevenue,
                                        profitPerUnit = profitPerUnit,
                                        totalProfit = totalProfit,
                                        markupPercentage = markupPercentage,
                                        profitMarginPercentage = profitMarginPercentage,
                                        // Store the actual break-even SELLING PRICE for the planned quantity.
                                        // breakEvenUnits is displayed separately in the live summary.
                                        breakEvenPrice = if (quantity > 0 && fixedCosts > 0) {
                                            totalCostPerUnit + (fixedCosts / quantity)
                                        } else {
                                            totalCostPerUnit
                                        },
                                        fixedCosts = fixedCosts
                                    )

                                    viewModel.saveBiasharaProduct(item)
                                    Toast.makeText(
                                        context,
                                        if (isSwahili) "Bidhaa imehifadhiwa kikamilifu!" else "Product calculation saved!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MsaadaNavy),
                                modifier = Modifier.weight(1f).testTag("biashara_save_btn")
                            ) {
                                Icon(MsaadaIcons.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isSwahili) "Hifadhi Hesabu" else "Save Product")
                            }

                            Button(
                                onClick = {
                                    val text = buildSummaryText()
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, "Hesabu ya Faida: $productName")
                                        putExtra(Intent.EXTRA_TEXT, text)
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Shiriki Hesabu ya Biashara"))
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MsaadaTeal),
                                modifier = Modifier.weight(1f).testTag("biashara_share_btn")
                            ) {
                                Icon(MsaadaIcons.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isSwahili) "Shiriki" else "Share")
                            }
                        }
                    }
                }
            } else {
                // Saved Products Tab
                if (savedProducts.isEmpty()) {
                    EmptyState(
                        icon = MsaadaIcons.BusinessCalculator,
                        title = if (isSwahili) "Hakuna Hesabu za Bidhaa Zilizohifadhiwa" else "No Saved Products",
                        description = if (isSwahili) "Bidhaa utakazofanyia hesabu ya faida na kuhifadhi zitaonekana hapa." else "Product calculations you save will appear here.",
                        actionButtonText = if (isSwahili) "Hesabu Bidhaa Mpya" else "Calculate New Product",
                        onActionClick = { selectedTab = 0 }
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(savedProducts) { _, prod ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = prod.productName,
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Text(
                                                text = "${prod.quantity.toInt()} pcs | Gharama: ${formatTsh(prod.totalCostPerUnit)} | Bei: ${formatTsh(prod.sellingPrice)}",
                                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            )
                                            Text(
                                                text = "Faida: ${formatTsh(prod.totalProfit)} (${String.format(Locale.US, "%.1f", prod.profitMarginPercentage)}%)",
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = MsaadaTeal
                                                )
                                            )
                                        }

                                        IconButton(
                                            onClick = { viewModel.deleteBiasharaProduct(prod) }
                                        ) {
                                            Icon(MsaadaIcons.Delete, contentDescription = "Futa", tint = Color(0xFFEF4444))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showProGate) {
        ProFeatureGateDialog(
            isSwahili = isSwahili,
            featureName = "Uchambuzi wa Juu wa Faida na Bidhaa Zote",
            onDismiss = { showProGate = false },
            onUpgrade = {
                showProGate = false
                onOpenPro()
            }
        )
    }
}

