package com.example.ui.screens.tools.home

import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ElectricBolt
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShoppingItem
import com.example.ui.components.MsaadaTopBar
import com.example.ui.screens.tools.finance.formatTsh
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaTeal

// -------------------------------------------------------------
// 1. SHOPPING LIST SCREEN
// -------------------------------------------------------------
@Composable
fun ShoppingListScreen(
    items: List<ShoppingItem>,
    isSwahili: Boolean,
    onAddItem: (title: String, quantity: String, category: String) -> Unit,
    onTogglePurchased: (ShoppingItem) -> Unit,
    onDeleteItem: (ShoppingItem) -> Unit,
    onBack: () -> Unit
) {
    var itemName by remember { mutableStateOf("") }
    var itemQty by remember { mutableStateOf("1") }
    var itemCategory by remember { mutableStateOf("Soko") }
    val context = LocalContext.current

    val totalItems = items.size
    val completedItems = items.count { it.isCompleted }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Orodha ya Ununuzi" else "Shopping List",
            onBack = onBack,
            actions = {
                if (items.isNotEmpty()) {
                    IconButton(onClick = {
                        val sb = StringBuilder()
                        sb.append(if (isSwahili) "Orodha ya Ununuzi (MSAADA):\n" else "Shopping List (MSAADA):\n")
                        items.forEach { itm ->
                            val mark = if (itm.isCompleted) "[✓]" else "[ ]"
                            sb.append("$mark ${itm.title} (${itm.quantity}) [${itm.category}]\n")
                        }
                        sb.append("\nJumla ya Bidhaa: $totalItems")
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, sb.toString())
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Shiriki Orodha"))
                    }) {
                        Icon(imageVector = Icons.Outlined.Share, contentDescription = "Share", tint = MsaadaNavy)
                    }
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Summary Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = if (isSwahili) "Jumla ya Bidhaa" else "Total Items", style = MaterialTheme.typography.labelSmall)
                            Text(text = "$totalItems", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = MsaadaNavy))
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = if (isSwahili) "Zilizokamilika" else "Purchased", style = MaterialTheme.typography.labelSmall)
                            Text(text = "$completedItems", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = Color(0xFF059669)))
                        }
                    }
                }
            }

            // Add Item Form
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isSwahili) "Ongeza Bidhaa Mpya" else "Add Item to List",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = itemName,
                            onValueChange = { itemName = it },
                            placeholder = { Text(if (isSwahili) "Jina la bidhaa (mfano: Mchele kg 5, Sabuni...)" else "Item name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = itemQty,
                                onValueChange = { itemQty = it },
                                placeholder = { Text(if (isSwahili) "Idadi / Kiasi" else "Quantity") },
                                modifier = Modifier.weight(0.5f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = itemCategory,
                                onValueChange = { itemCategory = it },
                                placeholder = { Text(if (isSwahili) "Aina (Soko, Duka...)" else "Category") },
                                modifier = Modifier.weight(0.5f),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (itemName.isNotBlank()) {
                                    onAddItem(
                                        itemName.trim(),
                                        itemQty.trim().ifBlank { "1" },
                                        itemCategory.trim().ifBlank { "Soko" }
                                    )
                                    itemName = ""
                                    itemQty = "1"
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MsaadaTeal)
                        ) {
                            Text(text = if (isSwahili) "Weka Kwenye Orodha" else "Add Item")
                        }
                    }
                }
            }

            items(items) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onTogglePurchased(item) },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (item.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                imageVector = if (item.isCompleted) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (item.isCompleted) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (item.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(
                                    text = "${item.quantity} • ${item.category}",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }

                        IconButton(onClick = { onDeleteItem(item) }) {
                            Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. LUKU ELECTRICITY CALCULATOR SCREEN (TANESCO TARIFFS)
// -------------------------------------------------------------
@Composable
fun LukuCalculatorScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var amountInput by remember { mutableStateOf("10000") }

    val amount = amountInput.toDoubleOrNull() ?: 0.0

    // TANESCO Domestic D1 Tariff Breakdown:
    // Rate: ~ 292 TSh / kWh
    // REA Levy: 3%
    // EWURA Regulatory fee: 1%
    // VAT: 18%
    // Deductions total ~ 22%
    val deductions = amount * 0.22
    val actualEnergyAmount = (amount - deductions).coerceAtLeast(0.0)
    val ratePerKwh = 292.0
    val unitsKwh = if (ratePerKwh > 0) actualEnergyAmount / ratePerKwh else 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Kikokotoo cha Umeme (LUKU)" else "LUKU Units Calculator",
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
                        text = if (isSwahili) "Ingiza Kiasi cha Kununua LUKU" else "Enter LUKU Purchase Amount",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = amountInput,
                        onValueChange = { amountInput = it },
                        label = { Text(if (isSwahili) "Kiasi cha Fedha (TSh)" else "Amount in TZS") },
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isSwahili) "Kadirio la Uniti za Umeme (kWh)" else "Estimated Electricity Units (kWh)",
                                style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                            Text(
                                text = "%.2f kWh".format(unitsKwh),
                                style = MaterialTheme.typography.displaySmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = MsaadaNavy
                                )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFEF3C7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Outlined.ElectricBolt, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(28.dp))
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = if (isSwahili) "Kodi ya VAT (18%):" else "VAT (18%):", style = MaterialTheme.typography.bodySmall)
                            Text(text = formatTsh(amount * 0.18), style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = if (isSwahili) "Ada ya EWURA & REA (4%):" else "EWURA & REA (4%):", style = MaterialTheme.typography.bodySmall)
                            Text(text = formatTsh(amount * 0.04), style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = if (isSwahili) "Thamani ya Umeme Halisi:" else "Net Energy Cost:", style = MaterialTheme.typography.bodySmall)
                            Text(text = formatTsh(actualEnergyAmount), style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = MsaadaTeal))
                        }
                    }
                }
            }

            Text(
                text = if (isSwahili)
                    "Kumbuka: Makadirio haya yanatumia kiwango cha kawaida cha nyumbani (Tariff D1). Bei halisi inaweza kutofautiana kulingana na deni la mita au tozo za manispaa."
                else
                    "Note: Calculations are based on standard domestic Tariff D1. Actual units received may vary if your meter has existing deductions or municipal levies.",
                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
    }
}
