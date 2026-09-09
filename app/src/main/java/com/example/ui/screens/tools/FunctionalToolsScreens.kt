package com.example.ui.screens.tools

import android.content.Intent
import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MsaadaTopBar
import com.example.ui.theme.MsaadaIcons
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaTeal
import kotlinx.coroutines.delay
import java.security.SecureRandom
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.ceil

@Composable
private fun ToolScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(topBar = { MsaadaTopBar(title = title, onBack = onBack) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).background(MaterialTheme.colorScheme.background)
        ) { content() }
    }
}

@Composable
fun HomeExpenseTrackerScreen(isSwahili: Boolean, onBack: () -> Unit) {
    var income by remember { mutableStateOf("") }
    var expenses by remember { mutableStateOf("") }
    val incomeValue = income.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val expenseValue = expenses.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val balance = incomeValue - expenseValue
    ToolScaffold(if (isSwahili) "Matumizi ya Nyumbani" else "Home Expense Tracker", onBack) {
        LazyColumn(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(if (isSwahili) "Muhtasari wa Bajeti ya Nyumbani" else "Home Budget Summary", fontWeight = FontWeight.Bold)
                        OutlinedTextField(income, { income = it }, Modifier.fillMaxWidth(), label = { Text(if (isSwahili) "Mapato (TSh)" else "Income (TSh)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true)
                        OutlinedTextField(expenses, { expenses = it }, Modifier.fillMaxWidth(), label = { Text(if (isSwahili) "Matumizi Yote (TSh)" else "Total Expenses (TSh)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true)
                    }
                }
            }
            item {
                ResultCard(if (isSwahili) "Salio" else "Balance", money(balance), if (balance >= 0) MsaadaTeal else MaterialTheme.colorScheme.error)
            }
            item {
                Text(if (isSwahili) "Tumia zana hii kama muhtasari wa haraka. Kwa ufuatiliaji wa kila matumizi, tumia orodha/rekodi zako za ndani." else "Use this as a quick summary. For item-by-item tracking, keep individual records in the app.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun StudyTimerScreen(isSwahili: Boolean, onBack: () -> Unit) {
    var workMinutes by remember { mutableIntStateOf(25) }
    var breakMinutes by remember { mutableIntStateOf(5) }
    var secondsLeft by remember { mutableIntStateOf(workMinutes * 60) }
    var running by remember { mutableStateOf(false) }
    var isBreak by remember { mutableStateOf(false) }

    LaunchedEffect(running, secondsLeft) {
        if (running && secondsLeft > 0) {
            delay(1000)
            secondsLeft -= 1
        } else if (running && secondsLeft == 0) {
            val nextIsBreak = !isBreak
            isBreak = nextIsBreak
            secondsLeft = (if (nextIsBreak) breakMinutes else workMinutes) * 60
        }
    }

    val minutes = secondsLeft / 60
    val seconds = secondsLeft % 60
    ToolScaffold(if (isSwahili) "Study Timer" else "Study Timer", onBack) {
        Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = MsaadaNavy)) {
                Column(Modifier.fillMaxWidth().padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(if (isBreak) if (isSwahili) "Mapumziko" else "Break" else if (isSwahili) "Kujisomea" else "Study", color = androidx.compose.ui.graphics.Color.White.copy(alpha = .8f))
                    Text(String.format(Locale.US, "%02d:%02d", minutes, seconds), style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Black, color = androidx.compose.ui.graphics.Color.White)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(onClick = { running = !running }) { Text(if (running) if (isSwahili) "Simamisha" else "Pause" else if (isSwahili) "Anza" else "Start") }
                OutlinedButton(onClick = { running = false; isBreak = false; secondsLeft = workMinutes * 60 }) { Text(if (isSwahili) "Anza Upya" else "Reset") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilterChip(selected = workMinutes == 25, onClick = { workMinutes = 25; if (!running) secondsLeft = 25 * 60 }, label = { Text("25 min") })
                FilterChip(selected = workMinutes == 50, onClick = { workMinutes = 50; if (!running) secondsLeft = 50 * 60 }, label = { Text("50 min") })
                FilterChip(selected = breakMinutes == 5, onClick = { breakMinutes = 5 }, label = { Text("5 min") })
                FilterChip(selected = breakMinutes == 10, onClick = { breakMinutes = 10 }, label = { Text("10 min") })
            }
        }
    }
}

@Composable
fun TravelChecklistScreen(isSwahili: Boolean, onBack: () -> Unit) {
    val defaultItems = remember { mutableStateListOf("Kitambulisho", "Simu na chaja", "Nguo", "Dawa muhimu", "Fedha / kadi") }
    val checked = remember { mutableStateListOf<Boolean>().apply { repeat(defaultItems.size) { add(false) } } }
    var newItem by remember { mutableStateOf("") }
    ToolScaffold(if (isSwahili) "Orodha ya Safari" else "Travel Checklist", onBack) {
        LazyColumn(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(newItem, { newItem = it }, Modifier.weight(1f), label = { Text(if (isSwahili) "Ongeza kitu" else "Add item") }, singleLine = true)
                    Button(onClick = { if (newItem.isNotBlank()) { defaultItems.add(newItem.trim()); checked.add(false); newItem = "" } }) { Icon(MsaadaIcons.Add, null); Text(if (isSwahili) "Ongeza" else "Add") }
                }
            }
            items(defaultItems.indices.toList()) { index ->
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Row(Modifier.fillMaxWidth().clickable { checked[index] = !checked[index] }.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked[index], { checked[index] = it })
                        Text(defaultItems[index], fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun TripCostSplitterScreen(isSwahili: Boolean, onBack: () -> Unit) {
    var total by remember { mutableStateOf("") }
    var people by remember { mutableStateOf("2") }
    val totalValue = total.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val peopleValue = (people.toIntOrNull() ?: 0).coerceAtLeast(0)
    val perPerson = if (peopleValue > 0) totalValue / peopleValue else 0.0
    ToolScaffold(if (isSwahili) "Gawanya Gharama za Safari" else "Trip Cost Splitter", onBack) {
        LazyColumn(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item { Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(total, { total = it }, Modifier.fillMaxWidth(), label = { Text(if (isSwahili) "Gharama ya Safari (TSh)" else "Trip Cost (TSh)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true)
                    OutlinedTextField(people, { people = it }, Modifier.fillMaxWidth(), label = { Text(if (isSwahili) "Idadi ya Abiria" else "Passengers") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                }
            } }
            item { ResultCard(if (isSwahili) "Kila Mtu Anachangia" else "Each Person Pays", money(perPerson), MsaadaTeal) }
        }
    }
}

@Composable
fun EmergencyContactsScreen(isSwahili: Boolean, onBack: () -> Unit) {
    val context = LocalContext.current
    val contacts = listOf("Polisi" to "112", "Zimamoto" to "114", "Ambulance" to "115")
    ToolScaffold(if (isSwahili) "Namba za Dharura" else "Emergency Contacts", onBack) {
        LazyColumn(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item { Text(if (isSwahili) "Namba hizi zinaweza kutofautiana kwa eneo; thibitisha kabla ya kuzitegemea wakati wa dharura." else "Emergency numbers can vary by area; verify locally before relying on them.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            items(contacts) { (name, number) ->
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column { Text(name, fontWeight = FontWeight.Bold); Text(number, color = MsaadaTeal) }
                        Button(onClick = { context.startActivity(Intent(Intent.ACTION_DIAL).apply { data = android.net.Uri.parse("tel:$number") }) }, colors = ButtonDefaults.buttonColors(containerColor = MsaadaNavy)) { Text(if (isSwahili) "Piga" else "Call") }
                    }
                }
            }
        }
    }
}

@Composable
fun FirstAidScreen(isSwahili: Boolean, onBack: () -> Unit) {
    val items = listOf(
        Pair(if (isSwahili) "Kutokwa damu" else "Bleeding", if (isSwahili) "Bonyeza jeraha kwa kitambaa safi kwa uthabiti. Tafuta huduma ya dharura ikiwa damu haikomi." else "Apply firm pressure with a clean cloth. Seek emergency care if bleeding does not stop."),
        Pair(if (isSwahili) "Kuungua" else "Burns", if (isSwahili) "Pooza eneo kwa maji safi yanayotiririka. Usipasue malengelenge. Tafuta huduma kwa kuungua sana." else "Cool the area with clean running water. Do not burst blisters. Seek care for serious burns."),
        Pair(if (isSwahili) "Kuzimia" else "Fainting", if (isSwahili) "Mlaze mtu kwa usalama na angalia upumuaji. Ikiwa hapumui kawaida, piga huduma ya dharura." else "Place the person safely and check breathing. If they are not breathing normally, call emergency services.")
    )
    ToolScaffold(if (isSwahili) "Huduma ya Kwanza" else "Basic First Aid", onBack) {
        LazyColumn(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item { Text(if (isSwahili) "Mwongozo wa msingi tu; si mbadala wa mtaalamu wa afya." else "Basic guidance only; not a substitute for professional medical care.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            items(items) { pair -> Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text(pair.first, fontWeight = FontWeight.Bold); Text(pair.second) } } }
        }
    }
}

@Composable
fun MedicineReminderScreen(isSwahili: Boolean, onBack: () -> Unit) {
    data class Med(val name: String, val time: String)
    val meds = remember { mutableStateListOf<Med>() }
    var name by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    ToolScaffold(if (isSwahili) "Kumbukumbu ya Dawa" else "Medicine Reminder", onBack) {
        LazyColumn(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item { Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth(), label = { Text(if (isSwahili) "Jina la Dawa" else "Medicine") }, singleLine = true)
                OutlinedTextField(time, { time = it }, Modifier.fillMaxWidth(), label = { Text(if (isSwahili) "Muda (mf. 08:00)" else "Time (e.g. 08:00)") }, singleLine = true)
                Button(onClick = { if (name.isNotBlank() && time.isNotBlank()) { meds.add(Med(name.trim(), time.trim())); name = ""; time = "" } }, colors = ButtonDefaults.buttonColors(containerColor = MsaadaNavy)) { Icon(MsaadaIcons.Add, null); Text(if (isSwahili) "Ongeza Ratiba" else "Add Schedule") }
            } } }
            items(meds) { med -> Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) { Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text(med.name, fontWeight = FontWeight.Bold); Text(med.time, color = MsaadaTeal) } } }
        }
    }
}

@Composable
fun FarmTaskPlannerScreen(isSwahili: Boolean, onBack: () -> Unit) {
    val tasks = remember { mutableStateListOf("Kuandaa shamba", "Kupanda", "Kupalilia", "Kuweka mbolea", "Kukagua wadudu", "Kuvuna") }
    val done = remember { mutableStateListOf<Boolean>().apply { repeat(tasks.size) { add(false) } } }
    ToolScaffold(if (isSwahili) "Ratiba ya Kazi za Shamba" else "Farm Task Planner", onBack) {
        LazyColumn(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(tasks.indices.toList()) { i -> Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) { Row(Modifier.fillMaxWidth().clickable { done[i] = !done[i] }.padding(12.dp), verticalAlignment = Alignment.CenterVertically) { Checkbox(done[i], { done[i] = it }); Text(tasks[i], fontWeight = FontWeight.SemiBold) } } }
        }
    }
}

@Composable
fun FarmProfitCalculatorScreen(isSwahili: Boolean, onBack: () -> Unit) {
    var costs by remember { mutableStateOf("") }
    var sales by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    val c = costs.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val s = sales.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val q = quantity.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val profit = s - c
    val margin = if (s > 0) profit / s * 100 else 0.0
    ToolScaffold(if (isSwahili) "Faida ya Shamba" else "Farm Profit Calculator", onBack) {
        LazyColumn(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item { Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) { Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(costs, { costs = it }, Modifier.fillMaxWidth(), label = { Text(if (isSwahili) "Jumla ya Gharama (TSh)" else "Total Costs (TSh)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true)
                OutlinedTextField(sales, { sales = it }, Modifier.fillMaxWidth(), label = { Text(if (isSwahili) "Mapato ya Mauzo (TSh)" else "Sales Revenue (TSh)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true)
                OutlinedTextField(quantity, { quantity = it }, Modifier.fillMaxWidth(), label = { Text(if (isSwahili) "Kiasi/Mavuno" else "Harvest Quantity") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true)
            } } }
            item { ResultCard(if (isSwahili) "Faida" else "Profit", money(profit), if (profit >= 0) MsaadaTeal else MaterialTheme.colorScheme.error) }
            item { Text(
                (if (isSwahili) "Margin: %.1f%% • Faida kwa unit: %s" else "Margin: %.1f%% • Profit per unit: %s")
                    .format(Locale.US, margin, money(if (q > 0) profit / q else 0.0)),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ) }
        }
    }
}

@Composable
fun PlantingPlannerScreen(isSwahili: Boolean, onBack: () -> Unit) {
    var crop by remember { mutableStateOf("Mahindi") }
    var daysText by remember { mutableStateOf("120") }
    var startDate by remember { mutableStateOf(LocalDate.now().format(DateTimeFormatter.ISO_DATE)) }
    val days = daysText.toLongOrNull()?.coerceAtLeast(0L) ?: 0L
    val harvest = runCatching { LocalDate.parse(startDate).plusDays(days) }.getOrNull()
    ToolScaffold(if (isSwahili) "Planting Planner" else "Planting Planner", onBack) {
        LazyColumn(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item { Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) { Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(crop, { crop = it }, Modifier.fillMaxWidth(), label = { Text(if (isSwahili) "Zao" else "Crop") }, singleLine = true)
                OutlinedTextField(startDate, { startDate = it }, Modifier.fillMaxWidth(), label = { Text(if (isSwahili) "Tarehe ya Kupanda (YYYY-MM-DD)" else "Planting Date (YYYY-MM-DD)") }, singleLine = true)
                OutlinedTextField(daysText, { daysText = it }, Modifier.fillMaxWidth(), label = { Text(if (isSwahili) "Siku hadi kukomaa" else "Days to maturity") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
            } } }
            item { ResultCard(if (isSwahili) "Makadirio ya Kuvuna $crop" else "Estimated $crop Harvest", harvest?.format(DateTimeFormatter.ISO_DATE) ?: "—", MsaadaTeal) }
        }
    }
}

@Composable
fun PasswordGeneratorScreen(isSwahili: Boolean, onBack: () -> Unit) {
    var length by remember { mutableIntStateOf(16) }
    var password by remember { mutableStateOf("") }
    val chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#%^&*"
    fun generate() { val random = SecureRandom(); password = buildString { repeat(length.coerceIn(8, 64)) { append(chars[random.nextInt(chars.length)]) } } }
    ToolScaffold(if (isSwahili) "Password Generator" else "Password Generator", onBack) {
        Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            OutlinedTextField(length.toString(), { length = it.toIntOrNull()?.coerceIn(8, 64) ?: length }, Modifier.fillMaxWidth(), label = { Text(if (isSwahili) "Urefu wa Password" else "Password Length") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
            Button(onClick = { generate() }, colors = ButtonDefaults.buttonColors(containerColor = MsaadaNavy), modifier = Modifier.fillMaxWidth()) { Icon(MsaadaIcons.Refresh, null); Text(if (isSwahili) "Tengeneza Password" else "Generate Password") }
            if (password.isNotBlank()) Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MsaadaTeal.copy(alpha = .12f))) { Text(password, Modifier.padding(18.dp), fontWeight = FontWeight.Bold, letterSpacing = 1.sp) }
            Text(if (isSwahili) "Password hii inatengenezwa kwenye kifaa chako." else "The password is generated locally on your device.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun TextCounterScreen(isSwahili: Boolean, onBack: () -> Unit) {
    var text by remember { mutableStateOf("") }
    val words = text.trim().takeIf { it.isNotEmpty() }?.split(Regex("\\s+")).orEmpty().size
    val chars = text.length
    val charsNoSpaces = text.count { !it.isWhitespace() }
    val lines = if (text.isEmpty()) 0 else text.count { it == '\n' } + 1
    val readMinutes = ceil(words / 200.0).toInt()
    ToolScaffold(if (isSwahili) "Hesabu Maandishi" else "Text Counter", onBack) {
        Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(text, { text = it }, Modifier.fillMaxWidth().weight(1f), label = { Text(if (isSwahili) "Andika au bandika maandishi" else "Type or paste text") })
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("Maneno", words.toString(), Modifier.weight(1f)); StatCard("Herufi", chars.toString(), Modifier.weight(1f)); StatCard("Mistari", lines.toString(), Modifier.weight(1f))
            }
            Text("Herufi bila nafasi: $charsNoSpaces • Muda wa kusoma: ~$readMinutes dk", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun TechnologyUtilitiesScreen(isSwahili: Boolean, onBack: () -> Unit) {
    var tab by remember { mutableIntStateOf(0) }
    var json by remember { mutableStateOf("") }
    var base64Input by remember { mutableStateOf("") }
    var base64Encoded by remember { mutableStateOf("") }
    var hex by remember { mutableStateOf("#008C95") }
    val base64Decoded = runCatching { String(Base64.decode(base64Input, Base64.DEFAULT)) }.getOrNull().orEmpty()
    val jsonOut = runCatching {
        org.json.JSONObject(json).toString(2)
    }.getOrNull().orEmpty()
    ToolScaffold(if (isSwahili) "Developer Utilities" else "Developer Utilities", onBack) {
        Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilterChip(selected = tab == 0, onClick = { tab = 0 }, label = { Text("JSON") })
                FilterChip(selected = tab == 1, onClick = { tab = 1 }, label = { Text("Base64") })
                FilterChip(selected = tab == 2, onClick = { tab = 2 }, label = { Text("Color") })
            }
            when (tab) {
                0 -> {
                    OutlinedTextField(json, { json = it }, Modifier.fillMaxWidth().weight(1f), label = { Text("JSON") })
                    Text(if (jsonOut.isBlank()) "—" else jsonOut, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                }
                1 -> {
                    OutlinedTextField(base64Input, { base64Input = it }, Modifier.fillMaxWidth(), label = { Text("Base64 input") })
                    Text(if (base64Decoded.isBlank()) "—" else base64Decoded)
                    Button(onClick = { base64Encoded = Base64.encodeToString(base64Input.toByteArray(), Base64.NO_WRAP) }, colors = ButtonDefaults.buttonColors(containerColor = MsaadaNavy)) { Text("Encode text") }
                    if (base64Encoded.isNotBlank()) Text(base64Encoded, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                }
                else -> {
                    OutlinedTextField(hex, { hex = it }, Modifier.fillMaxWidth(), label = { Text("Hex color") }, singleLine = true)
                    val valid = Regex("^#[0-9A-Fa-f]{6}$").matches(hex)
                    Text(if (valid) "Valid color: $hex" else "Weka rangi ya HEX kama #001848", color = if (valid) MsaadaTeal else MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun ResultCard(label: String, value: String, accent: androidx.compose.ui.graphics.Color) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(18.dp)) { Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant); Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = accent) }
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier) {
    Card(modifier, shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) { Column(Modifier.padding(12.dp)) { Text(value, fontWeight = FontWeight.Bold); Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) } }
}

private fun money(value: Double): String = "TSh " + String.format(Locale.US, "%,.0f", value)
