package com.example.ui.screens.tools.documents

import android.content.Context
import android.content.Intent
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableStateMapOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.data.model.SavedContract
import com.example.ui.MsaadaViewModel
import com.example.ui.components.EmptyState
import com.example.ui.components.MsaadaTopBar
import com.example.ui.components.ProBadge
import com.example.ui.components.ProFeatureGateDialog
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaTeal
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ContractType(
    val id: String,
    val titleSw: String,
    val titleEn: String,
    val descSw: String,
    val isPro: Boolean,
    val fields: List<ContractField>
)

data class ContractField(
    val key: String,
    val labelSw: String,
    val placeholder: String = ""
)

@Composable
fun MkatabaRahisiScreen(
    viewModel: MsaadaViewModel,
    isSwahili: Boolean,
    onBack: () -> Unit,
    onOpenPro: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val isProUser by viewModel.isProUser.collectAsState()
    val savedContracts by viewModel.savedContracts.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Unda Mkataba, 1: Kumbukumbu
    var showProGate by remember { mutableStateOf(false) }
    var proGateFeature by remember { mutableStateOf("") }
    var showPreviewDialog by remember { mutableStateOf(false) }

    val contractTypes = remember {
        listOf(
            ContractType(
                id = "pango",
                titleSw = "Mkataba wa Kupangisha Nyumba / Chumba",
                titleEn = "House / Room Tenancy Agreement",
                descSw = "Mkataba rasmi kati ya mwenye nyumba na mpangaji",
                isPro = false,
                fields = listOf(
                    ContractField("landlord_name", "Jina la Mwenye Nyumba (Mpangishaji)", "mf. John Mwamba"),
                    ContractField("landlord_phone", "Simu ya Mwenye Nyumba", "07XXXXXXXX"),
                    ContractField("tenant_name", "Jina la Mpangaji", "mf. Sarah Temu"),
                    ContractField("tenant_phone", "Simu ya Mpangaji", "07XXXXXXXX"),
                    ContractField("property_desc", "Maelezo ya Eneo (Chumba/Nyumba/Mtaa)", "mf. Chumba kimoja na sebule, Mtaa wa Sinza C"),
                    ContractField("rent_per_month", "Kodi kwa Mwezi (TSh)", "mf. 150,000"),
                    ContractField("months_paid", "Idadi ya Miezi Iliyolipwa", "mf. Miezi 6"),
                    ContractField("start_date", "Tarehe ya Kuanza Pango", "dd/mm/yyyy"),
                    ContractField("notice_period", "Muda wa Notisi ya Kuondoka", "mf. Mwezi 1")
                )
            ),
            ContractType(
                id = "mauziano",
                titleSw = "Mkataba wa Mauziano (Kiwanja / Chombo / Mali)",
                titleEn = "Sales & Purchase Agreement",
                descSw = "Uuzaji wa kiwanja, pikipiki, gari au bidhaa ya thamani",
                isPro = false,
                fields = listOf(
                    ContractField("seller_name", "Jina Kamili la Muuzaji", "mf. David Lyimo"),
                    ContractField("seller_nida", "NIDA ya Muuzaji", "19XXXXXXXXXXXXX"),
                    ContractField("buyer_name", "Jina Kamili la Mnunuzi", "mf. Grace Kimaro"),
                    ContractField("buyer_nida", "NIDA ya Mnunuzi", "19XXXXXXXXXXXXX"),
                    ContractField("item_desc", "Mali Inayouzwa na Namba za Utambulisho", "mf. Pikipiki Boxer MC 123 AB, Chassis No..."),
                    ContractField("price", "Bei ya Mauziano Iliyokubaliwa (TSh)", "mf. 2,500,000"),
                    ContractField("paid_amount", "Kiasi Kilicholipwa Leo (TSh)", "mf. 2,000,000"),
                    ContractField("balance_amount", "Kiasi Kilichobaki (TSh)", "mf. 500,000"),
                    ContractField("witness_1", "Jina la Shahidi wa Kwanza", "mf. Emmanuel Masawe"),
                    ContractField("witness_2", "Jina la Shahidi wa Pili", "mf. Mariam Juma")
                )
            ),
            ContractType(
                id = "mkopo",
                titleSw = "Mkataba wa Kukopesha Pesa Binafsi",
                titleEn = "Personal Loan Agreement",
                descSw = "Makubaliano ya kisheria ya kukopeshana pesa na marejesho",
                isPro = true,
                fields = listOf(
                    ContractField("lender_name", "Jina la Mkopeshaji", "mf. Ally Mwenda"),
                    ContractField("borrower_name", "Jina la Mkopaji", "mf. Rashid Khalfan"),
                    ContractField("borrower_nida", "NIDA ya Mkopaji", "19XXXXXXXXXXXXX"),
                    ContractField("amount", "Kiasi cha Mkopo (TSh)", "mf. 1,000,000"),
                    ContractField("interest", "Kiasi cha Faida / Riba (TSh)", "mf. 100,000"),
                    ContractField("due_date", "Tarehe ya Mwisho ya Kurudisha", "dd/mm/yyyy"),
                    ContractField("collateral", "Mali Inayowekwa Kama Dhamana", "mf. Runinga LG 55 inch na Hati ya Pikipiki")
                )
            ),
            ContractType(
                id = "kazi_huduma",
                titleSw = "Mkataba wa Kazi / Ufundi / Huduma",
                titleEn = "Service & Freelance Contract",
                descSw = "Makubaliano kati ya mteja na fundi/mtaalamu wa kazi",
                isPro = true,
                fields = listOf(
                    ContractField("client_name", "Jina la Mteja", "mf. Baraka Shirima"),
                    ContractField("contractor_name", "Jina la Mtoa Huduma / Fundi", "mf. Fundi Omary Athumani"),
                    ContractField("work_scope", "Maelezo ya Kazi Inayofanyika", "mf. Kupaka rangi nyumba nzima vyumba 4"),
                    ContractField("total_cost", "Jumla ya Gharama ya Kazi (TSh)", "mf. 800,000"),
                    ContractField("advance_paid", "Malipo ya Awali Yaliyotolewa (TSh)", "mf. 400,000"),
                    ContractField("completion_days", "Muda wa Kukamilisha Kazi", "mf. Siku 14")
                )
            )
        )
    }

    var selectedIndex by remember { mutableIntStateOf(0) }
    val currentType = contractTypes[selectedIndex]
    val fieldValues = remember { mutableStateMapOf<String, String>() }

    fun buildContractText(): String {
        val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        return when (currentType.id) {
            "pango" -> {
                val landlord = fieldValues["landlord_name"] ?: "........................"
                val landlordPhone = fieldValues["landlord_phone"] ?: "........................"
                val tenant = fieldValues["tenant_name"] ?: "........................"
                val tenantPhone = fieldValues["tenant_phone"] ?: "........................"
                val prop = fieldValues["property_desc"] ?: "........................"
                val rent = fieldValues["rent_per_month"] ?: "........................"
                val months = fieldValues["months_paid"] ?: "........................"
                val start = fieldValues["start_date"] ?: "........................"
                val notice = fieldValues["notice_period"] ?: "Mwezi 1"

                """
                MKATABA WA KUPANGISHA
                =======================================================
                Mkataba huu umefanyika leo tarehe $today,
                
                KATI YA:
                1. MPANGISHAJI (Mwenye Nyumba):
                   Jina: $landlord
                   Simu: $landlordPhone
                   (Hapa atatambulika kama "Mpangishaji")
                
                NA
                
                2. MPANGAJI:
                   Jina: $tenant
                   Simu: $tenantPhone
                   (Hapa atatambulika kama "Mpangaji")
                
                MASHARTI YA MKATABA:
                1. ENEO: Mpangishaji amekubali kumpa Mpangaji eneo la: $prop.
                2. KODI: Kodi iliyokubaliwa ni TSh $rent kwa kila mwezi.
                3. MALIPO: Mpangaji amelipa kodi ya $months kuanzia tarehe $start.
                4. MATUNZO: Mpangaji atalinda amani, usafi na mali iliyopo bila kufanya uharibifu.
                5. ILANI: Upande wowote unaotaka kusitisha mkataba utatoa notisi ya $notice mapema.
                
                SAINI ZA PANDE ZOTE MBILI:
                
                Saini ya Mpangishaji: ......................... Tarehe: $today
                
                Saini ya Mpangaji: ............................ Tarehe: $today
                
                SHAHIDI WA MPANGISHAJI: ....................... Simu: ....................
                SHAHIDI WA MPANGAJI: .......................... Simu: ....................
                =======================================================
                """.trimIndent()
            }
            "mauziano" -> {
                val seller = fieldValues["seller_name"] ?: "........................"
                val sellerNida = fieldValues["seller_nida"] ?: "........................"
                val buyer = fieldValues["buyer_name"] ?: "........................"
                val buyerNida = fieldValues["buyer_nida"] ?: "........................"
                val item = fieldValues["item_desc"] ?: "........................"
                val price = fieldValues["price"] ?: "........................"
                val paid = fieldValues["paid_amount"] ?: "........................"
                val bal = fieldValues["balance_amount"] ?: "0"
                val w1 = fieldValues["witness_1"] ?: "........................"
                val w2 = fieldValues["witness_2"] ?: "........................"

                """
                MKATABA WA MAUZIANO
                =======================================================
                Mkataba huu unafanyika leo tarehe $today,
                
                MUUZAJI:
                Jina: $seller
                NIDA: $sellerNida
                
                MNUNUZI:
                Jina: $buyer
                NIDA: $buyerNida
                
                MAELEZO YA MAUZIANO:
                Muuzaji ameuza kwa hiari yake bila kushurutishwa mali ifuatayo:
                $item
                
                BEI NA MALIPO:
                - Bei Iliyokubaliwa: TSh $price
                - Kiasi Kilicholipwa: TSh $paid
                - Kiasi Kilichobaki: TSh $bal
                
                TAMKO LA MUUZAJI:
                Muuza anathibitisha kuwa mali hii haina mgogoro, deni au kesi yoyote.
                
                SAINI YA MUUZAJI: ............................ Tarehe: $today
                SAINI YA MNUNUZI: ............................. Tarehe: $today
                
                MASHAHIDI:
                1. Shahidi wa Muuzaji: $w1 (Saini: ............)
                2. Shahidi wa Mnunuzi: $w2 (Saini: ............)
                =======================================================
                """.trimIndent()
            }
            "mkopo" -> {
                val lender = fieldValues["lender_name"] ?: "........................"
                val borrower = fieldValues["borrower_name"] ?: "........................"
                val borrowerNida = fieldValues["borrower_nida"] ?: "........................"
                val amt = fieldValues["amount"] ?: "........................"
                val interest = fieldValues["interest"] ?: "0"
                val due = fieldValues["due_date"] ?: "........................"
                val col = fieldValues["collateral"] ?: "........................"

                """
                MKATABA WA MKOPO BINAFSI
                =======================================================
                Tarehe: $today
                
                MKOPAJI: $borrower (NIDA: $borrowerNida)
                MKOPASHAJI: $lender
                
                MAKUBALIANO:
                1. Mkopaji amepokea mkopo wa kiasi cha TSh $amt.
                2. Riba / Faida iliyokubaliwa: TSh $interest.
                3. Tarehe ya Mwisho wa Marejesho: $due.
                4. Dhamana Iliyowekwa: $col.
                Ikiwa mkopaji atashindwa kurejesha kwa wakati, mkopeshaji ana haki ya kuuza dhamana kufidia deni.
                
                SAINI YA MKOPAJI: ............................ Tarehe: $today
                SAINI YA MKOPASHAJI: ......................... Tarehe: $today
                =======================================================
                """.trimIndent()
            }
            else -> {
                val client = fieldValues["client_name"] ?: "........................"
                val cont = fieldValues["contractor_name"] ?: "........................"
                val scope = fieldValues["work_scope"] ?: "........................"
                val total = fieldValues["total_cost"] ?: "........................"
                val adv = fieldValues["advance_paid"] ?: "........................"
                val days = fieldValues["completion_days"] ?: "........................"

                """
                MKATABA WA HUDUMA NA KAZI
                =======================================================
                Tarehe: $today
                
                MTEJA: $client
                MFUNDI / MTOA HUDUMA: $cont
                
                KAZI INAYOFANYIKA:
                $scope
                
                GHARAMA NA MAREJESHO:
                - Jumla ya Malipo ya Kazi: TSh $total
                - Malipo ya Awali Yaliyotolewa: TSh $adv
                - Muda wa Kukamilisha: $days
                
                SAINI YA MTEJA: .............................. Tarehe: $today
                SAINI YA MTOA HUDUMA: ........................ Tarehe: $today
                =======================================================
                """.trimIndent()
            }
        }
    }

    fun generatePdf(): File {
        val pdfDoc = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDoc.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint().apply { isAntiAlias = true }

        paint.color = AndroidColor.parseColor("#001848")
        canvas.drawRect(0f, 0f, 595f, 75f, paint)

        paint.color = AndroidColor.WHITE
        paint.textSize = 18f
        paint.isFakeBoldText = true
        canvas.drawText(currentType.titleSw, 40f, 45f, paint)

        val text = buildContractText()
        val lines = text.lines()

        paint.color = AndroidColor.BLACK
        paint.textSize = 10f
        paint.isFakeBoldText = false

        var y = 105f
        lines.forEach { line ->
            if (line.startsWith("MKATABA") || line.startsWith("KATI YA") || line.startsWith("MASHARTI") || line.startsWith("SAINI")) {
                paint.isFakeBoldText = true
            } else {
                paint.isFakeBoldText = false
            }
            canvas.drawText(line, 40f, y, paint)
            y += 16f
        }

        if (!isProUser) {
            paint.color = AndroidColor.parseColor("#D1D5DB")
            paint.textSize = 24f
            paint.isFakeBoldText = true
            canvas.drawText("MSAADA - MKATABA WA BURE", 130f, 650f, paint)
        }

        pdfDoc.finishPage(page)
        val file = File(context.cacheDir, "Mkataba_${currentType.id}.pdf")
        FileOutputStream(file).use { out ->
            pdfDoc.writeTo(out)
        }
        pdfDoc.close()
        return file
    }

    Scaffold(
        topBar = {
            MsaadaTopBar(
                title = if (isSwahili) "Mkataba Rahisi" else "Simple Contracts",
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
                            text = if (isSwahili) "Unda Mkataba" else "Create Contract",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = if (isSwahili) "Mikataba Yangu (${savedContracts.size})" else "My Contracts (${savedContracts.size})",
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
                    // Contract Types Selector
                    item {
                        Column {
                            Text(
                                text = if (isSwahili) "Chagua Aina ya Mkataba:" else "Select Contract Type:",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            contractTypes.forEachIndexed { index, item ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable {
                                            if (item.isPro && !isProUser) {
                                                proGateFeature = item.titleSw
                                                showProGate = true
                                            } else {
                                                selectedIndex = index
                                            }
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (selectedIndex == index)
                                            MsaadaNavy.copy(alpha = 0.08f)
                                        else
                                            MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = if (isSwahili) item.titleSw else item.titleEn,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Medium,
                                                        color = if (selectedIndex == index) MsaadaNavy else MaterialTheme.colorScheme.onSurface
                                                    )
                                                )
                                                if (item.isPro) {
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    ProBadge()
                                                }
                                            }
                                            Text(
                                                text = item.descSw,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            )
                                        }

                                        if (selectedIndex == index) {
                                            Box(
                                                modifier = Modifier
                                                    .size(22.dp)
                                                    .clip(CircleShape)
                                                    .background(MsaadaTeal),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    Icons.Outlined.Gavel,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Contract input fields
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (isSwahili) "Masharti na Wahusika wa Mkataba" else "Parties & Contract Terms",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                currentType.fields.forEach { field ->
                                    val v = fieldValues[field.key] ?: ""
                                    OutlinedTextField(
                                        value = v,
                                        onValueChange = { fieldValues[field.key] = it },
                                        label = { Text(field.labelSw) },
                                        placeholder = { if (field.placeholder.isNotBlank()) Text(field.placeholder) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .testTag("contract_${field.key}"),
                                        singleLine = true
                                    )
                                }
                            }
                        }
                    }

                    // Action buttons
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showPreviewDialog = true },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f).testTag("contract_preview_btn")
                            ) {
                                Icon(Icons.Outlined.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isSwahili) "Angalia" else "Preview")
                            }

                            Button(
                                onClick = {
                                    if (!isProUser) {
                                        showProGate = true
                                        return@Button
                                    }
                                    if (currentType.fields.any { fieldValues[it.key].isNullOrBlank() }) {
                                        Toast.makeText(context, if (isSwahili) "Jaza sehemu zote muhimu kabla ya kuhifadhi." else "Complete all required fields before saving.", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    val obj = JSONObject()
                                    fieldValues.forEach { (k, v) -> obj.put(k, v) }
                                    val text = buildContractText()

                                    val contract = SavedContract(
                                        contractType = currentType.id,
                                        title = currentType.titleSw,
                                        partyA = fieldValues.values.firstOrNull { it.isNotBlank() } ?: "Upande A",
                                        partyB = fieldValues.values.drop(2).firstOrNull { it.isNotBlank() } ?: "Upande B",
                                        fieldsJson = obj.toString(),
                                        fullText = text
                                    )

                                    viewModel.saveContract(contract)
                                    Toast.makeText(
                                        context,
                                        if (isSwahili) "Mkataba umehifadhiwa kikamilifu!" else "Contract saved!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MsaadaNavy),
                                modifier = Modifier.weight(1f).testTag("contract_save_btn")
                            ) {
                                Icon(Icons.Outlined.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isSwahili) "Hifadhi" else "Save")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val text = buildContractText()
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, currentType.titleSw)
                                        putExtra(Intent.EXTRA_TEXT, text)
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Shiriki Mkataba"))
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MsaadaTeal),
                                modifier = Modifier.weight(1f).testTag("contract_share_btn")
                            ) {
                                Icon(Icons.Outlined.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isSwahili) "Shiriki" else "Share")
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
                                        context.startActivity(Intent.createChooser(intent, "Fungua Mkataba PDF"))
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Kosa la kutengeneza PDF: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MsaadaNavy),
                                modifier = Modifier.weight(1f).testTag("contract_pdf_btn")
                            ) {
                                Icon(Icons.Outlined.PictureAsPdf, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Pakua PDF")
                            }
                        }
                    }
                }
            } else {
                // Saved Contracts
                if (savedContracts.isEmpty()) {
                    EmptyState(
                        icon = Icons.Outlined.Gavel,
                        title = if (isSwahili) "Hakuna Mikataba Iliyohifadhiwa" else "No Saved Contracts",
                        description = if (isSwahili) "Mikataba utakayotengeneza na kuhifadhi itaonekana hapa." else "Contracts you create and save will be stored here.",
                        actionButtonText = if (isSwahili) "Unda Mkataba Mpya" else "Create New Contract",
                        onActionClick = { selectedTab = 0 }
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(savedContracts) { _, contract ->
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
                                                text = contract.title,
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Text(
                                                text = "${contract.partyA} & ${contract.partyB}",
                                                style = MaterialTheme.typography.bodySmall.copy(color = MsaadaTeal)
                                            )
                                        }

                                        IconButton(
                                            onClick = { viewModel.deleteContract(contract) }
                                        ) {
                                            Icon(Icons.Outlined.Delete, contentDescription = "Futa", tint = Color(0xFFEF4444))
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

    if (showPreviewDialog) {
        AlertDialog(
            onDismissRequest = { showPreviewDialog = false },
            title = { Text(currentType.titleSw, fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF9FAFB), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = buildContractText(),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        lineHeight = 14.sp,
                        color = Color(0xFF1F2937)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(buildContractText()))
                        Toast.makeText(context, "Mkataba umenakiliwa!", Toast.LENGTH_SHORT).show()
                        showPreviewDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MsaadaTeal)
                ) {
                    Icon(Icons.Outlined.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Nakili Mkataba")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPreviewDialog = false }) {
                    Text("Funga")
                }
            }
        )
    }

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
