package com.example.ui.screens.tools.home

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ElectricBolt
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.WorkspacePremium
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UmemeMeterReading
import com.example.ui.MsaadaViewModel
import com.example.ui.components.EmptyState
import com.example.ui.components.MsaadaTopBar
import com.example.ui.components.ProBadge
import com.example.ui.components.ProFeatureGateDialog
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaTeal
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ApplianceItem(
    val nameSw: String,
    val nameEn: String,
    val defaultWatts: Double,
    val defaultHours: Double,
    var isSelected: Boolean = false,
    var customHours: Double = defaultHours
)

@Composable
fun UmemeCalculatorScreen(
    viewModel: MsaadaViewModel,
    isSwahili: Boolean,
    onBack: () -> Unit,
    onOpenPro: () -> Unit
) {
    val context = LocalContext.current
    val isProUser by viewModel.isProUser.collectAsState()
    val savedReadings by viewModel.umemeReadings.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Mita, 1: Vifaa, 2: Kumbukumbu
    var showProGate by remember { mutableStateOf(false) }

    // Mode 1: Meter reading inputs
    var prevReadingText by remember { mutableStateOf("") }
    var currentReadingText by remember { mutableStateOf("") }
    var unitPriceText by remember { mutableStateOf("350") } // Average TSh per kWh
    var includeLevies by remember { mutableStateOf(false) } // Optional estimate; actual bill charges vary
    var readingNote by remember { mutableStateOf("") }

    val parsedPrevReading = prevReadingText.toDoubleOrNull()
    val parsedCurrentReading = currentReadingText.toDoubleOrNull()
    val parsedPricePerUnit = unitPriceText.toDoubleOrNull()
    val prevReading = parsedPrevReading ?: 0.0
    val currentReading = parsedCurrentReading ?: 0.0
    val pricePerUnit = parsedPricePerUnit ?: 0.0

    val unitsUsed = if (currentReading >= prevReading && currentReading > 0) currentReading - prevReading else 0.0
    val baseCost = unitsUsed * pricePerUnit
    val leviesPercent = if (includeLevies) 0.18 + 0.01 + 0.03 else 0.0 // VAT 18% + EWURA 1% + REA 3%
    val totalMeterCost = baseCost * (1 + leviesPercent)

    // Mode 2: Appliance Estimator
    val appliances = remember {
        mutableStateListOf(
            ApplianceItem("Friji Kubwa", "Large Refrigerator", 150.0, 24.0, isSelected = true),
            ApplianceItem("Taa za Nyumbani (x5)", "House Bulbs (x5)", 50.0, 6.0, isSelected = true),
            ApplianceItem("Runinga (TV)", "Television", 80.0, 5.0, isSelected = true),
            ApplianceItem("Feni ya Dari", "Ceiling Fan", 65.0, 8.0, isSelected = false),
            ApplianceItem("Pasi ya Umeme", "Electric Iron", 1000.0, 0.5, isSelected = true),
            ApplianceItem("Birika la Maji (Kettle)", "Electric Kettle", 1500.0, 0.4, isSelected = false),
            ApplianceItem("Kiyoyozi (AC)", "Air Conditioner", 1200.0, 4.0, isSelected = false),
            ApplianceItem("Mashine ya Kufulia", "Washing Machine", 500.0, 1.0, isSelected = false)
        )
    }

    // Daily appliance calculation: Watts * Hours / 1000 = kWh
    val dailyKwh = appliances.filter { it.isSelected }.sumOf { (it.defaultWatts * it.customHours) / 1000.0 }
    val monthlyKwh = dailyKwh * 30
    val monthlyApplianceCost = monthlyKwh * pricePerUnit

    fun formatTsh(amount: Double): String {
        return "TSh " + NumberFormat.getNumberInstance(Locale.US).format(amount.toLong())
    }

    Scaffold(
        topBar = {
            MsaadaTopBar(
                title = if (isSwahili) "Umeme Calculator" else "Electricity Calculator",
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
                            text = if (isSwahili) "Mita ya LUKU" else "Meter Reading",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = if (isSwahili) "Kadiria Vifaa" else "Appliances",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Text(
                            text = if (isSwahili) "Historia (${savedReadings.size})" else "History (${savedReadings.size})",
                            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }

            when (selectedTab) {
                0 -> {
                    // Mita ya LUKU Tab
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Result Summary Card
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("umeme_meter_result_card"),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = MsaadaNavy)
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = if (isSwahili) "Makadirio ya Gharama" else "Estimated Cost",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                color = Color.White.copy(alpha = 0.8f)
                                            )
                                        )
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .background(MsaadaTeal.copy(alpha = 0.3f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                Icons.Outlined.Bolt,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = formatTsh(totalMeterCost),
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.Black,
                                            color = Color.White
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                text = if (isSwahili) "Unit Zilizotumika" else "Units Used",
                                                style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f))
                                            )
                                            Text(
                                                text = String.format(Locale.US, "%.1f kWh", unitsUsed),
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            )
                                        }

                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = if (isSwahili) "Bei kwa Unit" else "Price per Unit",
                                                style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f))
                                            )
                                            Text(
                                                text = formatTsh(pricePerUnit),
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Inputs Card
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = if (isSwahili) "Usomaji wa Mita" else "Meter Readings",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedTextField(
                                            value = prevReadingText,
                                            onValueChange = { prevReadingText = it },
                                            label = { Text(if (isSwahili) "Usomaji Uliopita" else "Previous") },
                                            modifier = Modifier.weight(1f).testTag("umeme_prev_reading"),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            singleLine = true
                                        )
                                        OutlinedTextField(
                                            value = currentReadingText,
                                            onValueChange = { currentReadingText = it },
                                            label = { Text(if (isSwahili) "Usomaji wa Sasa" else "Current") },
                                            modifier = Modifier.weight(1f).testTag("umeme_curr_reading"),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            singleLine = true
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = if (isSwahili) "Tariff / Bei kwa Unit (TSh):" else "Tariff / Price per Unit:",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        listOf("292" to "D1 (Kijamii)", "350" to "T1 (Kawaida)", "420" to "Biashara").forEach { (price, label) ->
                                            FilterChip(
                                                selected = unitPriceText == price,
                                                onClick = { unitPriceText = price },
                                                label = { Text(label, fontSize = 11.sp) }
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = unitPriceText,
                                        onValueChange = { unitPriceText = it },
                                        label = { Text(if (isSwahili) "Weka Bei Maalum (TSh)" else "Custom Price (TSh)") },
                                        modifier = Modifier.fillMaxWidth().testTag("umeme_price_input"),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = if (isSwahili) "Jumlisha Kodi & Ushuru (VAT, EWURA, REA)" else "Include VAT & Levies",
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                            )
                                            Text(
                                                text = if (isSwahili) "Makadirio ya jumla tu; makato halisi hutegemea bili/tariff" else "Estimate only; actual charges depend on the tariff/bill",
                                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            )
                                        }
                                        Switch(
                                            checked = includeLevies,
                                            onCheckedChange = { includeLevies = it }
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = readingNote,
                                        onValueChange = { readingNote = it },
                                        label = { Text(if (isSwahili) "Maelezo (mf. Mwezi Julai)" else "Note (e.g. July Reading)") },
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true
                                    )
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
                                        if (prevReadingText.isBlank() || currentReadingText.isBlank() || unitPriceText.isBlank() ||
                                            parsedPrevReading == null || parsedCurrentReading == null || parsedPricePerUnit == null ||
                                            prevReading < 0 || currentReading <= 0 || pricePerUnit <= 0 || currentReading < prevReading
                                        ) {
                                            Toast.makeText(
                                                context,
                                                if (isSwahili) "Weka usomaji na bei sahihi. Usomaji wa sasa hauwezi kuwa chini ya uliopita."
                                                else "Enter valid readings and price. Current reading cannot be below the previous reading.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return@Button
                                        }

                                        val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                                        val reading = UmemeMeterReading(
                                            date = today,
                                            previousReading = prevReading,
                                            currentReading = currentReading,
                                            unitsUsed = unitsUsed,
                                            pricePerUnit = pricePerUnit,
                                            estimatedCost = totalMeterCost,
                                            notes = readingNote
                                        )

                                        viewModel.saveUmemeReading(reading)
                                        Toast.makeText(
                                            context,
                                            if (isSwahili) "Usomaji wa mita umehifadhiwa kikamilifu!" else "Meter reading saved!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MsaadaNavy),
                                    modifier = Modifier.weight(1f).testTag("umeme_save_btn")
                                ) {
                                    Icon(Icons.Outlined.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (isSwahili) "Hifadhi Usomaji" else "Save Reading")
                                }

                                OutlinedButton(
                                    onClick = {
                                        val shareText = "MSAADA UMEME CALCULATOR:\nUnit zilizotumika: %.1f kWh\nBei kwa Unit: %s\nGharama Makadirio: %s".format(
                                            unitsUsed,
                                            formatTsh(pricePerUnit),
                                            formatTsh(totalMeterCost)
                                        )
                                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            putExtra(Intent.EXTRA_TEXT, shareText)
                                        }
                                        context.startActivity(Intent.createChooser(sendIntent, "Shiriki Makadirio ya Umeme"))
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f).testTag("umeme_share_btn")
                                ) {
                                    Icon(Icons.Outlined.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (isSwahili) "Shiriki" else "Share")
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // Appliance Estimator Tab
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = MsaadaNavy)
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Text(
                                        text = if (isSwahili) "Matumizi ya Mwezi (Vifaa Vilivyochaguliwa)" else "Monthly Consumption",
                                        style = MaterialTheme.typography.titleSmall.copy(color = Color.White.copy(alpha = 0.8f))
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = formatTsh(monthlyApplianceCost),
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.Black,
                                            color = Color.White
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = String.format(Locale.US, "%.1f kWh / siku | %.1f kWh / mwezi", dailyKwh, monthlyKwh),
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.7f))
                                    )
                                }
                            }
                        }

                        item {
                            Text(
                                text = if (isSwahili) "Chagua Vifaa Unavyotumia Nyumbani:" else "Select Household Appliances:",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        itemsIndexed(appliances) { index, app ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        appliances[index] = app.copy(isSelected = !app.isSelected)
                                    },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (app.isSelected) MaterialTheme.colorScheme.surface else Color(0xFFF3F4F6)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(if (app.isSelected) MsaadaTeal else Color(0xFFD1D5DB)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (app.isSelected) {
                                                Icon(
                                                    Icons.Outlined.Check,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = if (isSwahili) app.nameSw else app.nameEn,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Text(
                                                text = "${app.defaultWatts.toInt()} Watts | ${app.customHours} hrs/day",
                                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            )
                                        }
                                    }

                                    val itemCostMonth = (app.defaultWatts * app.customHours / 1000.0) * 30 * pricePerUnit
                                    Text(
                                        text = formatTsh(itemCostMonth),
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (app.isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // History Tab
                    if (savedReadings.isEmpty()) {
                        EmptyState(
                            icon = Icons.Outlined.History,
                            title = if (isSwahili) "Hakuna Historia ya Mita" else "No Meter History",
                            description = if (isSwahili) "Usomaji wa mita utakao hifadhi utaonekana hapa kwa ajili ya kufuatilia matumizi yako ya umeme." else "Saved meter readings will appear here to monitor your power usage trends.",
                            actionButtonText = if (isSwahili) "Pima Usomaji Sasa" else "Record Reading Now",
                            onActionClick = { selectedTab = 0 }
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            itemsIndexed(savedReadings) { _, item ->
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
                                            Column {
                                                Text(
                                                    text = item.date,
                                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                                )
                                                if (item.notes.isNotBlank()) {
                                                    Text(
                                                        text = item.notes,
                                                        style = MaterialTheme.typography.bodySmall.copy(
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    )
                                                }
                                            }
                                            Text(
                                                text = formatTsh(item.estimatedCost),
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Black,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Unit: %.1f kWh (%s -> %s)".format(
                                                    item.unitsUsed,
                                                    item.previousReading.toLong().toString(),
                                                    item.currentReading.toLong().toString()
                                                ),
                                                style = MaterialTheme.typography.labelSmall.copy(color = MsaadaTeal)
                                            )
                                            IconButton(
                                                onClick = { viewModel.deleteUmemeReading(item) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(
                                                    Icons.Outlined.Delete,
                                                    contentDescription = "Futa",
                                                    tint = Color(0xFFEF4444),
                                                    modifier = Modifier.size(18.dp)
                                                )
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

    if (showProGate) {
        ProFeatureGateDialog(
            isSwahili = isSwahili,
            featureName = "Ripoti Kamili ya Nishati ya Umeme",
            onDismiss = { showProGate = false },
            onUpgrade = {
                showProGate = false
                onOpenPro()
            }
        )
    }
}
