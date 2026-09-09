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
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.AlertDialog
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
import com.example.data.model.SavedFormProfile
import com.example.ui.MsaadaViewModel
import com.example.ui.components.EmptyState
import com.example.ui.components.MsaadaTopBar
import com.example.ui.components.ProBadge
import com.example.ui.components.ProFeatureGateDialog
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaProGold
import com.example.ui.theme.MsaadaTeal
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class FormTemplate(
    val id: String,
    val titleSw: String,
    val titleEn: String,
    val descSw: String,
    val isPro: Boolean,
    val fields: List<FormField>
)

data class FormField(
    val key: String,
    val labelSw: String,
    val labelEn: String,
    val placeholder: String = "",
    val isMultiline: Boolean = false
)

@Composable
fun MsaidiziWaFomuScreen(
    viewModel: MsaadaViewModel,
    isSwahili: Boolean,
    onBack: () -> Unit,
    onOpenPro: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val isProUser by viewModel.isProUser.collectAsState()
    val savedForms by viewModel.savedForms.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Jaza Fomu, 1: Kumbukumbu
    var showProGate by remember { mutableStateOf(false) }
    var proGateFeature by remember { mutableStateOf("") }
    var showPreviewDialog by remember { mutableStateOf(false) }

    val templates = remember {
        listOf(
            FormTemplate(
                id = "barua_kazi",
                titleSw = "Barua ya Maombi ya Kazi",
                titleEn = "Job Application Letter",
                descSw = "Muundo rasmi wa kuomba kazi kiserikali au sekta binafsi",
                isPro = false,
                fields = listOf(
                    FormField("applicant_name", "Jina Lako Kamili", "Your Full Name", "mf. Juma Hamisi Bakari"),
                    FormField("applicant_address", "Anwani Yako ya Posta / Makazi", "Your Address", "mf. S.L.P 1234, Kinondoni, Dar es Salaam"),
                    FormField("applicant_phone", "Namba ya Simu", "Phone Number", "07XX XXX XXX"),
                    FormField("recipient_title", "Cheo cha Mpokeaji", "Recipient Title", "mf. Mkurugenzi Mtendaji"),
                    FormField("company_name", "Jina la Taasisi / Kampuni", "Company/Org Name", "mf. Shirika la Posta Tanzania"),
                    FormField("company_address", "Anwani ya Kampuni", "Company Address", "mf. S.L.P 9000, Dar es Salaam"),
                    FormField("job_title", "Nafasi Unayoomba", "Job Position", "mf. Afisa Mauzo na Masoko"),
                    FormField("education", "Kiwango cha Elimu & Uzoefu", "Education & Experience", "mf. Shahada ya Biashara (UDSM) na uzoefu wa miaka 3...", isMultiline = true)
                )
            ),
            FormTemplate(
                id = "wasifu_binafsi",
                titleSw = "Wasifu & Taarifa Binafsi",
                titleEn = "Personal Profile & Bio",
                descSw = "Taarifa muhimu za utambulisho na mtu wa karibu",
                isPro = false,
                fields = listOf(
                    FormField("full_name", "Jina Kamili", "Full Name", "mf. Neema Joseph Masanja"),
                    FormField("nida_number", "Namba ya NIDA", "NIDA NIN Number", "19XXXXXXXXXXXXX"),
                    FormField("dob", "Tarehe ya Kuzaliwa", "Date of Birth", "dd/mm/yyyy"),
                    FormField("residence", "Mtaa na Wilaya ya Makazi", "Street & District", "mf. Mbezi Luis, Ubungo"),
                    FormField("phone", "Namba ya Simu", "Phone Number", "07XXXXXXXX"),
                    FormField("next_of_kin", "Mtu wa Karibu (Next of Kin)", "Next of Kin Name", "mf. Joseph Masanja (Baba)"),
                    FormField("next_of_kin_phone", "Simu ya Mtu wa Karibu", "Next of Kin Phone", "07XXXXXXXX")
                )
            ),
            FormTemplate(
                id = "barua_serikali_mtaa",
                titleSw = "Barua ya Utambulisho wa Makazi (Mtaa)",
                titleEn = "Local Gov Residence Introduction",
                descSw = "Barua ya kuomba utambulisho kwa Mwenyekiti wa Serikali ya Mtaa",
                isPro = true,
                fields = listOf(
                    FormField("name", "Jina Kamili la Mwombaji", "Full Name"),
                    FormField("street", "Jina la Mtaa / Kijiji", "Street / Village", "mf. Mtaa wa Amani"),
                    FormField("ward", "Kata na Wilaya", "Ward & District", "mf. Kata ya Makumbusho, Kinondoni"),
                    FormField("years_lived", "Muda wa Kuishi Hapo", "Years Lived", "mf. Miaka 4"),
                    FormField("purpose", "Sababu ya Utambulisho", "Purpose of Letter", "mf. Kufungua Akaunti ya Benki / Ajira / Leseni")
                )
            ),
            FormTemplate(
                id = "leseni_biashara",
                titleSw = "Maombi ya Leseni ya Biashara",
                titleEn = "Business Permit Application",
                descSw = "Fomu ya kuomba usajili na leseni kwa Manispaa/Halmashauri",
                isPro = true,
                fields = listOf(
                    FormField("owner_name", "Jina la Mmiliki", "Owner Name"),
                    FormField("business_name", "Jina la Biashara", "Business Name"),
                    FormField("tin_number", "Namba ya TIN (TRA)", "TIN Number", "9 digits"),
                    FormField("business_type", "Aina ya Biashara", "Type of Business", "mf. Duka la Rejareja / Karakana"),
                    FormField("location", "Mahali Biashara Ilipo", "Business Location", "Plot No. / Mtaa / Wilaya"),
                    FormField("capital", "Kiwango cha Mtaji (TSh)", "Capital Amount", "mf. 5,000,000")
                )
            ),
            FormTemplate(
                id = "maombi_mkopo_kikoba",
                titleSw = "Maombi ya Mkopo wa Kikundi",
                titleEn = "Group Loan Application Form",
                descSw = "Fomu rasmi ya kuomba mkopo kwenye Kikoba au Saccos",
                isPro = true,
                fields = listOf(
                    FormField("member_name", "Jina la Mwanachama", "Member Name"),
                    FormField("group_name", "Jina la Kikundi / Kikoba", "Group Name"),
                    FormField("loan_amount", "Kiasi cha Mkopo Kinachoombwa (TSh)", "Loan Amount"),
                    FormField("loan_purpose", "Kazi ya Mkopo", "Purpose of Loan", "mf. Kuongeza mtaji wa biashara ya nafaka"),
                    FormField("collateral", "Dhamana Inayowekwa", "Collateral", "mf. Pikipiki / Hisa za kikundi"),
                    FormField("repayment_months", "Muda wa Marejesho (Miezi)", "Repayment Duration", "mf. Miezi 6")
                )
            )
        )
    }

    var selectedTemplateIndex by remember { mutableIntStateOf(0) }
    val currentTemplate = templates[selectedTemplateIndex]
    val formValues = remember { mutableStateMapOf<String, String>() }

    fun buildFormText(): String {
        val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        return when (currentTemplate.id) {
            "barua_kazi" -> {
                val applicant = formValues["applicant_name"] ?: "Ndugu Mwombaji"
                val appAddress = formValues["applicant_address"] ?: "S.L.P XXX"
                val phone = formValues["applicant_phone"] ?: ""
                val recipient = formValues["recipient_title"] ?: "Mkurugenzi"
                val company = formValues["company_name"] ?: "Kampuni"
                val compAddress = formValues["company_address"] ?: ""
                val jobTitle = formValues["job_title"] ?: "Nafasi ya Kazi"
                val edu = formValues["education"] ?: ""

                """
                $appAddress
                Tarehe: $today
                
                $recipient,
                $company,
                $compAddress.
                
                Ndugu,
                
                YAH: MAOMBI YA NAFASI YA KAZI YA $jobTitle
                
                Husika na mada iliyotajwa hapo juu.
                
                Mimi $applicant, ninaandika barua hii kuomba kwa heshima nafasi ya kazi ya $jobTitle katika taasisi yenu.
                
                Kuhusu sifa zangu: $edu
                
                Nipo tayari kutoa mchango wangu mkubwa kwa bidii na uadilifu wa hali ya juu. Nimeambatanisha wasifu wangu (CV) na nakala za vyeti kwa mapitio yenu.
                
                Natumaini ombi langu litakubaliwa.
                
                Wako mtiifu,
                
                .......................
                $applicant
                Simu: $phone
                """.trimIndent()
            }
            "wasifu_binafsi" -> {
                val name = formValues["full_name"] ?: ""
                val nida = formValues["nida_number"] ?: ""
                val dob = formValues["dob"] ?: ""
                val res = formValues["residence"] ?: ""
                val phone = formValues["phone"] ?: ""
                val nok = formValues["next_of_kin"] ?: ""
                val nokPhone = formValues["next_of_kin_phone"] ?: ""

                """
                WASIFU WA TAARIFA BINAFSI (PERSONAL PROFILE)
                ================================================
                Jina Kamili: $name
                Namba ya NIDA: $nida
                Tarehe ya Kuzaliwa: $dob
                Makazi: $res
                Namba ya Simu: $phone
                
                TAARIFA ZA MTU WA KARIBU (NEXT OF KIN):
                Jina la Mtu wa Karibu: $nok
                Simu ya Mtu wa Karibu: $nokPhone
                
                Tarehe ya Kurekodiwa: $today
                ================================================
                """.trimIndent()
            }
            "barua_serikali_mtaa" -> {
                val name = formValues["name"] ?: ""
                val street = formValues["street"] ?: ""
                val ward = formValues["ward"] ?: ""
                val years = formValues["years_lived"] ?: ""
                val purpose = formValues["purpose"] ?: ""

                """
                OFISI YA SERIKALI YA MTAA WA $street,
                KATA YA $ward.
                
                Tarehe: $today
                
                KWA YEYOTE ANAYEHUSIKA,
                
                YAH: UTAMBULISHO WA MKAZI - $name
                
                Ofisi ya Serikali ya Mtaa inamtambulisha $name ambaye ni mkazi halali wa mtaa huu kwa muda wa $years.
                
                Utambulisho huu umetolewa kwa ajili ya: $purpose.
                
                Msaada wowote atakao patiwa utathaminiwa.
                
                Saini ya Mwenyekiti: .........................
                Muhuri wa Mtaa: 
                """.trimIndent()
            }
            "leseni_biashara" -> {
                val owner = formValues["owner_name"] ?: ""
                val bname = formValues["business_name"] ?: ""
                val tin = formValues["tin_number"] ?: ""
                val btype = formValues["business_type"] ?: ""
                val loc = formValues["location"] ?: ""
                val cap = formValues["capital"] ?: ""

                """
                FOMU YA MAOMBI YA LESENI YA BIASHARA
                ===========================================
                Mmiliki: $owner
                Jina la Biashara: $bname
                Namba ya TIN: $tin
                Aina ya Biashara: $btype
                Mahali Ilipo: $loc
                Kiwango cha Mtaji: TSh $cap
                Tarehe: $today
                ===========================================
                Saini ya Mwombaji: .........................
                """.trimIndent()
            }
            else -> {
                val member = formValues["member_name"] ?: ""
                val group = formValues["group_name"] ?: ""
                val amt = formValues["loan_amount"] ?: ""
                val purp = formValues["loan_purpose"] ?: ""
                val coll = formValues["collateral"] ?: ""
                val dur = formValues["repayment_months"] ?: ""

                """
                MAOMBI YA MKOPO - $group
                ===========================================
                Jina la Mwanachama: $member
                Kiasi cha Mkopo: TSh $amt
                Kazi ya Mkopo: $purp
                Dhamana: $coll
                Muda wa Marejesho: $dur miezi
                Tarehe: $today
                ===========================================
                Saini ya Mwanachama: .........................
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

        // Header band
        paint.color = AndroidColor.parseColor("#001848")
        canvas.drawRect(0f, 0f, 595f, 75f, paint)

        paint.color = AndroidColor.WHITE
        paint.textSize = 18f
        paint.isFakeBoldText = true
        canvas.drawText(currentTemplate.titleSw, 40f, 45f, paint)

        val text = buildFormText()
        val lines = text.lines()

        paint.color = AndroidColor.BLACK
        paint.textSize = 11f
        paint.isFakeBoldText = false

        var y = 110f
        lines.forEach { line ->
            if (line.startsWith("YAH:") || line.startsWith("WASIFU") || line.startsWith("OFISI")) {
                paint.isFakeBoldText = true
            } else {
                paint.isFakeBoldText = false
            }
            canvas.drawText(line, 40f, y, paint)
            y += 18f
        }

        if (!isProUser) {
            paint.color = AndroidColor.parseColor("#D1D5DB")
            paint.textSize = 24f
            paint.isFakeBoldText = true
            canvas.drawText("MSAADA - FOMU YA BURE", 140f, 650f, paint)
        }

        pdfDoc.finishPage(page)
        val file = File(context.cacheDir, "Fomu_${currentTemplate.id}.pdf")
        FileOutputStream(file).use { out ->
            pdfDoc.writeTo(out)
        }
        pdfDoc.close()
        return file
    }

    Scaffold(
        topBar = {
            MsaadaTopBar(
                title = if (isSwahili) "Msaidizi wa Fomu" else "Form Assistant",
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
                            text = if (isSwahili) "Jaza Fomu" else "Fill Form",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = if (isSwahili) "Zilizohifadhiwa (${savedForms.size})" else "Saved (${savedForms.size})",
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
                    // Template Selector Chips
                    item {
                        Column {
                            Text(
                                text = if (isSwahili) "Chagua Aina ya Fomu / Barua:" else "Select Form Template:",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            templates.forEachIndexed { index, tmpl ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable {
                                            if (tmpl.isPro && !isProUser) {
                                                proGateFeature = tmpl.titleSw
                                                showProGate = true
                                            } else {
                                                selectedTemplateIndex = index
                                            }
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (selectedTemplateIndex == index)
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
                                                    text = if (isSwahili) tmpl.titleSw else tmpl.titleEn,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = if (selectedTemplateIndex == index) FontWeight.Bold else FontWeight.Medium,
                                                        color = if (selectedTemplateIndex == index) MsaadaNavy else MaterialTheme.colorScheme.onSurface
                                                    )
                                                )
                                                if (tmpl.isPro) {
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    ProBadge()
                                                }
                                            }
                                            Text(
                                                text = tmpl.descSw,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            )
                                        }
                                        if (selectedTemplateIndex == index) {
                                            Box(
                                                modifier = Modifier
                                                    .size(22.dp)
                                                    .clip(CircleShape)
                                                    .background(MsaadaTeal),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    Icons.Outlined.Assignment,
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

                    // Form Fields Input Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (isSwahili) "Jaza Taarifa Zinazohitajika" else "Fill Details",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                currentTemplate.fields.forEach { field ->
                                    val value = formValues[field.key] ?: ""
                                    OutlinedTextField(
                                        value = value,
                                        onValueChange = { formValues[field.key] = it },
                                        label = { Text(if (isSwahili) field.labelSw else field.labelEn) },
                                        placeholder = { if (field.placeholder.isNotBlank()) Text(field.placeholder) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .testTag("form_field_${field.key}"),
                                        maxLines = if (field.isMultiline) 4 else 1,
                                        minLines = if (field.isMultiline) 3 else 1
                                    )
                                }
                            }
                        }
                    }

                    // Action Buttons
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showPreviewDialog = true },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f).testTag("form_preview_btn")
                            ) {
                                Icon(Icons.Outlined.Description, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isSwahili) "Tazama Barua" else "Preview")
                            }

                            Button(
                                onClick = {
                                    if (!isProUser) {
                                        showProGate = true
                                        return@Button
                                    }
                                    if (currentTemplate.fields.any { formValues[it.key].isNullOrBlank() }) {
                                        Toast.makeText(context, if (isSwahili) "Jaza sehemu zote muhimu kabla ya kuhifadhi." else "Complete all required fields before saving.", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    val obj = JSONObject()
                                    formValues.forEach { (k, v) -> obj.put(k, v) }

                                    val saved = SavedFormProfile(
                                        templateId = currentTemplate.id,
                                        templateTitle = currentTemplate.titleSw,
                                        category = "FOMU",
                                        profileName = formValues.values.firstOrNull { it.isNotBlank() } ?: currentTemplate.titleSw,
                                        fieldsJson = obj.toString()
                                    )

                                    viewModel.saveFormProfile(saved)
                                    Toast.makeText(
                                        context,
                                        if (isSwahili) "Taarifa za fomu zimehifadhiwa!" else "Form profile saved!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MsaadaNavy),
                                modifier = Modifier.weight(1f).testTag("form_save_btn")
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
                                    val text = buildFormText()
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, currentTemplate.titleSw)
                                        putExtra(Intent.EXTRA_TEXT, text)
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Shiriki Maandishi ya Fomu"))
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MsaadaTeal),
                                modifier = Modifier.weight(1f).testTag("form_share_btn")
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
                                        context.startActivity(Intent.createChooser(intent, "Fungua Fomu PDF"))
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Kosa la kutengeneza PDF: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MsaadaNavy),
                                modifier = Modifier.weight(1f).testTag("form_pdf_btn")
                            ) {
                                Icon(Icons.Outlined.PictureAsPdf, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Pakua PDF")
                            }
                        }
                    }
                }
            } else {
                // Saved Forms Tab
                if (savedForms.isEmpty()) {
                    EmptyState(
                        icon = Icons.Outlined.Assignment,
                        title = if (isSwahili) "Hakuna Fomu Zilizohifadhiwa" else "No Saved Forms",
                        description = if (isSwahili) "Fomu au barua utakazojaza na kuhifadhi zitaonekana hapa kwa matumizi ya baadaye." else "Forms and letters you fill and save will be stored here.",
                        actionButtonText = if (isSwahili) "Jaza Fomu Mpya" else "Fill New Form",
                        onActionClick = { selectedTab = 0 }
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(savedForms) { _, form ->
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
                                                text = form.templateTitle,
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Text(
                                                text = form.profileName,
                                                style = MaterialTheme.typography.bodySmall.copy(color = MsaadaTeal)
                                            )
                                        }

                                        Row {
                                            IconButton(
                                                onClick = {
                                                    try {
                                                        val json = JSONObject(form.fieldsJson)
                                                        formValues.clear()
                                                        json.keys().forEach { k ->
                                                            formValues[k] = json.getString(k)
                                                        }
                                                        val idx = templates.indexOfFirst { it.id == form.templateId }
                                                        if (idx != -1) selectedTemplateIndex = idx
                                                        selectedTab = 0
                                                        Toast.makeText(context, "Taarifa zimepakiwa kwenye fomu", Toast.LENGTH_SHORT).show()
                                                    } catch (e: Exception) {
                                                        // ignore
                                                    }
                                                }
                                            ) {
                                                Icon(Icons.Outlined.ContentCopy, contentDescription = "Pia", tint = MsaadaTeal)
                                            }

                                            IconButton(
                                                onClick = { viewModel.deleteFormProfile(form) }
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
    }

    if (showPreviewDialog) {
        AlertDialog(
            onDismissRequest = { showPreviewDialog = false },
            title = { Text(currentTemplate.titleSw, fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF9FAFB), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = buildFormText(),
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
                        clipboardManager.setText(AnnotatedString(buildFormText()))
                        Toast.makeText(context, "Nakala imehifadhiwa kwenye clipboard!", Toast.LENGTH_SHORT).show()
                        showPreviewDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MsaadaTeal)
                ) {
                    Icon(Icons.Outlined.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
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
