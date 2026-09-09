package com.example.ui.screens.tools.agriculture

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MsaadaTopBar
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaTeal

// -------------------------------------------------------------
// 1. LAND AREA CALCULATOR (ENEO LA SHAMBA / KIWANJA)
// -------------------------------------------------------------
@Composable
fun LandAreaCalculatorScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var lengthMeters by remember { mutableStateOf("70") }
    var widthMeters by remember { mutableStateOf("70") }

    val l = lengthMeters.toDoubleOrNull() ?: 0.0
    val w = widthMeters.toDoubleOrNull() ?: 0.0

    val areaSqm = l * w
    val areaAcres = areaSqm / 4046.86 // 1 acre = 4,046.86 sqm
    val areaHectares = areaSqm / 10000.0 // 1 hectare = 10,000 sqm
    val areaSqft = areaSqm * 10.7639

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Kipima Eneo la Shamba & Kiwanja" else "Land Area Calculator",
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
                        text = if (isSwahili) "Vipimo vya Urefu na Upana" else "Plot Dimensions",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = lengthMeters,
                        onValueChange = { lengthMeters = it },
                        label = { Text(if (isSwahili) "Urefu (Mita)" else "Length (Meters)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = widthMeters,
                        onValueChange = { widthMeters = it },
                        label = { Text(if (isSwahili) "Upana (Mita)" else "Width (Meters)") },
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
                        text = if (isSwahili) "Ukubwa wa Eneo kwa Hekari" else "Area in Acres",
                        style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Text(
                        text = "%.3f ${if (isSwahili) "Hekari" else "Acres"}".format(areaAcres),
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Black,
                            color = MsaadaTeal
                        )
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = if (isSwahili) "Mita za Mraba (sqm):" else "Square Meters:")
                            Text(text = "%,.1f m²".format(areaSqm), fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = if (isSwahili) "Hekta (Hectares):" else "Hectares:")
                            Text(text = "%.4f ha".format(areaHectares), fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = if (isSwahili) "Futi za Mraba (sqft):" else "Square Feet:")
                            Text(text = "%,.0f ft²".format(areaSqft), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Quick Common Plots in TZ
            Text(
                text = if (isSwahili) "MIFANO YA VIWANJA VYA KAWAIDA (TANZANIA)" else "COMMON TANZANIAN PLOT SIZES",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            )

            listOf(
                "Kiwanja cha Mtaa (20m x 20m)" to "400 m² (~ ekari 0.1)",
                "Kiwanja cha Makazi (20m x 30m)" to "600 m² (~ ekari 0.15)",
                "Kiwanja Kikubwa (30m x 30m)" to "900 m² (~ ekari 0.22)",
                "Shamba Ekari 1 Kamili (70m x 70m)" to "4,900 m² (~ ekari 1.2)"
            ).forEach { (name, size) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                        Text(text = size, style = MaterialTheme.typography.bodyMedium.copy(color = MsaadaTeal, fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. SEED & HARVEST CALCULATOR (MBEGU & MAVUNO YA ZAO)
// -------------------------------------------------------------
data class CropInfo(
    val name: String,
    val seedPerAcreKg: Double,
    val spacing: String,
    val expectedYieldBags: String,
    val fertilizerDapKg: Int,
    val fertilizerUreaKg: Int
)

val tanzaniaCrops = listOf(
    CropInfo("Mahindi (Maize)", 10.0, "75 cm x 25 cm (mche 1)", "magunia 25 - 35", 50, 50),
    CropInfo("Mpunga (Rice)", 30.0, "20 cm x 20 cm", "magunia 30 - 45", 50, 50),
    CropInfo("Maharage (Beans)", 35.0, "50 cm x 10 cm", "magunia 8 - 14", 25, 0),
    CropInfo("Alizeti (Sunflower)", 4.0, "75 cm x 30 cm", "magunia 12 - 18", 25, 25),
    CropInfo("Nyanya (Tomatoes)", 0.2, "60 cm x 45 cm", "kreti 300 - 500", 50, 50),
    CropInfo("Vitunguu (Onions)", 2.0, "15 cm x 10 cm", "magunia 150 - 200", 50, 50)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeedPlantingCalculatorScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var selectedCropIndex by remember { mutableStateOf(0) }
    var acresInput by remember { mutableStateOf("2.0") }
    var cropExpanded by remember { mutableStateOf(false) }

    val acres = acresInput.toDoubleOrNull() ?: 1.0
    val crop = tanzaniaCrops[selectedCropIndex]

    val totalSeedsKg = crop.seedPerAcreKg * acres
    val totalDap = crop.fertilizerDapKg * acres
    val totalUrea = crop.fertilizerUreaKg * acres

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Kikokotoo cha Mbegu & Mavuno" else "Seed & Yield Calculator",
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
                        text = if (isSwahili) "Chagua Zao & Ukubwa wa Shamba" else "Select Crop & Farm Size",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    ExposedDropdownMenuBox(
                        expanded = cropExpanded,
                        onExpandedChange = { cropExpanded = !cropExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = crop.name,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(if (isSwahili) "Zao" else "Crop") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cropExpanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = cropExpanded,
                            onDismissRequest = { cropExpanded = false }
                        ) {
                            tanzaniaCrops.forEachIndexed { idx, c ->
                                DropdownMenuItem(
                                    text = { Text(c.name) },
                                    onClick = {
                                        selectedCropIndex = idx
                                        cropExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = acresInput,
                        onValueChange = { acresInput = it },
                        label = { Text(if (isSwahili) "Idadi ya Hekari (Acres)" else "Number of Acres") },
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
                        text = if (isSwahili) "Kiasi cha Mbegu Kinachohitajika" else "Seed Quantity Required",
                        style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Text(
                        text = "%.1f kg".format(totalSeedsKg),
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Black,
                            color = MsaadaTeal
                        )
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = if (isSwahili) "Nafasi ya Mashina:" else "Spacing:")
                            Text(text = crop.spacing, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = if (isSwahili) "Kadirio la Mavuno:" else "Expected Yield:")
                            Text(text = crop.expectedYieldBags, fontWeight = FontWeight.Bold, color = Color(0xFF059669))
                        }
                        if (totalDap > 0) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = if (isSwahili) "Mbolea ya Kupandia (DAP):" else "Basal Fertilizer (DAP):")
                                Text(text = "%.0f kg (mfuko %d)".format(totalDap, (totalDap / 50).toInt().coerceAtLeast(1)), fontWeight = FontWeight.Bold)
                            }
                        }
                        if (totalUrea > 0) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = if (isSwahili) "Mbolea ya Kukuzia (Urea):" else "Top Dressing (Urea):")
                                Text(text = "%.0f kg (mfuko %d)".format(totalUrea, (totalUrea / 50).toInt().coerceAtLeast(1)), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
