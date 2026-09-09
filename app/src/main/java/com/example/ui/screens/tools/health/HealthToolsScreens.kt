package com.example.ui.screens.tools.health

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalDrink
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MsaadaTopBar
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaTeal

// -------------------------------------------------------------
// 1. BMI CALCULATOR SCREEN
// -------------------------------------------------------------
@Composable
fun BmiCalculatorScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var weightKg by remember { mutableStateOf("70") }
    var heightCm by remember { mutableStateOf("175") }

    val w = weightKg.toDoubleOrNull() ?: 0.0
    val h = (heightCm.toDoubleOrNull() ?: 0.0) / 100.0 // to meters

    val bmi = if (h > 0) w / (h * h) else 0.0

    val minIdealWeight = 18.5 * (h * h)
    val maxIdealWeight = 24.9 * (h * h)

    val (statusLabel, statusColor, advice) = when {
        bmi < 18.5 -> Triple(
            if (isSwahili) "Uzito Uliopungua (Underweight)" else "Underweight",
            Color(0xFF3B82F6),
            if (isSwahili) "Uzito wako uko chini ya kiwango cha kawaida. Inashauriwa kuongeza virutubisho na milo yenye lishe bora."
            else "Your weight is below standard range. Nutritional balance with wholesome calories is advised."
        )
        bmi in 18.5..24.9 -> Triple(
            if (isSwahili) "Uzito wa Kawaida (Normal / Afya)" else "Normal Weight",
            Color(0xFF10B981),
            if (isSwahili) "Hongera! Una uzito unaofaa kiafya. Endelea kudumisha mlo kamili na mazoezi ya kila siku."
            else "Congratulations! Your BMI is in the healthy zone. Maintain your balanced diet and regular exercise."
        )
        bmi in 25.0..29.9 -> Triple(
            if (isSwahili) "Uzito Uliozidi (Overweight)" else "Overweight",
            Color(0xFFF59E0B),
            if (isSwahili) "Uzito wako umevuka kiwango cha kawaida. Jaribu kupunguza vyakula vya sukari na mafuta mengi, na ufanye mazoezi ya cardio."
            else "You are slightly above ideal weight. Moderate dietary improvements and daily physical activity can help."
        )
        else -> Triple(
            if (isSwahili) "Unene Uliokithiri (Obese)" else "Obese",
            Color(0xFFEF4444),
            if (isSwahili) "Kiwango cha unene kinahitaji uangalizi. Inashauriwa kushauriana na mtaalamu wa afya au daktari kwa mwongozo wa lishe."
            else "High BMI indicates obesity. Consultation with a healthcare provider or nutritionist is recommended."
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Kipima Uzito & Afya (BMI)" else "BMI Calculator",
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
                        text = if (isSwahili) "Ingiza Uzito na Urefu Wako" else "Enter Weight & Height",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = weightKg,
                        onValueChange = { weightKg = it },
                        label = { Text(if (isSwahili) "Uzito (Kilogramu / kg)" else "Weight (kg)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = heightCm,
                        onValueChange = { heightCm = it },
                        label = { Text(if (isSwahili) "Urefu (Sentimita / cm)" else "Height (cm)") },
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isSwahili) "Kiwango Chako cha BMI" else "Your BMI Score",
                        style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "%.1f".format(bmi),
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = statusColor
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(statusColor.copy(alpha = 0.15f))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = statusLabel,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = statusColor
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = advice,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    if (h > 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isSwahili)
                                "Uzito unaofaa kwa urefu wako: ${"%.1f".format(minIdealWeight)} kg - ${"%.1f".format(maxIdealWeight)} kg"
                            else
                                "Ideal weight range for your height: ${"%.1f".format(minIdealWeight)} kg - ${"%.1f".format(maxIdealWeight)} kg",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = MsaadaTeal)
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. WATER INTAKE TRACKER SCREEN
// -------------------------------------------------------------
@Composable
fun WaterIntakeScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var currentMl by remember { mutableIntStateOf(1000) }
    val goalMl = 2500

    val progress = (currentMl.toFloat() / goalMl.toFloat()).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Kiwango cha Maji ya Kunywa" else "Water Intake Tracker",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Outlined.LocalDrink, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$currentMl mL",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = Color(0xFF0369A1))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = if (isSwahili) "Lengo la Leo: $goalMl mL (glasi ~10)" else "Daily Goal: $goalMl mL (~10 glasses)",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = Color(0xFF0284C7),
                        trackColor = Color(0xFFE2E8F0)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${(progress * 100).toInt()}% ya lengo imefikiwa",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }

            // Quick Add Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { currentMl = (currentMl + 250).coerceAtMost(5000) },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
                    ) {
                        Text(text = "+ Glasi 1 (250 mL)")
                    }

                    Button(
                        onClick = { currentMl = (currentMl + 500).coerceAtMost(5000) },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MsaadaNavy)
                    ) {
                        Text(text = "+ Chupa (500 mL)")
                    }
                }

                OutlinedButton(
                    onClick = { currentMl = 0 },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(imageVector = Icons.Outlined.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = if (isSwahili) "Anza Upya Leo" else "Reset Daily Count")
                }
            }
        }
    }
}
