package com.example.ui.screens.tools.finance

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
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MsaadaTopBar
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaTeal
import java.text.NumberFormat
import java.util.Locale

fun formatTsh(amount: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale.US)
    formatter.maximumFractionDigits = 2
    return "TSh ${formatter.format(amount)}"
}

// -------------------------------------------------------------
// 1. CALCULATOR SCREEN
// -------------------------------------------------------------
@Composable
fun CalculatorScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var expression by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("0") }
    val history = remember { mutableStateListOf<String>() }
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    fun calculateCurrent() {
        if (expression.isBlank()) return
        try {
            val sanitized = expression.replace("×", "*").replace("÷", "/")
            val res = evaluateSimpleMath(sanitized)
            val formatted = if (res % 1.0 == 0.0) res.toLong().toString() else "%.4f".format(res).trimEnd('0').trimEnd('.')
            resultText = formatted
            history.add(0, "$expression = $formatted")
        } catch (e: Exception) {
            resultText = if (isSwahili) "Hitilafu" else "Error"
        }
    }

    fun onKeyClick(key: String) {
        when (key) {
            "C" -> {
                expression = ""
                resultText = "0"
            }
            "⌫" -> {
                if (expression.isNotEmpty()) {
                    expression = expression.dropLast(1)
                }
            }
            "=" -> {
                calculateCurrent()
            }
            "+", "-", "×", "÷", "%" -> {
                if (expression.isNotEmpty() && !expression.last().isDigit() && expression.last() != '.') {
                    expression = expression.dropLast(1) + key
                } else if (expression.isNotEmpty()) {
                    expression += key
                }
            }
            else -> {
                expression += key
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Calculator" else "Calculator",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 8.dp)
        ) {
            // Display Area
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.35f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (history.isNotEmpty()) "Historia (${history.size})" else "",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        if (resultText != "0" && resultText != "Error") {
                            IconButton(onClick = {
                                clipboard.setText(AnnotatedString(resultText))
                                Toast.makeText(context, if (isSwahili) "Imenakiliwa!" else "Copied!", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(imageVector = Icons.Outlined.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = expression.ifEmpty { "0" },
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = resultText,
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Keypad Grid
            val keys = listOf(
                listOf("C", "÷", "×", "⌫"),
                listOf("7", "8", "9", "-"),
                listOf("4", "5", "6", "+"),
                listOf("1", "2", "3", "%"),
                listOf("0", "00", ".", "=")
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.65f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                keys.forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { key ->
                            val isOp = key in listOf("÷", "×", "-", "+", "%")
                            val isEquals = key == "="
                            val isClear = key in listOf("C", "⌫")

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        when {
                                            isEquals -> MsaadaNavy
                                            isOp -> MsaadaTeal.copy(alpha = 0.15f)
                                            isClear -> Color(0xFFFEE2E2)
                                            else -> MaterialTheme.colorScheme.surface
                                        }
                                    )
                                    .clickable { onKeyClick(key) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = key,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = when {
                                            isEquals -> Color.White
                                            isOp -> MsaadaTeal
                                            isClear -> Color(0xFFDC2626)
                                            else -> MaterialTheme.colorScheme.onSurface
                                        }
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Simple Math Evaluator
fun evaluateSimpleMath(str: String): Double {
    val tokens = mutableListOf<String>()
    var numberBuffer = ""
    for (ch in str) {
        if (ch.isDigit() || ch == '.') {
            numberBuffer += ch
        } else if (ch in listOf('+', '-', '*', '/', '%')) {
            if (numberBuffer.isNotEmpty()) {
                tokens.add(numberBuffer)
                numberBuffer = ""
            }
            tokens.add(ch.toString())
        }
    }
    if (numberBuffer.isNotEmpty()) tokens.add(numberBuffer)

    if (tokens.isEmpty()) return 0.0

    // Multiply, Divide, Percent
    var i = 0
    while (i < tokens.size) {
        val t = tokens[i]
        if (t == "*" || t == "/" || t == "%") {
            val prev = tokens[i - 1].toDoubleOrNull() ?: 0.0
            val next = tokens.getOrNull(i + 1)?.toDoubleOrNull() ?: 1.0
            val computed = when (t) {
                "*" -> prev * next
                "/" -> if (next != 0.0) prev / next else 0.0
                "%" -> prev * (next / 100.0)
                else -> prev
            }
            tokens[i - 1] = computed.toString()
            tokens.removeAt(i)
            if (i < tokens.size) tokens.removeAt(i)
            i--
        }
        i++
    }

    // Add, Subtract
    var result = tokens[0].toDoubleOrNull() ?: 0.0
    i = 1
    while (i < tokens.size) {
        val op = tokens[i]
        val next = tokens.getOrNull(i + 1)?.toDoubleOrNull() ?: 0.0
        if (op == "+") result += next
        if (op == "-") result -= next
        i += 2
    }

    return result
}

// -------------------------------------------------------------
// 2. PERCENTAGE CALCULATOR SCREEN
// -------------------------------------------------------------
@Composable
fun PercentageCalculatorScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    // Mode 1: What is X% of Y?
    var mode1Percent by remember { mutableStateOf("18") }
    var mode1Total by remember { mutableStateOf("100000") }

    // Mode 2: Percentage increase/decrease from X to Y
    var mode2From by remember { mutableStateOf("50000") }
    var mode2To by remember { mutableStateOf("65000") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Percentage Calculator" else "Percentage Calculator",
            onBack = onBack
        )

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(text = if (isSwahili) "Asilimia ya Kiasi" else "Percentage Of") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text(text = if (isSwahili) "Ongezeko / Upungufu" else "Change %") }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (selectedTab == 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = if (isSwahili) "Je, asilimia X ni kiasi gani cha Y?" else "What is X% of Y?",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = mode1Percent,
                            onValueChange = { mode1Percent = it },
                            label = { Text(if (isSwahili) "Asilimia (%)" else "Percentage (%)") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = mode1Total,
                            onValueChange = { mode1Total = it },
                            label = { Text(if (isSwahili) "Jumla Kuu (TSh)" else "Total Amount (TZS)") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        val pct = mode1Percent.toDoubleOrNull() ?: 0.0
                        val tot = mode1Total.toDoubleOrNull() ?: 0.0
                        val calculatedAmount = tot * (pct / 100.0)

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MsaadaTeal.copy(alpha = 0.1f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (isSwahili) "Jibu:" else "Result:",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                                Text(
                                    text = formatTsh(calculatedAmount),
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = MsaadaTeal
                                    )
                                )
                                Text(
                                    text = if (isSwahili)
                                        "$pct% ya ${formatTsh(tot)} ni ${formatTsh(calculatedAmount)}"
                                    else
                                        "$pct% of ${formatTsh(tot)} is ${formatTsh(calculatedAmount)}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = if (isSwahili) "Kiwango cha Ongezeko au Upungufu" else "Percentage Increase or Decrease",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = mode2From,
                            onValueChange = { mode2From = it },
                            label = { Text(if (isSwahili) "Thamani ya Awali (Kutoka)" else "Initial Value (From)") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = mode2To,
                            onValueChange = { mode2To = it },
                            label = { Text(if (isSwahili) "Thamani Mpya (Hadi)" else "New Value (To)") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        val from = mode2From.toDoubleOrNull() ?: 0.0
                        val to = mode2To.toDoubleOrNull() ?: 0.0
                        val diff = to - from
                        val pctChange = if (from != 0.0) (diff / from) * 100.0 else 0.0
                        val isIncrease = diff >= 0

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isIncrease) Color(0xFFD1FAE5) else Color(0xFFFEE2E2)
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (isIncrease) (if (isSwahili) "Ongezeko" else "Increase") else (if (isSwahili) "Upungufu" else "Decrease"),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isIncrease) Color(0xFF065F46) else Color(0xFF991B1B)
                                    )
                                )
                                Text(
                                    text = "%.2f%%".format(kotlin.math.abs(pctChange)),
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = if (isIncrease) Color(0xFF065F46) else Color(0xFF991B1B)
                                    )
                                )
                                Text(
                                    text = if (isSwahili) "Tofauti: ${formatTsh(diff)}" else "Difference: ${formatTsh(diff)}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (isIncrease) Color(0xFF047857) else Color(0xFFB91C1C)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 3. BUDGET CALCULATOR SCREEN
// -------------------------------------------------------------
data class BudgetItem(val id: Long, val name: String, val amount: Double, val isIncome: Boolean)

@Composable
fun BudgetCalculatorScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var itemName by remember { mutableStateOf("") }
    var itemAmount by remember { mutableStateOf("") }
    var isIncomeMode by remember { mutableStateOf(false) }

    val items = remember {
        mutableStateListOf(
            BudgetItem(1, if (isSwahili) "Mshahara" else "Salary", 800000.0, true),
            BudgetItem(2, if (isSwahili) "Biashara Ndogo" else "Side Business", 250000.0, true),
            BudgetItem(3, if (isSwahili) "Kodi ya Nyumba" else "House Rent", 250000.0, false),
            BudgetItem(4, if (isSwahili) "Chakula cha Mwezi" else "Monthly Food", 300000.0, false),
            BudgetItem(5, if (isSwahili) "Umeme & Maji (LUKU)" else "Electricity & Water", 60000.0, false),
            BudgetItem(6, if (isSwahili) "Ada ya Watoto" else "School Fees", 150000.0, false)
        )
    }

    val totalIncome = items.filter { it.isIncome }.sumOf { it.amount }
    val totalExpenses = items.filter { !it.isIncome }.sumOf { it.amount }
    val remaining = totalIncome - totalExpenses

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Bajeti ya Mapato & Matumizi" else "Budget Calculator",
            onBack = onBack
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
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = if (isSwahili) "Muhtasari wa Bajeti" else "Budget Summary",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = if (isSwahili) "Mapato Yote" else "Total Income", style = MaterialTheme.typography.labelSmall)
                                Text(text = formatTsh(totalIncome), style = MaterialTheme.typography.titleSmall.copy(color = Color(0xFF10B981), fontWeight = FontWeight.Bold))
                            }
                            Column {
                                Text(text = if (isSwahili) "Matumizi Yote" else "Total Expenses", style = MaterialTheme.typography.labelSmall)
                                Text(text = formatTsh(totalExpenses), style = MaterialTheme.typography.titleSmall.copy(color = Color(0xFFEF4444), fontWeight = FontWeight.Bold))
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = if (isSwahili) "Kiasi Kilichobaki" else "Remaining Balance", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                                Text(
                                    text = formatTsh(remaining),
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = if (remaining >= 0) MsaadaTeal else Color(0xFFEF4444)
                                    )
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (remaining >= 0) Color(0xFFD1FAE5) else Color(0xFFFEE2E2))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = if (remaining >= 0) (if (isSwahili) "Bajeti Salama" else "Surplus") else (if (isSwahili) "Upungufu" else "Deficit"),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (remaining >= 0) Color(0xFF065F46) else Color(0xFF991B1B)
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Input Form
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isSwahili) "Ongeza Kipengele Kwenye Bajeti" else "Add Budget Item",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { isIncomeMode = true },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isIncomeMode) Color(0xFFD1FAE5) else Color.Transparent
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(text = if (isSwahili) "+ Mapato" else "+ Income", color = if (isIncomeMode) Color(0xFF065F46) else MaterialTheme.colorScheme.onSurface)
                            }
                            OutlinedButton(
                                onClick = { isIncomeMode = false },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (!isIncomeMode) Color(0xFFFEE2E2) else Color.Transparent
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(text = if (isSwahili) "- Matumizi" else "- Expense", color = if (!isIncomeMode) Color(0xFF991B1B) else MaterialTheme.colorScheme.onSurface)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = itemName,
                            onValueChange = { itemName = it },
                            placeholder = { Text(if (isSwahili) "Jina (mfano: Usafiri, Kodi...)" else "Title (e.g. Transport, Rent...)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = itemAmount,
                            onValueChange = { itemAmount = it },
                            placeholder = { Text(if (isSwahili) "Kiasi (TSh)" else "Amount (TZS)") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                val amt = itemAmount.toDoubleOrNull() ?: 0.0
                                if (itemName.isNotBlank() && amt > 0) {
                                    items.add(0, BudgetItem(System.currentTimeMillis(), itemName.trim(), amt, isIncomeMode))
                                    itemName = ""
                                    itemAmount = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MsaadaTeal)
                        ) {
                            Text(text = if (isSwahili) "Weka kwenye Bajeti" else "Add to Budget")
                        }
                    }
                }
            }

            // Items List
            items(items) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (item.isIncome) Color(0xFFD1FAE5) else Color(0xFFFEE2E2)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (item.isIncome) Icons.Outlined.TrendingUp else Icons.Outlined.TrendingDown,
                                    contentDescription = null,
                                    tint = if (item.isIncome) Color(0xFF047857) else Color(0xFFDC2626),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = item.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                Text(
                                    text = if (item.isIncome) (if (isSwahili) "Mapato" else "Income") else (if (isSwahili) "Matumizi" else "Expense"),
                                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = (if (item.isIncome) "+ " else "- ") + formatTsh(item.amount),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (item.isIncome) Color(0xFF059669) else Color(0xFFDC2626)
                                )
                            )
                            IconButton(
                                onClick = { items.remove(item) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(imageVector = Icons.Outlined.Close, contentDescription = "Remove", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 4. SAVINGS CALCULATOR SCREEN
// -------------------------------------------------------------
@Composable
fun SavingsCalculatorScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var targetAmount by remember { mutableStateOf("5000000") }
    var currentSavings by remember { mutableStateOf("1000000") }
    var monthlyContribution by remember { mutableStateOf("200000") }

    val target = targetAmount.toDoubleOrNull() ?: 0.0
    val current = currentSavings.toDoubleOrNull() ?: 0.0
    val monthly = monthlyContribution.toDoubleOrNull() ?: 1.0

    val remaining = (target - current).coerceAtLeast(0.0)
    val monthsNeeded = if (monthly > 0) (remaining / monthly).toInt() + (if (remaining % monthly > 0) 1 else 0) else 0
    val years = monthsNeeded / 12
    val extraMonths = monthsNeeded % 12

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Savings Calculator" else "Savings Calculator",
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
                        text = if (isSwahili) "Panga Lengo Lako la Akiba" else "Plan Your Savings Target",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = targetAmount,
                        onValueChange = { targetAmount = it },
                        label = { Text(if (isSwahili) "Lengo la Akiba (TSh)" else "Target Savings Goal (TZS)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = currentSavings,
                        onValueChange = { currentSavings = it },
                        label = { Text(if (isSwahili) "Kiasi Ulichonacho Sasa (TSh)" else "Current Savings (TZS)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = monthlyContribution,
                        onValueChange = { monthlyContribution = it },
                        label = { Text(if (isSwahili) "Kiasi cha Kuweka Kila Mwezi (TSh)" else "Monthly Deposit (TZS)") },
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
                colors = CardDefaults.cardColors(containerColor = MsaadaTeal.copy(alpha = 0.12f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = if (isSwahili) "Muda Unaohitajika Kufikia Lengo" else "Estimated Time to Reach Goal",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = MsaadaTeal)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (years > 0)
                            "$years ${if (isSwahili) "mwaka" else "yr(s)"} & $extraMonths ${if (isSwahili) "miezi" else "mo(s)"} ($monthsNeeded ${if (isSwahili) "miezi jumla" else "total mos"})"
                        else
                            "$monthsNeeded ${if (isSwahili) "miezi" else "months"}",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = MsaadaNavy
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isSwahili)
                            "Kiasi kilichobaki kufikia lengo: ${formatTsh(remaining)}"
                        else
                            "Remaining amount to goal: ${formatTsh(remaining)}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 5. LOAN CALCULATOR SCREEN
// -------------------------------------------------------------
@Composable
fun LoanCalculatorScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var loanAmount by remember { mutableStateOf("3000000") }
    var interestRateAnnual by remember { mutableStateOf("12") }
    var periodMonths by remember { mutableStateOf("12") }

    val principal = loanAmount.toDoubleOrNull() ?: 0.0
    val annualRate = interestRateAnnual.toDoubleOrNull() ?: 0.0
    val months = periodMonths.toDoubleOrNull() ?: 1.0

    // Simple / Standard Amortization loan estimate
    val monthlyRate = (annualRate / 100.0) / 12.0
    val monthlyPayment = if (monthlyRate > 0 && months > 0) {
        val factor = Math.pow(1.0 + monthlyRate, months)
        principal * (monthlyRate * factor) / (factor - 1.0)
    } else if (months > 0) {
        principal / months
    } else 0.0

    val totalPayment = monthlyPayment * months
    val totalInterest = (totalPayment - principal).coerceAtLeast(0.0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Loan Calculator" else "Loan Calculator",
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
                        text = if (isSwahili) "Taarifa za Mkopo" else "Loan Details",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = loanAmount,
                        onValueChange = { loanAmount = it },
                        label = { Text(if (isSwahili) "Kiasi cha Mkopo (TSh)" else "Principal Amount (TZS)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = interestRateAnnual,
                        onValueChange = { interestRateAnnual = it },
                        label = { Text(if (isSwahili) "Riba kwa Mwaka (%)" else "Annual Interest Rate (%)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = periodMonths,
                        onValueChange = { periodMonths = it },
                        label = { Text(if (isSwahili) "Muda wa Marejesho (Miezi)" else "Duration (Months)") },
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
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = if (isSwahili) "Kadirio la Marejesho ya Kila Mwezi" else "Estimated Monthly Repayment",
                        style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Text(
                        text = formatTsh(monthlyPayment),
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
                            Text(text = if (isSwahili) "Jumla ya Riba" else "Total Interest", style = MaterialTheme.typography.labelSmall)
                            Text(text = formatTsh(totalInterest), style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFFDC2626)))
                        }
                        Column {
                            Text(text = if (isSwahili) "Jumla ya Marejesho" else "Total Payment", style = MaterialTheme.typography.labelSmall)
                            Text(text = formatTsh(totalPayment), style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = MsaadaNavy))
                        }
                    }
                }
            }

            Text(
                text = if (isSwahili)
                    "Kumbuka: Hesabu hizi ni makadirio tu kulingana na fomula ya kawaida ya riba. Masharti ya benki au taasisi ya mikopo yanaweza kutofautiana."
                else
                    "Note: These figures are estimates based on standard amortization formulas. Actual terms from lending institutions may vary.",
                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
    }
}

// -------------------------------------------------------------
// 6. TITHE & OFFERING CALCULATOR (ZAKA NA SADAKA)
// -------------------------------------------------------------
@Composable
fun TitheCalculatorScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var incomeInput by remember { mutableStateOf("500000") }
    var tithePercent by remember { mutableStateOf("10") }
    var extraOffering by remember { mutableStateOf("20000") }

    val income = incomeInput.toDoubleOrNull() ?: 0.0
    val pct = tithePercent.toDoubleOrNull() ?: 10.0
    val extra = extraOffering.toDoubleOrNull() ?: 0.0

    val titheAmount = income * (pct / 100.0)
    val totalGift = titheAmount + extra

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Kikokotoo cha Zaka & Sadaka" else "Tithe & Offering Calculator",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Scripture header card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E8FF))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isSwahili) "“Leteni zaka kamili ghalani…” — Malaki 3:10" else "“Bring the whole tithe into the storehouse…” — Malachi 3:10",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF6B21A8)
                        )
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (isSwahili) "Mapato na Sadaka Yako" else "Income & Offering Details",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = incomeInput,
                        onValueChange = { incomeInput = it },
                        label = { Text(if (isSwahili) "Mapato Yako (Mshahara / Biashara TSh)" else "Total Income (TZS)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = tithePercent,
                        onValueChange = { tithePercent = it },
                        label = { Text(if (isSwahili) "Asilimia ya Fungu la Kumi (%)" else "Tithe Percentage (%)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = extraOffering,
                        onValueChange = { extraOffering = it },
                        label = { Text(if (isSwahili) "Sadaka ya Ziada / Shukrani (TSh)" else "Extra Offering / Thanksgiving (TZS)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Results
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = if (isSwahili) "Fungu la Kumi (Zaka)" else "Tithe Amount", style = MaterialTheme.typography.labelSmall)
                            Text(text = formatTsh(titheAmount), style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = Color(0xFF7C3AED)))
                        }
                        Column {
                            Text(text = if (isSwahili) "Sadaka ya Ziada" else "Offering", style = MaterialTheme.typography.labelSmall)
                            Text(text = formatTsh(extra), style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = MsaadaTeal))
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = if (isSwahili) "Jumla ya Kutoa Kanisani" else "Total Church Gift", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                            Text(
                                text = formatTsh(totalGift),
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black, color = MsaadaNavy)
                            )
                        }
                    }
                }
            }
        }
    }
}
