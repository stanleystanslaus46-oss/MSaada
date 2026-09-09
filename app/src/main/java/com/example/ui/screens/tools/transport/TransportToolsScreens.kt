package com.example.ui.screens.tools.transport

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MsaadaTopBar
import com.example.ui.screens.tools.finance.formatTsh
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaTeal

val tanzaniaCities = listOf(
    "Dar es Salaam",
    "Dodoma",
    "Arusha",
    "Mwanza",
    "Mbeya",
    "Morogoro",
    "Tanga",
    "Moshi",
    "Iringa",
    "Kigoma",
    "Tabora",
    "Mtwara",
    "Zanzibar"
)

// Intercity distance map in km
fun getTzDistance(origin: String, destination: String): Int {
    if (origin == destination) return 0
    val key = listOf(origin, destination).sorted().joinToString("-")
    return when (key) {
        "Arusha-Dar es Salaam" -> 630
        "Dar es Salaam-Dodoma" -> 450
        "Dar es Salaam-Mwanza" -> 1150
        "Dar es Salaam-Mbeya" -> 830
        "Dar es Salaam-Morogoro" -> 190
        "Dar es Salaam-Tanga" -> 350
        "Dar es Salaam-Moshi" -> 550
        "Dar es Salaam-Iringa" -> 490
        "Dar es Salaam-Kigoma" -> 1450
        "Dar es Salaam-Tabora" -> 830
        "Dar es Salaam-Mtwara" -> 560
        "Arusha-Dodoma" -> 440
        "Arusha-Moshi" -> 80
        "Arusha-Mwanza" -> 590
        "Dodoma-Mwanza" -> 700
        "Dodoma-Mbeya" -> 450
        "Dodoma-Morogoro" -> 260
        "Dodoma-Iringa" -> 260
        "Mbeya-Iringa" -> 340
        "Mwanza-Tabora" -> 360
        else -> 480 // reasonable fallback estimate across regions
    }
}

// -------------------------------------------------------------
// 1. TANZANIA DISTANCE & TRAVEL CALCULATOR SCREEN
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistanceCalculatorScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var originCity by remember { mutableStateOf("Dar es Salaam") }
    var destinationCity by remember { mutableStateOf("Dodoma") }
    var originExpanded by remember { mutableStateOf(false) }
    var destExpanded by remember { mutableStateOf(false) }

    val distanceKm = getTzDistance(originCity, destinationCity)
    val busHours = if (distanceKm > 0) (distanceKm / 70.0) else 0.0
    val carHours = if (distanceKm > 0) (distanceKm / 85.0) else 0.0
    val estBusFare = distanceKm * 75.0 // ~75 TSh per km bus fare estimate
    val estFuelLitres = distanceKm / 11.0 // 11 km/L average car
    val estFuelCost = estFuelLitres * 3200.0 // 3,200 TSh per litre

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Umbali & Usafiri Tanzania" else "Tanzania Distance & Travel",
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
                        text = if (isSwahili) "Chagua Miji ya Safari" else "Select Travel Route",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Origin Dropdown
                    ExposedDropdownMenuBox(
                        expanded = originExpanded,
                        onExpandedChange = { originExpanded = !originExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = originCity,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(if (isSwahili) "Kuanzia (Origin)" else "From (Origin)") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = originExpanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = originExpanded,
                            onDismissRequest = { originExpanded = false }
                        ) {
                            tanzaniaCities.forEach { city ->
                                DropdownMenuItem(
                                    text = { Text(city) },
                                    onClick = {
                                        originCity = city
                                        originExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = {
                                val tmp = originCity
                                originCity = destinationCity
                                destinationCity = tmp
                            }
                        ) {
                            Icon(imageVector = Icons.Outlined.SwapVert, contentDescription = "Swap", tint = MsaadaTeal)
                        }
                    }

                    // Destination Dropdown
                    ExposedDropdownMenuBox(
                        expanded = destExpanded,
                        onExpandedChange = { destExpanded = !destExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = destinationCity,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(if (isSwahili) "Kuelekea (Destination)" else "To (Destination)") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = destExpanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = destExpanded,
                            onDismissRequest = { destExpanded = false }
                        ) {
                            tanzaniaCities.forEach { city ->
                                DropdownMenuItem(
                                    text = { Text(city) },
                                    onClick = {
                                        destinationCity = city
                                        destExpanded = false
                                    }
                                )
                            }
                        }
                    }
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
                        text = if (isSwahili) "Umbali wa Moja kwa Moja (Barabara)" else "Road Distance",
                        style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Text(
                        text = "$distanceKm km",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Black,
                            color = MsaadaNavy
                        )
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp))

                    // Travel Times
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Outlined.DirectionsBus, contentDescription = null, tint = MsaadaTeal)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = if (isSwahili) "Basi la Abiria" else "Public Bus", style = MaterialTheme.typography.labelSmall)
                                Text(text = "%.1f masaa".format(busHours), style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                Text(text = "Nauli: ~${formatTsh(estBusFare)}", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF059669)))
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Outlined.DirectionsCar, contentDescription = null, tint = MsaadaNavy)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = if (isSwahili) "Gari Binafsi" else "Private Car", style = MaterialTheme.typography.labelSmall)
                                Text(text = "%.1f masaa".format(carHours), style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                Text(text = "Mafuta: ~${formatTsh(estFuelCost)}", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFD97706)))
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. FUEL COST CALCULATOR SCREEN
// -------------------------------------------------------------
@Composable
fun FuelCostCalculatorScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var tripDistanceKm by remember { mutableStateOf("450") }
    var vehicleEfficiency by remember { mutableStateOf("10") } // km per litre
    var fuelPricePerLitre by remember { mutableStateOf("3250") }

    val dist = tripDistanceKm.toDoubleOrNull() ?: 0.0
    val eff = vehicleEfficiency.toDoubleOrNull() ?: 1.0
    val price = fuelPricePerLitre.toDoubleOrNull() ?: 0.0

    val litresNeeded = if (eff > 0) dist / eff else 0.0
    val totalCost = litresNeeded * price

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Gharama ya Mafuta ya Safari" else "Fuel Cost Calculator",
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
                        text = if (isSwahili) "Taarifa za Safari & Gari" else "Trip & Vehicle Parameters",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = tripDistanceKm,
                        onValueChange = { tripDistanceKm = it },
                        label = { Text(if (isSwahili) "Umbali wa Safari (km)" else "Trip Distance (km)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = vehicleEfficiency,
                        onValueChange = { vehicleEfficiency = it },
                        label = { Text(if (isSwahili) "Uwezo wa Gari (km kwa lita 1)" else "Fuel Consumption (km / Litre)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = fuelPricePerLitre,
                        onValueChange = { fuelPricePerLitre = it },
                        label = { Text(if (isSwahili) "Bei ya Mafuta kwa Lita (TSh)" else "Fuel Price per Litre (TZS)") },
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
                        text = if (isSwahili) "Jumla ya Gharama ya Mafuta" else "Total Estimated Fuel Cost",
                        style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Text(
                        text = formatTsh(totalCost),
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
                            Text(text = if (isSwahili) "Lita Zinazohitajika" else "Litres Required", style = MaterialTheme.typography.labelSmall)
                            Text(text = "%.1f Lita".format(litresNeeded), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        }
                        Column {
                            Text(text = if (isSwahili) "Gharama kwa Kilomita" else "Cost per km", style = MaterialTheme.typography.labelSmall)
                            Text(text = formatTsh(if (dist > 0) totalCost / dist else 0.0), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }
    }
}
