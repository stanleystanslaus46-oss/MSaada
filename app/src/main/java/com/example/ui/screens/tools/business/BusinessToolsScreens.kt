package com.example.ui.screens.tools.business

import android.content.Context
import android.content.Intent
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material.icons.outlined.FolderCopy
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.RequestQuote
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Summarize
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.data.model.SavedDocument
import com.example.ui.components.MsaadaTopBar
import com.example.ui.components.ProBadge
import com.example.ui.screens.tools.finance.formatTsh
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaProGold
import com.example.ui.theme.MsaadaTeal
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// -------------------------------------------------------------
// 1. PROFIT CALCULATOR SCREEN
// -------------------------------------------------------------
@Composable
fun ProfitCalculatorScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var costPrice by remember { mutableStateOf("15000") }
    var sellingPrice by remember { mutableStateOf("25000") }
    var quantity by remember { mutableStateOf("10") }

    val cost = costPrice.toDoubleOrNull() ?: 0.0
    val sell = sellingPrice.toDoubleOrNull() ?: 0.0
    val qty = quantity.toDoubleOrNull() ?: 1.0

    val totalCost = cost * qty
    val totalRevenue = sell * qty
    val profit = totalRevenue - totalCost
    val profitMargin = if (totalRevenue > 0) (profit / totalRevenue) * 100.0 else 0.0
    val markupPercent = if (totalCost > 0) (profit / totalCost) * 100.0 else 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Profit Calculator" else "Profit Calculator",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (isSwahili) "Taarifa za Bidhaa" else "Product Pricing Details",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = costPrice,
                        onValueChange = { costPrice = it },
                        label = { Text(if (isSwahili) "Gharama ya Kununua kwa Kipande (TSh)" else "Cost Price per Unit (TZS)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = sellingPrice,
                        onValueChange = { sellingPrice = it },
                        label = { Text(if (isSwahili) "Bei ya Kuuza kwa Kipande (TSh)" else "Selling Price per Unit (TZS)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text(if (isSwahili) "Idadi ya Bidhaa / Vipande" else "Quantity / Units") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Results Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (profit >= 0) Color(0xFFD1FAE5) else Color(0xFFFEE2E2)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = if (profit >= 0) (if (isSwahili) "Faida Halisi (Net Profit)" else "Net Profit")
                               else (if (isSwahili) "Hasara (Loss)" else "Net Loss"),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (profit >= 0) Color(0xFF065F46) else Color(0xFF991B1B)
                        )
                    )
                    Text(
                        text = formatTsh(kotlin.math.abs(profit)),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = if (profit >= 0) Color(0xFF065F46) else Color(0xFF991B1B)
                        )
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = if (isSwahili) "Asilimia ya Faida" else "Profit Margin", style = MaterialTheme.typography.labelSmall)
                            Text(text = "%.1f%%".format(profitMargin), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        }
                        Column {
                            Text(text = if (isSwahili) "Markup %" else "Markup %", style = MaterialTheme.typography.labelSmall)
                            Text(text = "%.1f%%".format(markupPercent), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        }
                        Column {
                            Text(text = if (isSwahili) "Mauzo Yote" else "Total Revenue", style = MaterialTheme.typography.labelSmall)
                            Text(text = formatTsh(totalRevenue), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. DISCOUNT CALCULATOR SCREEN
// -------------------------------------------------------------
@Composable
fun DiscountCalculatorScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var originalPrice by remember { mutableStateOf("120000") }
    var discountPercent by remember { mutableStateOf("15") }

    val orig = originalPrice.toDoubleOrNull() ?: 0.0
    val disc = discountPercent.toDoubleOrNull() ?: 0.0

    val savings = orig * (disc / 100.0)
    val finalPrice = (orig - savings).coerceAtLeast(0.0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Discount Calculator" else "Discount Calculator",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (isSwahili) "Hesabu Punguzo la Bei" else "Calculate Discount",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = originalPrice,
                        onValueChange = { originalPrice = it },
                        label = { Text(if (isSwahili) "Bei Halisi ya Awali (TSh)" else "Original Price (TZS)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = discountPercent,
                        onValueChange = { discountPercent = it },
                        label = { Text(if (isSwahili) "Asilimia ya Punguzo (%)" else "Discount Percentage (%)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Results Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MsaadaTeal.copy(alpha = 0.12f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = if (isSwahili) "Bei Baada ya Punguzo" else "Final Price After Discount",
                        style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Text(
                        text = formatTsh(finalPrice),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = MsaadaNavy
                        )
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = if (isSwahili) "Kiasi Ulichookoa" else "You Save", style = MaterialTheme.typography.labelSmall)
                            Text(text = formatTsh(savings), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF059669)))
                        }
                        Column {
                            Text(text = if (isSwahili) "Bei ya Awali" else "Original Price", style = MaterialTheme.typography.labelSmall)
                            Text(text = formatTsh(orig), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 3. MARKUP CALCULATOR SCREEN
// -------------------------------------------------------------
@Composable
fun MarkupCalculatorScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var costPrice by remember { mutableStateOf("50000") }
    var markupPercent by remember { mutableStateOf("30") }

    val cost = costPrice.toDoubleOrNull() ?: 0.0
    val markup = markupPercent.toDoubleOrNull() ?: 0.0

    val profit = cost * (markup / 100.0)
    val sellingPrice = cost + profit

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Markup Calculator" else "Markup Calculator",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (isSwahili) "Hesabu Bei ya Kuuza kwa Markup" else "Calculate Selling Price via Markup",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = costPrice,
                        onValueChange = { costPrice = it },
                        label = { Text(if (isSwahili) "Gharama ya Ununuzi (TSh)" else "Cost Price (TZS)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = markupPercent,
                        onValueChange = { markupPercent = it },
                        label = { Text(if (isSwahili) "Asilimia ya Markup (%)" else "Markup Percentage (%)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Results Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MsaadaTeal.copy(alpha = 0.12f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = if (isSwahili) "Bei ya Kuuza Inayopendekezwa" else "Recommended Selling Price",
                        style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Text(
                        text = formatTsh(sellingPrice),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = MsaadaNavy
                        )
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = if (isSwahili) "Faida kwa Kipande" else "Profit per Unit", style = MaterialTheme.typography.labelSmall)
                            Text(text = formatTsh(profit), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF059669)))
                        }
                        Column {
                            Text(text = if (isSwahili) "Gharama" else "Cost", style = MaterialTheme.typography.labelSmall)
                            Text(text = formatTsh(cost), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 4. DOCUMENT GENERATOR & PREVIEW SYSTEM
// -------------------------------------------------------------
data class DocItemRow(
    val description: String,
    val quantity: Double,
    val unitPrice: Double
) {
    val total: Double get() = quantity * unitPrice
}

@Composable
fun DocumentEditorScreen(
    docType: String, // "INVOICE", "QUOTATION", "RECEIPT", "EXPENSE", "REPORT"
    isSwahili: Boolean,
    isProUser: Boolean,
    onSaveDoc: (SavedDocument) -> Unit,
    onPreviewDoc: (docType: String, title: String, client: String, amount: Double, json: String) -> Unit,
    onBack: () -> Unit
) {
    var businessName by remember { mutableStateOf("Sinza Tech Solutions") }
    var businessPhone by remember { mutableStateOf("+255 712 345 678") }
    var businessAddress by remember { mutableStateOf("Dar es Salaam, Tanzania") }
    var clientName by remember { mutableStateOf("Mteja: Alpha Enterprises") }
    var docNumber by remember { mutableStateOf("INV-2026-001") }
    var dateString by remember { mutableStateOf(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())) }
    var paymentMethod by remember { mutableStateOf("Lipa kwa M-Pesa / Tigo Pesa") }
    var notes by remember { mutableStateOf("Asante sana kwa biashara yako! Malipo yafanyike ndani ya siku 14.") }
    var includeVat by remember { mutableStateOf(false) }
    var discountInput by remember { mutableStateOf("0") }

    val items = remember {
        mutableStateListOf(
            DocItemRow(if (isSwahili) "Utengenezaji wa Tovuti (Website Design)" else "Website Design", 1.0, 450000.0),
            DocItemRow(if (isSwahili) "Hosting & Domain (Mwaka 1)" else "Hosting & Domain (1 Year)", 1.0, 120000.0),
            DocItemRow(if (isSwahili) "Matengenezo na Usaidizi wa Kiufundi" else "Technical Support", 2.0, 50000.0)
        )
    }

    var newItemDesc by remember { mutableStateOf("") }
    var newItemQty by remember { mutableStateOf("1") }
    var newItemPrice by remember { mutableStateOf("") }

    val subtotal = items.sumOf { it.total }
    val discount = discountInput.toDoubleOrNull() ?: 0.0
    val vatAmount = if (includeVat) (subtotal - discount).coerceAtLeast(0.0) * 0.18 else 0.0
    val grandTotal = (subtotal - discount + vatAmount).coerceAtLeast(0.0)

    val docTitle = when (docType) {
        "QUOTATION" -> if (isSwahili) "Makisio ya Bei (Quotation)" else "Price Quotation"
        "RECEIPT" -> if (isSwahili) "Risiti ya Malipo (Receipt)" else "Payment Receipt"
        "EXPENSE" -> if (isSwahili) "Ripoti ya Matumizi (Expense Report)" else "Expense Report"
        "REPORT" -> if (isSwahili) "Ripoti ya Biashara (Business Report)" else "Business Report"
        else -> if (isSwahili) "Ankara ya Mauzo (Invoice)" else "Sales Invoice"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = docTitle,
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Business details card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isSwahili) "Taarifa za Biashara Yako" else "Your Business Info",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = businessName,
                        onValueChange = { businessName = it },
                        label = { Text(if (isSwahili) "Jina la Biashara / Kampuni" else "Business Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = businessPhone,
                        onValueChange = { businessPhone = it },
                        label = { Text(if (isSwahili) "Simu / Mawasiliano" else "Phone / Contact") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = businessAddress,
                        onValueChange = { businessAddress = it },
                        label = { Text(if (isSwahili) "Mahali / Eneo" else "Address / Location") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Client & Meta details
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isSwahili) "Taarifa za Mteja & Hati" else "Client & Document Info",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = clientName,
                        onValueChange = { clientName = it },
                        label = { Text(if (isSwahili) "Jina la Mteja / Kampuni" else "Client Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = docNumber,
                            onValueChange = { docNumber = it },
                            label = { Text(if (isSwahili) "Namba ya Hati" else "Ref Number") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = dateString,
                            onValueChange = { dateString = it },
                            label = { Text(if (isSwahili) "Tarehe" else "Date") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                    if (docType == "RECEIPT") {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = paymentMethod,
                            onValueChange = { paymentMethod = it },
                            label = { Text(if (isSwahili) "Njia ya Malipo (M-Pesa / Tigo Pesa / Benki / Taslimu)" else "Payment Method") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            // Items Table
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isSwahili) "Orodha ya Bidhaa / Huduma" else "Items & Services List",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    items.forEachIndexed { idx, row ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = row.description, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                Text(
                                    text = "${row.quantity} x ${formatTsh(row.unitPrice)}",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                            Text(
                                text = formatTsh(row.total),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            IconButton(
                                onClick = { items.removeAt(idx) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(imageVector = Icons.Outlined.Close, contentDescription = "Remove", tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
                            }
                        }
                        if (idx < items.size - 1) HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Add Item Form
                    Text(
                        text = if (isSwahili) "+ Ongeza Bidhaa Mpya" else "+ Add New Item",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = MsaadaTeal)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newItemDesc,
                        onValueChange = { newItemDesc = it },
                        placeholder = { Text(if (isSwahili) "Maelezo ya bidhaa" else "Item description") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = newItemQty,
                            onValueChange = { newItemQty = it },
                            placeholder = { Text(if (isSwahili) "Idadi" else "Qty") },
                            modifier = Modifier.weight(0.4f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = newItemPrice,
                            onValueChange = { newItemPrice = it },
                            placeholder = { Text(if (isSwahili) "Bei ya Kipande" else "Unit Price") },
                            modifier = Modifier.weight(0.6f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = {
                            val q = newItemQty.toDoubleOrNull() ?: 1.0
                            val p = newItemPrice.toDoubleOrNull() ?: 0.0
                            if (newItemDesc.isNotBlank() && p > 0) {
                                items.add(DocItemRow(newItemDesc.trim(), q, p))
                                newItemDesc = ""
                                newItemQty = "1"
                                newItemPrice = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = if (isSwahili) "Weka Kwenye Orodha" else "Add Item")
                    }
                }
            }

            // Taxes & Totals Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = includeVat,
                                onCheckedChange = { includeVat = it },
                                colors = CheckboxDefaults.colors(checkedColor = MsaadaTeal)
                            )
                            Text(text = if (isSwahili) "Weka Kodi ya Ongezeko la Thamani (VAT 18%)" else "Include VAT (18%)")
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = discountInput,
                        onValueChange = { discountInput = it },
                        label = { Text(if (isSwahili) "Punguzo (TSh)" else "Discount (TZS)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = if (isSwahili) "Jumla Ndogo:" else "Subtotal:")
                        Text(text = formatTsh(subtotal), fontWeight = FontWeight.SemiBold)
                    }
                    if (discount > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = if (isSwahili) "Punguzo:" else "Discount:", color = Color(0xFF059669))
                            Text(text = "- ${formatTsh(discount)}", color = Color(0xFF059669), fontWeight = FontWeight.SemiBold)
                        }
                    }
                    if (includeVat) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "VAT (18%):")
                            Text(text = formatTsh(vatAmount), fontWeight = FontWeight.SemiBold)
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isSwahili) "JUMLA KUU:" else "GRAND TOTAL:",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                        )
                        Text(
                            text = formatTsh(grandTotal),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = MsaadaNavy
                            )
                        )
                    }
                }
            }

            // Action: Generate / Preview
            Button(
                onClick = {
                    val jsonObj = JSONObject()
                    jsonObj.put("businessName", businessName)
                    jsonObj.put("businessPhone", businessPhone)
                    jsonObj.put("businessAddress", businessAddress)
                    jsonObj.put("clientName", clientName)
                    jsonObj.put("docNumber", docNumber)
                    jsonObj.put("dateString", dateString)
                    jsonObj.put("notes", notes)
                    jsonObj.put("subtotal", subtotal)
                    jsonObj.put("discount", discount)
                    jsonObj.put("vatAmount", vatAmount)
                    jsonObj.put("grandTotal", grandTotal)
                    jsonObj.put("paymentMethod", paymentMethod)

                    val itemsArray = JSONArray()
                    items.forEach { itm ->
                        val obj = JSONObject()
                        obj.put("desc", itm.description)
                        obj.put("qty", itm.quantity)
                        obj.put("price", itm.unitPrice)
                        obj.put("total", itm.total)
                        itemsArray.put(obj)
                    }
                    jsonObj.put("items", itemsArray)

                    val savedDoc = SavedDocument(
                        docType = docType,
                        title = "$docTitle #$docNumber",
                        customerOrClient = clientName,
                        totalAmount = grandTotal,
                        contentJson = jsonObj.toString()
                    )
                    onSaveDoc(savedDoc)
                    onPreviewDoc(docType, "$docTitle #$docNumber", clientName, grandTotal, jsonObj.toString())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("doc_generate_preview_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MsaadaNavy)
            ) {
                Text(
                    text = if (isSwahili) "Tengeneza & Tazama Hati (PDF)" else "Generate & Preview Document",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color.White)
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 5. DOCUMENT PREVIEW SCREEN (AUTHENTIC TANZANIAN INVOICE / RECEIPT)
// -------------------------------------------------------------
@Composable
fun DocumentPreviewScreen(
    docType: String,
    title: String,
    customer: String,
    totalAmount: Double,
    jsonContent: String,
    isSwahili: Boolean,
    isProUser: Boolean,
    onOpenPro: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val jsonObj = remember {
        try {
            JSONObject(jsonContent)
        } catch (e: Exception) {
            JSONObject()
        }
    }

    val businessName = jsonObj.optString("businessName", "Sinza Tech Solutions")
    val businessPhone = jsonObj.optString("businessPhone", "+255 712 345 678")
    val businessAddress = jsonObj.optString("businessAddress", "Dar es Salaam, Tanzania")
    val clientName = jsonObj.optString("clientName", customer)
    val docNumber = jsonObj.optString("docNumber", "INV-001")
    val dateString = jsonObj.optString("dateString", "2026-09-07")
    val notes = jsonObj.optString("notes", "Asante kwa biashara yako!")
    val paymentMethod = jsonObj.optString("paymentMethod", "M-Pesa / Tigo Pesa")
    val subtotal = jsonObj.optDouble("subtotal", totalAmount)
    val discount = jsonObj.optDouble("discount", 0.0)
    val vatAmount = jsonObj.optDouble("vatAmount", 0.0)
    val grandTotal = jsonObj.optDouble("grandTotal", totalAmount)

    val itemsList = remember {
        val list = mutableListOf<DocItemRow>()
        val arr = jsonObj.optJSONArray("items")
        if (arr != null) {
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    DocItemRow(
                        description = obj.optString("desc", "Huduma"),
                        quantity = obj.optDouble("qty", 1.0),
                        unitPrice = obj.optDouble("price", 0.0)
                    )
                )
            }
        }
        list
    }

    // Export PDF function
    fun generatePdfFile(): File {
        val pdfDoc = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 standard
        val page = pdfDoc.startPage(pageInfo)
        val canvas = page.canvas

        val paint = Paint()
        paint.isAntiAlias = true

        // Draw header background banner
        paint.color = AndroidColor.parseColor("#001848")
        canvas.drawRect(0f, 0f, 595f, 90f, paint)

        // Title on banner
        paint.color = AndroidColor.WHITE
        paint.textSize = 22f
        paint.isFakeBoldText = true
        canvas.drawText("MSAADA - $title", 36f, 52f, paint)

        paint.textSize = 10f
        paint.isFakeBoldText = false
        canvas.drawText("Zana Rahisi. Kazi Rahisi. Maisha Rahisi.", 36f, 72f, paint)

        // Business info
        paint.color = AndroidColor.BLACK
        paint.textSize = 14f
        paint.isFakeBoldText = true
        canvas.drawText(businessName, 36f, 125f, paint)

        paint.textSize = 10f
        paint.isFakeBoldText = false
        paint.color = AndroidColor.DKGRAY
        canvas.drawText(businessPhone, 36f, 142f, paint)
        canvas.drawText(businessAddress, 36f, 157f, paint)

        // Document Meta info on right
        paint.color = AndroidColor.BLACK
        paint.textSize = 11f
        paint.isFakeBoldText = true
        canvas.drawText("Hati: #$docNumber", 380f, 125f, paint)
        paint.isFakeBoldText = false
        canvas.drawText("Tarehe: $dateString", 380f, 142f, paint)
        canvas.drawText("Mteja: $clientName", 380f, 157f, paint)

        // Divider
        paint.color = AndroidColor.LTGRAY
        canvas.drawLine(36f, 180f, 559f, 180f, paint)

        // Table Header
        var y = 210f
        paint.color = AndroidColor.parseColor("#008C95")
        paint.isFakeBoldText = true
        paint.textSize = 11f
        canvas.drawText("Maelezo", 36f, y, paint)
        canvas.drawText("Idadi", 340f, y, paint)
        canvas.drawText("Bei (TSh)", 410f, y, paint)
        canvas.drawText("Jumla (TSh)", 490f, y, paint)

        paint.color = AndroidColor.LTGRAY
        canvas.drawLine(36f, y + 6f, 559f, y + 6f, paint)

        // Table Rows
        paint.color = AndroidColor.BLACK
        paint.isFakeBoldText = false
        y += 24f

        itemsList.forEach { item ->
            canvas.drawText(item.description.take(36), 36f, y, paint)
            canvas.drawText(item.quantity.toString(), 340f, y, paint)
            canvas.drawText(item.unitPrice.toInt().toString(), 410f, y, paint)
            canvas.drawText(item.total.toInt().toString(), 490f, y, paint)
            y += 20f
        }

        // Divider
        y += 10f
        paint.color = AndroidColor.LTGRAY
        canvas.drawLine(36f, y, 559f, y, paint)
        y += 24f

        // Totals Box
        paint.color = AndroidColor.BLACK
        paint.isFakeBoldText = false
        canvas.drawText("Jumla Ndogo:", 380f, y, paint)
        canvas.drawText(formatTsh(subtotal), 480f, y, paint)
        y += 18f

        if (discount > 0) {
            canvas.drawText("Punguzo:", 380f, y, paint)
            canvas.drawText("- ${formatTsh(discount)}", 480f, y, paint)
            y += 18f
        }

        if (vatAmount > 0) {
            canvas.drawText("VAT (18%):", 380f, y, paint)
            canvas.drawText(formatTsh(vatAmount), 480f, y, paint)
            y += 18f
        }

        paint.isFakeBoldText = true
        paint.textSize = 13f
        canvas.drawText("JUMLA KUU:", 380f, y, paint)
        canvas.drawText(formatTsh(grandTotal), 480f, y, paint)

        // Payment and notes
        y = 650f
        paint.textSize = 10f
        paint.isFakeBoldText = true
        canvas.drawText("Maelezo ya Malipo:", 36f, y, paint)
        paint.isFakeBoldText = false
        canvas.drawText(paymentMethod, 36f, y + 16f, paint)
        canvas.drawText(notes, 36f, y + 32f, paint)

        // Signature line
        canvas.drawLine(380f, y + 40f, 540f, y + 40f, paint)
        canvas.drawText("Saini ya Mamlaka", 410f, y + 55f, paint)

        // Watermark if not pro
        if (!isProUser) {
            paint.color = AndroidColor.LTGRAY
            paint.textSize = 9f
            canvas.drawText("Imeundwa na programu ya MSAADA Tanzania. Pata toleo la PRO kuondoa alama hii.", 36f, 810f, paint)
        }

        pdfDoc.finishPage(page)

        val docsFolder = File(context.cacheDir, "docs")
        docsFolder.mkdirs()
        val pdfFile = File(docsFolder, "MSAADA_${docNumber}_${System.currentTimeMillis()}.pdf")
        val outStream = FileOutputStream(pdfFile)
        pdfDoc.writeTo(outStream)
        outStream.flush()
        outStream.close()
        pdfDoc.close()

        return pdfFile
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Hakiki Hati ya Kitaalamu" else "Document Preview",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Document Card (Simulates authentic physical invoice/receipt)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = businessName,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = MsaadaNavy
                                )
                            )
                            Text(text = businessPhone, style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray))
                            Text(text = businessAddress, style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray))
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = docType,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = MsaadaTeal
                                )
                            )
                            Text(text = "#$docNumber", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = Color.Gray))
                            Text(text = dateString, style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = Color(0xFFE2E8F0))

                    // Client info box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF8FAFC))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text = if (isSwahili) "IMETOLEWA KWA:" else "ISSUED TO:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.Gray)
                            )
                            Text(
                                text = clientName,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color.Black)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Items table
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = if (isSwahili) "Kipengele" else "Item", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = MsaadaTeal))
                            Text(text = if (isSwahili) "Jumla" else "Total", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = MsaadaTeal))
                        }
                        HorizontalDivider(color = Color(0xFFE2E8F0))

                        itemsList.forEach { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = item.description, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, color = Color.Black))
                                    Text(text = "${item.quantity} x ${formatTsh(item.unitPrice)}", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                                }
                                Text(text = formatTsh(item.total), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color.Black))
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE2E8F0))

                    // Totals
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(0.6f),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = if (isSwahili) "Jumla Ndogo:" else "Subtotal:", color = Color.DarkGray, fontSize = 12.sp)
                            Text(text = formatTsh(subtotal), fontWeight = FontWeight.SemiBold, color = Color.Black, fontSize = 12.sp)
                        }
                        if (discount > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(0.6f),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = if (isSwahili) "Punguzo:" else "Discount:", color = Color(0xFF059669), fontSize = 12.sp)
                                Text(text = "- ${formatTsh(discount)}", fontWeight = FontWeight.Bold, color = Color(0xFF059669), fontSize = 12.sp)
                            }
                        }
                        if (vatAmount > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(0.6f),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "VAT (18%):", color = Color.DarkGray, fontSize = 12.sp)
                                Text(text = formatTsh(vatAmount), fontWeight = FontWeight.SemiBold, color = Color.Black, fontSize = 12.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(0.6f),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isSwahili) "JUMLA KUU:" else "TOTAL:",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black, color = MsaadaNavy)
                            )
                            Text(
                                text = formatTsh(grandTotal),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = MsaadaNavy)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Footer notes
                    Text(
                        text = "Taarifa za Malipo: $paymentMethod",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.DarkGray, fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = notes,
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Signature
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .width(130.dp)
                                    .height(1.dp)
                                    .background(Color.Gray)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isSwahili) "Saini Iliyoidhinishwa" else "Authorized Signature",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray, fontSize = 10.sp)
                            )
                        }
                    }
                }
            }

            // Buttons: Download PDF & Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        try {
                            val pdfFile = generatePdfFile()
                            Toast.makeText(
                                context,
                                if (isSwahili) "PDF imehifadhiwa: ${pdfFile.name}" else "PDF saved: ${pdfFile.name}",
                                Toast.LENGTH_LONG
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Hitilafu katika kutengeneza PDF", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("doc_download_pdf_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MsaadaTeal)
                ) {
                    Icon(imageVector = Icons.Outlined.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = if (isSwahili) "Pakua PDF" else "Download PDF")
                }

                OutlinedButton(
                    onClick = {
                        try {
                            val pdfFile = generatePdfFile()
                            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", pdfFile)
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/pdf"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                putExtra(Intent.EXTRA_SUBJECT, title)
                                putExtra(Intent.EXTRA_TEXT, "Tafadhali pokea hati ya $title kutoka $businessName.")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Shiriki Hati ya PDF"))
                        } catch (e: Exception) {
                            Toast.makeText(context, "Hitilafu katika kushiriki PDF", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("doc_share_pdf_button"),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(imageVector = Icons.Outlined.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = if (isSwahili) "Shiriki" else "Share")
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 6. TEMPLATES HUB SCREEN
// -------------------------------------------------------------
data class TemplateItem(
    val id: String,
    val title: String,
    val category: String,
    val docType: String,
    val isPro: Boolean,
    val icon: ImageVector
)

@Composable
fun TemplatesScreen(
    isSwahili: Boolean,
    isProUser: Boolean,
    onOpenDocWithTemplate: (docType: String) -> Unit,
    onOpenPro: () -> Unit,
    onBack: () -> Unit
) {
    val templates = listOf(
        TemplateItem("inv_modern", if (isSwahili) "Ankara ya Mauzo ya Kisasa" else "Modern Sales Invoice", "Biashara", "INVOICE", false, Icons.Outlined.ReceiptLong),
        TemplateItem("quot_classic", if (isSwahili) "Makisio ya Bei (Proforma Quotation)" else "Proforma Quotation", "Biashara", "QUOTATION", false, Icons.Outlined.RequestQuote),
        TemplateItem("receipt_mpesa", if (isSwahili) "Risiti Rasmi ya Malipo (M-Pesa / Benki)" else "Official Payment Receipt", "Biashara", "RECEIPT", false, Icons.Outlined.Receipt),
        TemplateItem("expense_tracker", if (isSwahili) "Ripoti ya Matumizi ya Biashara" else "Business Expense Report", "Biashara", "EXPENSE", false, Icons.Outlined.Assessment),
        TemplateItem("biz_summary", if (isSwahili) "Ripoti ya Faida na Mauzo ya Mwezi" else "Monthly Profit & Sales Report", "Biashara", "REPORT", true, Icons.Outlined.Summarize),
        TemplateItem("church_pledge", if (isSwahili) "Fomu ya Ahadi & Michango ya Kanisa" else "Church Pledge Form", "Kanisa", "RECEIPT", false, Icons.Outlined.VolunteerActivism),
        TemplateItem("church_program", if (isSwahili) "Ratiba ya Ibada & Matukio" else "Church Service Program", "Kanisa", "REPORT", true, Icons.Outlined.EventNote),
        TemplateItem("study_schedule", if (isSwahili) "Ratiba ya Masomo & Mitihani" else "Exam & Study Timetable", "Elimu", "REPORT", false, Icons.Outlined.MenuBook)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Violezo vya Kitaalamu" else "Professional Templates",
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MsaadaTeal.copy(alpha = 0.12f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FolderCopy,
                            contentDescription = null,
                            tint = MsaadaTeal,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = if (isSwahili) "Violezo Tayari vya Kutumia" else "Ready-To-Use Templates",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = MsaadaNavy)
                            )
                            Text(
                                text = if (isSwahili) "Chagua kiolezo kisha jaza taarifa zako kwa dakika moja tu." else "Pick a template and fill your information in seconds.",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }
                }
            }

            items(templates) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            if (item.isPro && !isProUser) {
                                onOpenPro()
                            } else {
                                onOpenDocWithTemplate(item.docType)
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                if (item.isPro) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    ProBadge()
                                }
                            }
                            Text(
                                text = "${item.category} • ${item.docType}",
                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }

                        Button(
                            onClick = {
                                if (item.isPro && !isProUser) {
                                    onOpenPro()
                                } else {
                                    onOpenDocWithTemplate(item.docType)
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (item.isPro && !isProUser) MsaadaProGold else MsaadaTeal
                            )
                        ) {
                            Text(text = if (item.isPro && !isProUser) "PRO" else (if (isSwahili) "Tumia" else "Use"), fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
