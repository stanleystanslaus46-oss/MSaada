package com.example.ui.screens.tools.business

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import com.example.ui.theme.MsaadaIcons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.data.model.RisitiSafeReceipt
import com.example.ui.MsaadaViewModel
import com.example.ui.components.EmptyState
import com.example.ui.components.MsaadaTopBar
import com.example.ui.components.ProBadge
import com.example.ui.components.ProFeatureGateDialog
import com.example.ui.components.UpgradeToProCard
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaProGold
import com.example.ui.theme.MsaadaTeal
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ReceiptLineItem(
    var name: String = "",
    var quantity: String = "1",
    var unitPrice: String = ""
) {
    val total: Double
        get() {
            val q = quantity.toDoubleOrNull() ?: 0.0
            val p = unitPrice.toDoubleOrNull() ?: 0.0
            return q * p
        }
}

@Composable
fun RisitiSafeScreen(
    viewModel: MsaadaViewModel,
    isSwahili: Boolean,
    onBack: () -> Unit,
    onOpenPro: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val isProUser by viewModel.isProUser.collectAsState()
    val savedReceipts by viewModel.receipts.collectAsState()
    val receiptCount by viewModel.receiptCount.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Tengeneza, 1: Kumbukumbu
    var showProGate by remember { mutableStateOf(false) }
    var proGateFeature by remember { mutableStateOf("") }
    var showPreviewDialog by remember { mutableStateOf(false) }

    // Receipt form state
    val defaultDate = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()) }
    val autoReceiptNo = remember { "RST-${System.currentTimeMillis() % 100000}" }

    var businessName by remember { mutableStateOf("Duka Langu") }
    var businessPhone by remember { mutableStateOf("") }
    var businessAddress by remember { mutableStateOf("Dar es Salaam, Tanzania") }
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var receiptNumber by remember { mutableStateOf(autoReceiptNo) }
    var receiptDate by remember { mutableStateOf(defaultDate) }
    var paymentMethod by remember { mutableStateOf("Pesa Taslimu") }
    var amountPaidText by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("Asante kwa kufanya biashara nasi!") }
    var selectedTemplate by remember { mutableStateOf("modern") } // modern, classic, compact

    val items = remember {
        mutableStateListOf(
            ReceiptLineItem("Sukari (kg)", "2", "3200"),
            ReceiptLineItem("Mchele (kg)", "5", "3000")
        )
    }

    val subtotal = items.sumOf { it.total }
    val parsedAmountPaid = amountPaidText.toDoubleOrNull()
    val amountPaid = if (amountPaidText.isBlank()) subtotal else (parsedAmountPaid ?: 0.0)
    val balance = amountPaid - subtotal

    fun formatTsh(amount: Double): String {
        return "TSh " + NumberFormat.getNumberInstance(Locale.US).format(amount.toLong())
    }

    fun buildReceiptText(): String {
        val sb = StringBuilder()
        sb.append("================================\n")
        sb.append("       ${businessName.uppercase()}\n")
        if (businessPhone.isNotBlank()) sb.append("       Simu: $businessPhone\n")
        if (businessAddress.isNotBlank()) sb.append("       Anwani: $businessAddress\n")
        sb.append("================================\n")
        sb.append("RISITI YA MALIPO: #$receiptNumber\n")
        sb.append("Tarehe: $receiptDate\n")
        if (customerName.isNotBlank()) sb.append("Mteja: $customerName\n")
        if (customerPhone.isNotBlank()) sb.append("Simu ya Mteja: $customerPhone\n")
        sb.append("--------------------------------\n")
        sb.append(String.format("%-16s %4s %9s\n", "Bidhaa", "Idd", "Jumla"))
        sb.append("--------------------------------\n")
        items.forEach { item ->
            val nameTrunc = if (item.name.length > 15) item.name.take(15) else item.name
            sb.append(String.format("%-16s %4s %9s\n", nameTrunc, item.quantity, formatTsh(item.total)))
        }
        sb.append("--------------------------------\n")
        sb.append("JUMLA KUU:      ${formatTsh(subtotal)}\n")
        sb.append("KILICHOLIPWA:   ${formatTsh(amountPaid)}\n")
        if (balance >= 0) {
            sb.append("CHENJI:         ${formatTsh(balance)}\n")
        } else {
            sb.append("DENI LILILOBAKI:${formatTsh(-balance)}\n")
        }
        sb.append("Njia ya Malipo: $paymentMethod\n")
        if (notes.isNotBlank()) sb.append("Maelezo: $notes\n")
        sb.append("================================\n")
        if (!isProUser) {
            sb.append("Imeundwa na MSAADA App\n")
        }
        return sb.toString()
    }

    fun generatePdf(): File {
        val pdfDoc = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDoc.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint().apply { isAntiAlias = true }

        // Header Background
        paint.color = AndroidColor.parseColor(if (selectedTemplate == "classic") "#141416" else "#001848")
        canvas.drawRect(0f, 0f, 595f, 90f, paint)

        // Header Text
        paint.color = AndroidColor.WHITE
        paint.textSize = 22f
        paint.isFakeBoldText = true
        canvas.drawText(businessName, 36f, 48f, paint)

        paint.textSize = 11f
        paint.isFakeBoldText = false
        val contactLine = listOfNotNull(
            businessPhone.takeIf { it.isNotBlank() }?.let { "Simu: $it" },
            businessAddress.takeIf { it.isNotBlank() }
        ).joinToString(" | ")
        canvas.drawText(if (contactLine.isNotBlank()) contactLine else "Risiti ya Kidijitali", 36f, 70f, paint)

        // Meta info right
        paint.color = AndroidColor.BLACK
        paint.textSize = 12f
        paint.isFakeBoldText = true
        canvas.drawText("RISITI: #$receiptNumber", 380f, 130f, paint)
        paint.isFakeBoldText = false
        paint.textSize = 10f
        canvas.drawText("Tarehe: $receiptDate", 380f, 148f, paint)
        if (customerName.isNotBlank()) {
            canvas.drawText("Mteja: $customerName", 380f, 164f, paint)
        }

        // Divider
        paint.color = AndroidColor.LTGRAY
        canvas.drawLine(36f, 185f, 559f, 185f, paint)

        // Table Header
        var y = 210f
        paint.color = AndroidColor.parseColor("#008C95")
        paint.isFakeBoldText = true
        paint.textSize = 11f
        canvas.drawText("Bidhaa / Huduma", 36f, y, paint)
        canvas.drawText("Idadi", 330f, y, paint)
        canvas.drawText("Bei (TSh)", 400f, y, paint)
        canvas.drawText("Jumla (TSh)", 480f, y, paint)

        paint.color = AndroidColor.LTGRAY
        canvas.drawLine(36f, y + 6f, 559f, y + 6f, paint)

        // Rows
        paint.color = AndroidColor.BLACK
        paint.isFakeBoldText = false
        y += 24f

        items.forEach { item ->
            canvas.drawText(item.name.take(34), 36f, y, paint)
            canvas.drawText(item.quantity, 330f, y, paint)
            canvas.drawText(item.unitPrice, 400f, y, paint)
            canvas.drawText(item.total.toLong().toString(), 480f, y, paint)
            y += 20f
        }

        // Totals divider
        y += 10f
        paint.color = AndroidColor.LTGRAY
        canvas.drawLine(36f, y, 559f, y, paint)
        y += 26f

        paint.color = AndroidColor.BLACK
        paint.isFakeBoldText = true
        paint.textSize = 12f
        canvas.drawText("JUMLA KUU:", 380f, y, paint)
        canvas.drawText(formatTsh(subtotal), 480f, y, paint)
        y += 20f

        paint.isFakeBoldText = false
        paint.textSize = 10f
        canvas.drawText("Kiasi Kilicholipwa:", 380f, y, paint)
        canvas.drawText(formatTsh(amountPaid), 480f, y, paint)
        y += 18f

        if (balance >= 0) {
            canvas.drawText("Chenji:", 380f, y, paint)
            canvas.drawText(formatTsh(balance), 480f, y, paint)
        } else {
            paint.color = AndroidColor.RED
            canvas.drawText("Deni Lililobaki:", 380f, y, paint)
            canvas.drawText(formatTsh(-balance), 480f, y, paint)
            paint.color = AndroidColor.BLACK
        }
        y += 24f

        // Payment method and notes
        paint.textSize = 10f
        paint.isFakeBoldText = true
        canvas.drawText("Njia ya Malipo: $paymentMethod", 36f, y, paint)
        if (notes.isNotBlank()) {
            paint.isFakeBoldText = false
            canvas.drawText("Maelezo: $notes", 36f, y + 16f, paint)
        }

        // Watermark if free user
        if (!isProUser) {
            paint.color = AndroidColor.parseColor("#CCCCCC")
            paint.textSize = 28f
            paint.isFakeBoldText = true
            canvas.drawText("MSAADA - RISITI YA BURE", 130f, 520f, paint)
        }

        pdfDoc.finishPage(page)
        val file = File(context.cacheDir, "RisitiSafe_${receiptNumber}.pdf")
        FileOutputStream(file).use { out ->
            pdfDoc.writeTo(out)
        }
        pdfDoc.close()
        return file
    }

    Scaffold(
        topBar = {
            MsaadaTopBar(
                title = if (isSwahili) "RisitiSafe" else "RisitiSafe",
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
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (isSwahili) "Tengeneza Risiti" else "Create Receipt",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isSwahili) "Kumbukumbu (${savedReceipts.size})" else "History (${savedReceipts.size})",
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                )
            }

            if (selectedTab == 0) {
                // Form Tab
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    // Free quota banner
                    item {
                        if (!isProUser) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = if (isSwahili) "Risiti za Bure" else "Free Receipts",
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = "$receiptCount / 5 zilizohifadhiwa",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = if (receiptCount >= 5) Color(0xFFEF4444) else MsaadaTeal
                                            )
                                        )
                                    }
                                    OutlinedButton(
                                        onClick = onOpenPro,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Boresha PRO", fontSize = 11.sp, color = MsaadaNavy)
                                    }
                                }
                            }
                        }
                    }

                    // Business Info Card
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (isSwahili) "Taarifa za Biashara Yako" else "Your Business Details",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = businessName,
                                    onValueChange = { businessName = it },
                                    label = { Text(if (isSwahili) "Jina la Biashara *" else "Business Name *") },
                                    modifier = Modifier.fillMaxWidth().testTag("receipt_business_name"),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = businessPhone,
                                        onValueChange = { businessPhone = it },
                                        label = { Text(if (isSwahili) "Simu" else "Phone") },
                                        modifier = Modifier.weight(1f).testTag("receipt_business_phone"),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                        singleLine = true
                                    )
                                    OutlinedTextField(
                                        value = businessAddress,
                                        onValueChange = { businessAddress = it },
                                        label = { Text(if (isSwahili) "Anwani/Mahali" else "Address") },
                                        modifier = Modifier.weight(1f).testTag("receipt_business_address"),
                                        singleLine = true
                                    )
                                }
                            }
                        }
                    }

                    // Customer & Receipt Meta Card
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (isSwahili) "Taarifa za Mteja na Risiti" else "Customer & Receipt Info",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = receiptNumber,
                                        onValueChange = { receiptNumber = it },
                                        label = { Text(if (isSwahili) "Namba ya Risiti" else "Receipt #") },
                                        modifier = Modifier.weight(1f).testTag("receipt_number_input"),
                                        singleLine = true
                                    )
                                    OutlinedTextField(
                                        value = receiptDate,
                                        onValueChange = { receiptDate = it },
                                        label = { Text(if (isSwahili) "Tarehe" else "Date") },
                                        modifier = Modifier.weight(1f).testTag("receipt_date_input"),
                                        singleLine = true
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = customerName,
                                        onValueChange = { customerName = it },
                                        label = { Text(if (isSwahili) "Jina la Mteja" else "Customer Name") },
                                        modifier = Modifier.weight(1f).testTag("receipt_customer_name"),
                                        singleLine = true
                                    )
                                    OutlinedTextField(
                                        value = customerPhone,
                                        onValueChange = { customerPhone = it },
                                        label = { Text(if (isSwahili) "Simu ya Mteja" else "Customer Phone") },
                                        modifier = Modifier.weight(1f).testTag("receipt_customer_phone"),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                        singleLine = true
                                    )
                                }
                            }
                        }
                    }

                    // Items Section
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isSwahili) "Orodha ya Bidhaa / Huduma" else "Items / Services",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                    Button(
                                        onClick = { items.add(ReceiptLineItem("", "1", "")) },
                                        colors = ButtonDefaults.buttonColors(containerColor = MsaadaTeal),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.testTag("receipt_add_item_btn")
                                    ) {
                                        Icon(MsaadaIcons.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(if (isSwahili) "Weka Bidhaa" else "Add Item", fontSize = 12.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                items.forEachIndexed { index, item ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        OutlinedTextField(
                                            value = item.name,
                                            onValueChange = {
                                                items[index] = item.copy(name = it)
                                            },
                                            label = { Text(if (isSwahili) "Bidhaa" else "Item") },
                                            modifier = Modifier.weight(2f),
                                            singleLine = true
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        OutlinedTextField(
                                            value = item.quantity,
                                            onValueChange = {
                                                items[index] = item.copy(quantity = it)
                                            },
                                            label = { Text("Idd") },
                                            modifier = Modifier.weight(1f),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            singleLine = true
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        OutlinedTextField(
                                            value = item.unitPrice,
                                            onValueChange = {
                                                items[index] = item.copy(unitPrice = it)
                                            },
                                            label = { Text("Bei") },
                                            modifier = Modifier.weight(1.3f),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            singleLine = true
                                        )
                                        if (items.size > 1) {
                                            IconButton(
                                                onClick = { items.removeAt(index) },
                                                modifier = Modifier.size(36.dp)
                                            ) {
                                                Icon(
                                                    MsaadaIcons.Delete,
                                                    contentDescription = "Futa",
                                                    tint = Color(0xFFEF4444)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "Jumla: ${formatTsh(item.total)}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                                    )
                                    if (index < items.size - 1) {
                                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                                    }
                                }
                            }
                        }
                    }

                    // Payment details Card
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (isSwahili) "Malipo na Jumla" else "Payment & Totals",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                // Totals summary
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = if (isSwahili) "Jumla Kuu:" else "Grand Total:",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = formatTsh(subtotal),
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = amountPaidText,
                                    onValueChange = { amountPaidText = it },
                                    label = { Text(if (isSwahili) "Kiasi Kilicholipwa (TSh)" else "Amount Paid") },
                                    placeholder = { Text(subtotal.toLong().toString()) },
                                    modifier = Modifier.fillMaxWidth().testTag("receipt_amount_paid"),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = if (balance >= 0) "Chenji:" else "Deni Lililobaki:",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = if (balance >= 0) MaterialTheme.colorScheme.onSurface else Color(0xFFEF4444)
                                        )
                                    )
                                    Text(
                                        text = formatTsh(if (balance >= 0) balance else -balance),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (balance >= 0) MsaadaTeal else Color(0xFFEF4444)
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = if (isSwahili) "Njia ya Malipo:" else "Payment Method:",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                                )

                                val paymentMethods = listOf("Pesa Taslimu", "M-Pesa", "Tigo Pesa", "Airtel Money", "Benki", "Hundi")
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    paymentMethods.take(3).forEach { method ->
                                        FilterChip(
                                            selected = paymentMethod == method,
                                            onClick = { paymentMethod = method },
                                            label = { Text(method, fontSize = 11.sp) }
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    paymentMethods.drop(3).forEach { method ->
                                        FilterChip(
                                            selected = paymentMethod == method,
                                            onClick = { paymentMethod = method },
                                            label = { Text(method, fontSize = 11.sp) }
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = notes,
                                    onValueChange = { notes = it },
                                    label = { Text(if (isSwahili) "Maelezo / Shukrani" else "Notes / Thank You") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                            }
                        }
                    }

                    // PRO Templates selection
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (isSwahili) "Mtindo wa Risiti (PRO)" else "Receipt Template (PRO)",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    ProBadge()
                                }
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf("modern" to "Kisasa", "classic" to "Kawaida", "compact" to "Fupi").forEach { (key, label) ->
                                        val isSelected = selectedTemplate == key
                                        OutlinedButton(
                                            onClick = {
                                                if (isProUser) {
                                                    selectedTemplate = key
                                                } else {
                                                    proGateFeature = "Violezo vya Kisasa vya Risiti"
                                                    showProGate = true
                                                }
                                            },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                containerColor = if (isSelected) MsaadaTeal.copy(alpha = 0.12f) else Color.Transparent
                                            ),
                                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                                brush = androidx.compose.ui.graphics.SolidColor(
                                                    if (isSelected) MsaadaTeal else MaterialTheme.colorScheme.outline
                                                )
                                            )
                                        ) {
                                            Text(label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Actions Row
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showPreviewDialog = true },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f).testTag("receipt_preview_btn")
                            ) {
                                Icon(MsaadaIcons.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isSwahili) "Angalia" else "Preview")
                            }

                            Button(
                                onClick = {
                                    val hasInvalidItem = items.any { it.name.isBlank() || it.quantity.toDoubleOrNull()?.let { q -> q <= 0 } != false || it.unitPrice.toDoubleOrNull()?.let { p -> p < 0 } != false }
                                    if (businessName.isBlank() || receiptNumber.isBlank() || receiptDate.isBlank() || items.isEmpty() || hasInvalidItem || subtotal <= 0) {
                                        Toast.makeText(context, if (isSwahili) "Jaza taarifa sahihi za biashara, namba ya risiti na bidhaa." else "Enter valid business, receipt and item details.", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    if (amountPaidText.isNotBlank() && parsedAmountPaid == null) {
                                        Toast.makeText(context, if (isSwahili) "Weka kiasi kilicholipwa kwa namba sahihi." else "Enter a valid amount paid.", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    if (amountPaid < 0) {
                                        Toast.makeText(context, if (isSwahili) "Kiasi kilicholipwa hakiwezi kuwa hasi." else "Amount paid cannot be negative.", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    if (!isProUser && receiptCount >= 5) {
                                        proGateFeature = "Hifadhi Risiti Zaidi ya 5"
                                        showProGate = true
                                        return@Button
                                    }

                                    // Save receipt
                                    val itemsArr = JSONArray()
                                    items.forEach {
                                        val obj = JSONObject()
                                        obj.put("name", it.name)
                                        obj.put("qty", it.quantity)
                                        obj.put("price", it.unitPrice)
                                        obj.put("total", it.total)
                                        itemsArr.put(obj)
                                    }

                                    val receipt = RisitiSafeReceipt(
                                        receiptNumber = receiptNumber,
                                        businessName = businessName,
                                        businessPhone = businessPhone,
                                        businessAddress = businessAddress,
                                        customerName = customerName,
                                        customerPhone = customerPhone,
                                        date = receiptDate,
                                        itemsJson = itemsArr.toString(),
                                        subtotal = subtotal,
                                        total = subtotal,
                                        amountPaid = amountPaid,
                                        balance = balance,
                                        paymentMethod = paymentMethod,
                                        notes = notes,
                                        template = selectedTemplate,
                                        isPro = isProUser
                                    )

                                    viewModel.saveReceipt(receipt) {
                                        Toast.makeText(
                                            context,
                                            if (isSwahili) "Risiti imehifadhiwa kikamilifu!" else "Receipt saved successfully!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MsaadaNavy),
                                modifier = Modifier.weight(1f).testTag("receipt_save_btn")
                            ) {
                                Icon(MsaadaIcons.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isSwahili) "Hifadhi" else "Save")
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val text = buildReceiptText()
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, "Risiti #$receiptNumber")
                                        putExtra(Intent.EXTRA_TEXT, text)
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Shiriki Risiti"))
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MsaadaTeal),
                                modifier = Modifier.weight(1f).testTag("receipt_share_btn")
                            ) {
                                Icon(MsaadaIcons.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isSwahili) "Shiriki Risiti" else "Share Receipt")
                            }

                            Button(
                                onClick = {
                                    try {
                                        val pdfFile = generatePdf()
                                        val uri = FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.provider",
                                            pdfFile
                                        )
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            setDataAndType(uri, "application/pdf")
                                            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        }
                                        context.startActivity(Intent.createChooser(intent, "Fungua Risiti PDF"))
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Imeshindikana kufungua PDF: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MsaadaNavy),
                                modifier = Modifier.weight(1f).testTag("receipt_pdf_btn")
                            ) {
                                Icon(MsaadaIcons.Pdf, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Pakua PDF")
                            }
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            } else {
                // History Tab
                if (savedReceipts.isEmpty()) {
                    EmptyState(
                        icon = MsaadaIcons.ReceiptSafe,
                        title = if (isSwahili) "Hakuna Risiti Zilizohifadhiwa" else "No Saved Receipts",
                        description = if (isSwahili) "Risiti zote utakazohifadhi zitaonekana hapa kwa ajili ya kutazamwa na kushirikiwa." else "All saved receipts will appear here for viewing and sharing.",
                        actionButtonText = if (isSwahili) "Tengeneza Risiti Mpya" else "Create New Receipt",
                        onActionClick = { selectedTab = 0 }
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(savedReceipts) { _, receipt ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("saved_receipt_${receipt.id}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "Risiti #${receipt.receiptNumber}",
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Text(
                                                text = receipt.date,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            )
                                        }
                                        Text(
                                            text = formatTsh(receipt.total),
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Black,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    if (receipt.customerName.isNotBlank()) {
                                        Text(
                                            text = "Mteja: ${receipt.customerName}",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        )
                                    }

                                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            onClick = {
                                                // Load into form to duplicate / edit
                                                businessName = receipt.businessName
                                                businessPhone = receipt.businessPhone
                                                businessAddress = receipt.businessAddress
                                                customerName = receipt.customerName
                                                customerPhone = receipt.customerPhone
                                                receiptNumber = "RST-${System.currentTimeMillis() % 100000}"
                                                paymentMethod = receipt.paymentMethod
                                                notes = receipt.notes
                                                selectedTab = 0
                                                Toast.makeText(context, "Risiti imenakiliwa kwa marekebisho", Toast.LENGTH_SHORT).show()
                                            }
                                        ) {
                                            Icon(MsaadaIcons.Copy, contentDescription = "Nakili", tint = MsaadaTeal)
                                        }

                                        IconButton(
                                            onClick = {
                                                viewModel.deleteReceipt(receipt)
                                                Toast.makeText(context, "Risiti imefutwa", Toast.LENGTH_SHORT).show()
                                            }
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

    // Preview Dialog
    if (showPreviewDialog) {
        AlertDialog(
            onDismissRequest = { showPreviewDialog = false },
            title = {
                Text(
                    text = "Muonekano wa Risiti",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF9FAFB), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Text(
                        text = buildReceiptText(),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        color = Color(0xFF1F2937)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(buildReceiptText()))
                        Toast.makeText(context, "Imenakiliwa kwenye clipboard!", Toast.LENGTH_SHORT).show()
                        showPreviewDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MsaadaTeal)
                ) {
                    Icon(MsaadaIcons.Copy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Nakili Maandishi")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPreviewDialog = false }) {
                    Text("Funga")
                }
            }
        )
    }

    // Pro Feature Gate Dialog
    if (showProGate) {
        ProFeatureGateDialog(
            isSwahili = isSwahili,
            featureName = proGateFeature,
            onDismiss = { showProGate = false },
            onUpgrade = {
                showProGate = false
                onOpenPro()
            }
        )
    }
}
