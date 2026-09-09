package com.example.ui.screens.tools.tech

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MsaadaTopBar
import com.example.ui.screens.tools.photo.saveAndShareBitmap
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaTeal
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

// QR Code Generator using ZXing
fun generateQrBitmap(content: String, size: Int = 512): Bitmap? {
    if (content.isBlank()) return null
    return try {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size)
        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bmp
    } catch (e: Exception) {
        null
    }
}

// -------------------------------------------------------------
// 1. QR CODE GENERATOR SCREEN
// -------------------------------------------------------------
@Composable
fun QrGeneratorScannerScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var selectedTypeIndex by remember { mutableIntStateOf(0) }
    var rawInputText by remember { mutableStateOf("Tigo Pesa Lipa Namba: 1234567") }
    var generatedQr by remember { mutableStateOf<Bitmap?>(generateQrBitmap(rawInputText)) }
    val context = LocalContext.current

    val types = listOf(
        if (isSwahili) "Lipa kwa Simu" else "Till / Paybill",
        if (isSwahili) "Tovuti / Link" else "Website URL",
        if (isSwahili) "Maandishi" else "Text Message",
        if (isSwahili) "Namba ya Simu" else "Phone Number"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Tengeneza QR Code" else "QR Code Generator",
            onBack = onBack
        )

        TabRow(
            selectedTabIndex = selectedTypeIndex,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            types.forEachIndexed { idx, label ->
                Tab(
                    selected = selectedTypeIndex == idx,
                    onClick = {
                        selectedTypeIndex = idx
                        rawInputText = when (idx) {
                            0 -> "LIPA NAMBA: 55443322 (Sinza Enterprises)"
                            1 -> "https://www.msaada.co.tz"
                            2 -> "Karibu kwenye huduma ya MSAADA"
                            else -> "tel:+255712345678"
                        }
                        generatedQr = generateQrBitmap(rawInputText)
                    },
                    text = { Text(text = label, fontSize = 12.sp) }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (isSwahili) "Taarifa za Kuweka Kwenye QR Code" else "QR Code Content",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = rawInputText,
                        onValueChange = {
                            rawInputText = it
                            generatedQr = generateQrBitmap(it)
                        },
                        label = { Text(types[selectedTypeIndex]) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 2
                    )
                }
            }

            // QR Code Display Card
            if (generatedQr != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            bitmap = generatedQr!!.asImageBitmap(),
                            contentDescription = "QR Code",
                            modifier = Modifier
                                .size(220.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = rawInputText,
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray),
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    saveAndShareBitmap(context, generatedQr!!, isShare = false)
                                    Toast.makeText(context, if (isSwahili) "QR Code imehifadhiwa!" else "QR Code saved!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MsaadaTeal)
                            ) {
                                Icon(imageVector = Icons.Outlined.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = if (isSwahili) "Hifadhi" else "Save")
                            }

                            OutlinedButton(
                                onClick = {
                                    saveAndShareBitmap(context, generatedQr!!, isShare = true)
                                },
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(imageVector = Icons.Outlined.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = if (isSwahili) "Shiriki" else "Share")
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. UNIT CONVERTER SCREEN
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitConverterScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var inputValue by remember { mutableStateOf("100") }

    val categories = listOf(
        if (isSwahili) "Urefu (Length)" else "Length",
        if (isSwahili) "Uzito (Weight)" else "Weight",
        if (isSwahili) "Joto (Temp)" else "Temperature"
    )

    val input = inputValue.toDoubleOrNull() ?: 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Kigeuzi cha Vipimo (Unit Converter)" else "Unit Converter",
            onBack = onBack
        )

        TabRow(
            selectedTabIndex = selectedCategoryIndex,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            categories.forEachIndexed { idx, label ->
                Tab(
                    selected = selectedCategoryIndex == idx,
                    onClick = { selectedCategoryIndex = idx },
                    text = { Text(text = label, fontSize = 12.sp) }
                )
            }
        }

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
                        text = if (isSwahili) "Ingiza Thamani ya Kubadili" else "Enter Value to Convert",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { inputValue = it },
                        label = {
                            Text(
                                when (selectedCategoryIndex) {
                                    0 -> if (isSwahili) "Mita (Meters)" else "Meters"
                                    1 -> if (isSwahili) "Kilogramu (kg)" else "Kilograms"
                                    else -> if (isSwahili) "Nyuzi Selsiasi (°C)" else "Celsius (°C)"
                                }
                            )
                        },
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
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    when (selectedCategoryIndex) {
                        0 -> {
                            // Length conversions from meters
                            ConversionRow("Kilomita (km)", "%.3f km".format(input / 1000.0))
                            ConversionRow("Sentimita (cm)", "%,.1f cm".format(input * 100.0))
                            ConversionRow("Futi (Feet)", "%,.2f ft".format(input * 3.28084))
                            ConversionRow("Inchi (Inches)", "%,.2f in".format(input * 39.3701))
                            ConversionRow("Maili (Miles)", "%.4f mi".format(input / 1609.34))
                        }
                        1 -> {
                            // Weight conversions from kg
                            ConversionRow("Gramu (g)", "%,.1f g".format(input * 1000.0))
                            ConversionRow("Pauni (Pounds / lbs)", "%,.2f lbs".format(input * 2.20462))
                            ConversionRow("Tani (Metric Tonnes)", "%.4f t".format(input / 1000.0))
                            ConversionRow("Aunsi (Ounces)", "%,.2f oz".format(input * 35.274))
                        }
                        else -> {
                            // Temperature conversions from Celsius
                            val fahrenheit = (input * 9.0 / 5.0) + 32.0
                            val kelvin = input + 273.15
                            ConversionRow("Fahrenheit (°F)", "%.1f °F".format(fahrenheit))
                            ConversionRow("Kelvin (K)", "%.2f K".format(kelvin))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConversionRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
        Text(text = value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MsaadaNavy))
    }
    HorizontalDivider(color = Color(0xFFF1F5F9))
}

// -------------------------------------------------------------
// 3. DATA USAGE & INTERNET BUNDLE ESTIMATOR
// -------------------------------------------------------------
@Composable
fun DataUsageCalculatorScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var videoHoursDaily by remember { mutableStateOf("1") }
    var socialHoursDaily by remember { mutableStateOf("2") }
    var browsingHoursDaily by remember { mutableStateOf("2") }

    val vid = videoHoursDaily.toDoubleOrNull() ?: 0.0
    val soc = socialHoursDaily.toDoubleOrNull() ?: 0.0
    val brw = browsingHoursDaily.toDoubleOrNull() ?: 0.0

    // Video streaming (e.g. YouTube/TikTok) ~ 0.8 GB/hr
    // Social media (Instagram, WhatsApp, X) ~ 0.2 GB/hr
    // Browsing/reading ~ 0.06 GB/hr
    val dailyGb = (vid * 0.8) + (soc * 0.2) + (brw * 0.06)
    val monthlyGb = dailyGb * 30.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Kikokotoo cha Vifurushi vya Intaneti" else "Data Bundle Estimator",
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
                        text = if (isSwahili) "Masaa ya Matumizi kwa Siku" else "Daily Hours of Usage",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = videoHoursDaily,
                        onValueChange = { videoHoursDaily = it },
                        label = { Text(if (isSwahili) "Video (YouTube, TikTok, Netflix - masaa)" else "Video Streaming (hours)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = socialHoursDaily,
                        onValueChange = { socialHoursDaily = it },
                        label = { Text(if (isSwahili) "Mitandao ya Kijamii (WhatsApp, Instagram - masaa)" else "Social Media (hours)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = browsingHoursDaily,
                        onValueChange = { browsingHoursDaily = it },
                        label = { Text(if (isSwahili) "Kusoma Habari & Tovuti (masaa)" else "Web Browsing (hours)") },
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
                        text = if (isSwahili) "Kadirio la Kifurushi cha Mwezi" else "Recommended Monthly Bundle",
                        style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Text(
                        text = "%.1f GB / mwezi".format(monthlyGb),
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
                            Text(text = if (isSwahili) "Matumizi kwa Siku:" else "Daily Usage:", style = MaterialTheme.typography.labelSmall)
                            Text(text = "%.2f GB".format(dailyGb), style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = if (isSwahili) "Kifurushi Kinachofaa:" else "Recommended Plan:", style = MaterialTheme.typography.labelSmall)
                            Text(
                                text = if (monthlyGb <= 15) "15 - 20 GB" else if (monthlyGb <= 35) "30 - 40 GB" else "50+ GB Unlimited",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = MsaadaTeal)
                            )
                        }
                    }
                }
            }
        }
    }
}
