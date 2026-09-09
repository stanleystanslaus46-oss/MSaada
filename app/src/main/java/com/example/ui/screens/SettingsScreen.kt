package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MsaadaLogo
import com.example.ui.components.MsaadaTopBar
import com.example.ui.components.ProBadge
import com.example.ui.theme.MsaadaIcons
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaProGold
import com.example.ui.theme.MsaadaProGoldLight
import com.example.ui.theme.MsaadaTeal

@Composable
fun SettingsScreen(
    isSwahili: Boolean,
    language: String,
    themeMode: String,
    notificationsEnabled: Boolean,
    isProUser: Boolean,
    onLanguageChange: (String) -> Unit,
    onThemeModeChange: (String) -> Unit,
    onNotificationsToggle: (Boolean) -> Unit,
    onClearAllData: () -> Unit,
    onOpenPro: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showClearDataDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Mipangilio" else "Settings"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // PRO Upgrade Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .clickable(onClick = onOpenPro)
                    .testTag("settings_pro_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isProUser) Color(0xFFD1FAE5) else MsaadaProGoldLight.copy(alpha = 0.4f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isProUser) Color(0xFF10B981) else MsaadaProGold),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = MsaadaIcons.Pro,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "MSAADA PRO",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isProUser) Color(0xFF065F46) else Color(0xFF92400E)
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            if (isProUser) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFF10B981))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "ACTIVE",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        )
                                    )
                                }
                            }
                        }
                        Text(
                            text = if (isProUser)
                                (if (isSwahili) "Huduma zote za PRO zimefunguliwa" else "All PRO features unlocked")
                            else
                                (if (isSwahili) "Picha bila kikomo na violezo vya ziada" else "Unlimited photos and extra templates"),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isProUser) Color(0xFF047857) else Color(0xFFB45309)
                            )
                        )
                    }

                    Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = null,
                        tint = if (isProUser) Color(0xFF065F46) else Color(0xFF92400E)
                    )
                }
            }

            // Group: General
            Text(
                text = if (isSwahili) "MIPANGILIO YA JUMLA" else "GENERAL PREFERENCES",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    // Language Setting
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showLanguageDialog = true }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = MsaadaIcons.Language, contentDescription = null, tint = MsaadaTeal)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = if (isSwahili) "Lugha" else "Language",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (language == "sw") "Kiswahili" else "English",
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                            Icon(imageVector = MsaadaIcons.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    // Appearance Setting
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showThemeDialog = true }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = MsaadaIcons.DarkMode, contentDescription = null, tint = MsaadaTeal)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = if (isSwahili) "Mandhari" else "Appearance",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val modeLabel = when (themeMode) {
                                "light" -> if (isSwahili) "Mwangaza" else "Light"
                                "dark" -> if (isSwahili) "Giza" else "Dark"
                                else -> if (isSwahili) "Mfumo wa Simu" else "System default"
                            }
                            Text(
                                text = modeLabel,
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                            Icon(imageVector = MsaadaIcons.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    // Notifications Setting
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = MsaadaIcons.Notifications, contentDescription = null, tint = MsaadaTeal)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (isSwahili) "Arifa na Vikumbusho" else "Notifications & Reminders",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                                )
                                Text(
                                    text = if (isSwahili) "Kumbukumbu za dawa na mitihani" else "Medication & exam alerts",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = onNotificationsToggle,
                            colors = SwitchDefaults.colors(checkedThumbColor = MsaadaTeal, checkedTrackColor = MsaadaTeal.copy(alpha = 0.4f))
                        )
                    }
                }
            }

            // Group: Data & Security
            Text(
                text = if (isSwahili) "DATA & USALAMA" else "DATA & SECURITY",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showClearDataDialog = true }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = MsaadaIcons.Delete, contentDescription = null, tint = Color(0xFFEF4444))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = if (isSwahili) "Futa Data Yote ya Ndani" else "Clear All Local Data",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFFEF4444)
                                )
                            )
                        }
                        Icon(imageVector = MsaadaIcons.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Group: About
            Text(
                text = if (isSwahili) "KUHUSU" else "ABOUT",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAboutDialog = true }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = MsaadaIcons.Info, contentDescription = null, tint = MsaadaTeal)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = if (isSwahili) "Kuhusu MSAADA" else "About MSAADA",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "v1.0.0",
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                            Icon(imageVector = MsaadaIcons.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer branding
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "MSAADA",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    text = "“Zana Rahisi. Kazi Rahisi. Maisha Rahisi.”",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }

    // Language Dialog
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(text = if (isSwahili) "Chagua Lugha" else "Choose Language") },
            text = {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onLanguageChange("sw")
                                showLanguageDialog = false
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = language == "sw",
                            onClick = {
                                onLanguageChange("sw")
                                showLanguageDialog = false
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = MsaadaTeal)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Kiswahili (Chaguo-msingi)")
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onLanguageChange("en")
                                showLanguageDialog = false
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = language == "en",
                            onClick = {
                                onLanguageChange("en")
                                showLanguageDialog = false
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = MsaadaTeal)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "English")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text(text = if (isSwahili) "Funga" else "Close")
                }
            }
        )
    }

    // Theme Dialog
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text(text = if (isSwahili) "Chagua Mandhari" else "Choose Appearance") },
            text = {
                Column {
                    listOf(
                        "system" to (if (isSwahili) "Mfumo wa Simu" else "System default"),
                        "light" to (if (isSwahili) "Mwangaza" else "Light"),
                        "dark" to (if (isSwahili) "Giza" else "Dark")
                    ).forEach { (mode, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onThemeModeChange(mode)
                                    showThemeDialog = false
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = themeMode == mode,
                                onClick = {
                                    onThemeModeChange(mode)
                                    showThemeDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = MsaadaTeal)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text(text = if (isSwahili) "Funga" else "Close")
                }
            }
        )
    }

    // Clear Data Confirmation Dialog
    if (showClearDataDialog) {
        AlertDialog(
            onDismissRequest = { showClearDataDialog = false },
            title = { Text(text = if (isSwahili) "Futa Data Yote?" else "Clear All Data?") },
            text = {
                Text(
                    text = if (isSwahili)
                        "Hatua hii itafuta orodha zote za ununuzi, maombi, mitihani na nyaraka zilizohifadhiwa kwenye simu yako. Hatua hii haiwezi kubadilishwa."
                    else
                        "This action will remove all shopping lists, prayers, exams, and documents stored locally on your device. This cannot be undone."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onClearAllData()
                        showClearDataDialog = false
                        Toast.makeText(
                            context,
                            if (isSwahili) "Data zote za ndani zimefutwa" else "All local data has been cleared",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text(text = if (isSwahili) "Futa Kila Kitu" else "Clear Everything", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDataDialog = false }) {
                    Text(text = if (isSwahili) "Ghairi" else "Cancel")
                }
            }
        )
    }

    // About Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MsaadaLogo(size = 32, horizontal = true)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "MSAADA v1.0.0",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "“Zana Rahisi. Kazi Rahisi. Maisha Rahisi.”",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MsaadaTeal, fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isSwahili)
                            "MSAADA ni programu ya kila siku iliyotengenezwa kutoa zana rahisi, zenye manufaa kwa wananchi, wanafunzi, familia, makanisa, wakulima, na wafanyabiashara wadogo nchini Tanzania."
                        else
                            "MSAADA is an everyday-life utility application crafted to provide simple, useful tools for individuals, students, families, churches, farmers, and small businesses in Tanzania.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isSwahili)
                            "100% Salama & Offline: Data zote zinabaki kwenye simu yako pekee. Hakuna akaunti wala ufuatiliaji wa siri."
                        else
                            "100% Secure & Offline-First: All your data stays locally on your device. No cloud tracking or mandatory accounts.",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF047857))
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text(text = if (isSwahili) "Sawa" else "OK")
                }
            }
        )
    }
}
