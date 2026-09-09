package com.example.ui.screens.tools.education

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Refresh
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExamItem
import com.example.data.model.StudyPlanItem
import com.example.ui.components.MsaadaTopBar
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaTeal
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

// -------------------------------------------------------------
// 1. GPA CALCULATOR SCREEN
// -------------------------------------------------------------
data class CourseEntry(
    val code: String,
    val grade: String,
    val units: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GpaCalculatorScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    val gradePointsMap = mapOf(
        "A" to 5.0,
        "B+" to 4.0,
        "B" to 3.0,
        "C" to 2.0,
        "D" to 1.0,
        "E" to 0.5,
        "F" to 0.0
    )

    val courses = remember {
        mutableStateListOf(
            CourseEntry("CS 101: Programming", "A", 3),
            CourseEntry("MT 114: Calculus", "B+", 4),
            CourseEntry("PH 120: Physics", "B", 3),
            CourseEntry("DS 100: Development Studies", "A", 2)
        )
    }

    var newCourseCode by remember { mutableStateOf("") }
    var selectedGrade by remember { mutableStateOf("A") }
    var selectedUnits by remember { mutableStateOf("3") }
    var expandedGradeDropdown by remember { mutableStateOf(false) }

    val totalUnits = courses.sumOf { it.units }
    val totalPoints = courses.sumOf { (gradePointsMap[it.grade] ?: 0.0) * it.units }
    val gpa = if (totalUnits > 0) totalPoints / totalUnits else 0.0

    val classification = when {
        gpa >= 4.4 -> if (isSwahili) "First Class (Daraja la Kwanza)" else "First Class Honours"
        gpa >= 3.5 -> if (isSwahili) "Upper Second (Daraja la Pili Juu)" else "Upper Second Class"
        gpa >= 2.7 -> if (isSwahili) "Lower Second (Daraja la Pili Chini)" else "Lower Second Class"
        gpa >= 2.0 -> if (isSwahili) "Pass (Kufaulu)" else "Pass"
        else -> if (isSwahili) "Fail (Kufeli)" else "Fail"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "GPA Calculator" else "GPA Calculator",
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // GPA Result Card
            item {
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
                            text = if (isSwahili) "GPA Yako ya Sasa" else "Your Current GPA",
                            style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "%.2f".format(gpa),
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = if (gpa >= 3.5) MsaadaTeal else if (gpa >= 2.0) MsaadaNavy else Color(0xFFEF4444)
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (gpa >= 3.0) Color(0xFFD1FAE5) else Color(0xFFFEF3C7))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = classification,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (gpa >= 3.0) Color(0xFF065F46) else Color(0xFF92400E)
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isSwahili) "Jumla ya Mikopo: $totalUnits | Alama za GPA: ${"%.1f".format(totalPoints)}"
                                   else "Total Credits: $totalUnits | Quality Points: ${"%.1f".format(totalPoints)}",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                }
            }

            // Add Course Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isSwahili) "Ongeza Somo / Kozi" else "Add Course / Subject",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = newCourseCode,
                            onValueChange = { newCourseCode = it },
                            placeholder = { Text(if (isSwahili) "Jina au Msimbo (mfano: CS 102)" else "Course Name or Code") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Grade Selector
                            ExposedDropdownMenuBox(
                                expanded = expandedGradeDropdown,
                                onExpandedChange = { expandedGradeDropdown = !expandedGradeDropdown },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedGrade,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text(if (isSwahili) "Daraja" else "Grade") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGradeDropdown) },
                                    modifier = Modifier.menuAnchor(),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedGradeDropdown,
                                    onDismissRequest = { expandedGradeDropdown = false }
                                ) {
                                    gradePointsMap.keys.forEach { grade ->
                                        DropdownMenuItem(
                                            text = { Text("$grade (${gradePointsMap[grade]} pts)") },
                                            onClick = {
                                                selectedGrade = grade
                                                expandedGradeDropdown = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Units / Credits
                            OutlinedTextField(
                                value = selectedUnits,
                                onValueChange = { selectedUnits = it },
                                label = { Text(if (isSwahili) "Mikopo (Credits)" else "Credits") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                val u = selectedUnits.toIntOrNull() ?: 1
                                if (newCourseCode.isNotBlank() && u > 0) {
                                    courses.add(CourseEntry(newCourseCode.trim(), selectedGrade, u))
                                    newCourseCode = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MsaadaTeal)
                        ) {
                            Text(text = if (isSwahili) "Ongeza Kozi" else "Add Course")
                        }
                    }
                }
            }

            // Courses List
            items(courses) { course ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MsaadaNavy),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = course.grade, color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = course.code, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                Text(
                                    text = "${course.units} ${if (isSwahili) "Mikopo" else "Credits"} • ${(gradePointsMap[course.grade] ?: 0.0) * course.units} pts",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }

                        IconButton(onClick = { courses.remove(course) }) {
                            Icon(imageVector = Icons.Outlined.Close, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. STUDY PLANNER & POMODORO SCREEN
// -------------------------------------------------------------
@Composable
fun StudyPlannerScreen(
    sessions: List<StudyPlanItem>,
    isSwahili: Boolean,
    onAddSession: (subject: String, task: String, dueDate: String) -> Unit,
    onToggleSession: (StudyPlanItem) -> Unit,
    onDeleteSession: (StudyPlanItem) -> Unit,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Schedule, 1 = Pomodoro

    // Form inputs
    var subject by remember { mutableStateOf("") }
    var topic by remember { mutableStateOf("") }
    var durationMins by remember { mutableStateOf("45") }

    // Pomodoro Timer State
    var pomodoroTimeLeftSeconds by remember { mutableIntStateOf(25 * 60) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var isBreakMode by remember { mutableStateOf(false) }

    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning && pomodoroTimeLeftSeconds > 0) {
            delay(1000)
            pomodoroTimeLeftSeconds--
        }
        if (pomodoroTimeLeftSeconds == 0 && isTimerRunning) {
            isTimerRunning = false
            isBreakMode = !isBreakMode
            pomodoroTimeLeftSeconds = if (isBreakMode) 5 * 60 else 25 * 60
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Mpango wa Masomo & Pomodoro" else "Study Planner & Pomodoro",
            onBack = onBack
        )

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(text = if (isSwahili) "Ratiba ya Masomo" else "Study Schedule") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text(text = "Pomodoro Timer") }
            )
        }

        if (selectedTab == 0) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Add Session Form
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (isSwahili) "Panga Kipindi cha Kusoma" else "Schedule Study Session",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = subject,
                                onValueChange = { subject = it },
                                placeholder = { Text(if (isSwahili) "Somo (mfano: Hisabati, Biolojia...)" else "Subject (e.g. Mathematics)") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                value = topic,
                                onValueChange = { topic = it },
                                placeholder = { Text(if (isSwahili) "Mada (mfano: Trigonometry, Genetics...)" else "Topic") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                value = durationMins,
                                onValueChange = { durationMins = it },
                                placeholder = { Text(if (isSwahili) "Muda (Dakika)" else "Duration (Minutes)") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = {
                                    if (subject.isNotBlank()) {
                                        onAddSession(
                                            subject.trim(),
                                            topic.trim().ifBlank { "Kusoma na Kujikumbusha" },
                                            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
                                        )
                                        subject = ""
                                        topic = ""
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MsaadaTeal)
                            ) {
                                Text(text = if (isSwahili) "Hifadhi Ratiba" else "Save Session")
                            }
                        }
                    }
                }

                // Sessions List
                items(sessions) { session ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { onToggleSession(session) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (session.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Icon(
                                    imageVector = if (session.isCompleted) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                                    contentDescription = null,
                                    tint = if (session.isCompleted) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = session.subject,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (session.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                    if (session.task.isNotBlank()) {
                                        Text(text = session.task, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                                    }
                                    Text(
                                        text = session.dueDate,
                                        style = MaterialTheme.typography.labelSmall.copy(color = MsaadaTeal)
                                    )
                                }
                            }

                            IconButton(onClick = { onDeleteSession(session) }) {
                                Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        } else {
            // Pomodoro Screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val mins = pomodoroTimeLeftSeconds / 60
                val secs = pomodoroTimeLeftSeconds % 60

                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .clip(CircleShape)
                        .background(if (isBreakMode) Color(0xFFD1FAE5) else MsaadaTeal.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isBreakMode) Icons.Outlined.Coffee else Icons.Outlined.MenuBook,
                                contentDescription = null,
                                tint = if (isBreakMode) Color(0xFF065F46) else MsaadaNavy,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBreakMode) (if (isSwahili) "Mapumziko" else "Short Break")
                                       else (if (isSwahili) "Kipindi cha Kusoma" else "Focus Study"),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isBreakMode) Color(0xFF065F46) else MsaadaNavy
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "%02d:%02d".format(mins, secs),
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = if (isBreakMode) Color(0xFF065F46) else MsaadaNavy
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = { isTimerRunning = !isTimerRunning },
                        modifier = Modifier
                            .height(54.dp)
                            .width(140.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if (isTimerRunning) Color(0xFFEF4444) else MsaadaNavy)
                    ) {
                        Icon(imageVector = if (isTimerRunning) Icons.Outlined.Pause else Icons.Outlined.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = if (isTimerRunning) (if (isSwahili) "Sitisha" else "Pause") else (if (isSwahili) "Anza" else "Start"))
                    }

                    OutlinedButton(
                        onClick = {
                            isTimerRunning = false
                            pomodoroTimeLeftSeconds = if (isBreakMode) 5 * 60 else 25 * 60
                        },
                        modifier = Modifier
                            .height(54.dp)
                            .width(110.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(imageVector = Icons.Outlined.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = if (isSwahili) "Rudia" else "Reset")
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 3. EXAM COUNTDOWN SCREEN
// -------------------------------------------------------------
@Composable
fun ExamCountdownScreen(
    exams: List<ExamItem>,
    isSwahili: Boolean,
    onAddExam: (name: String, date: String, notes: String) -> Unit,
    onDeleteExam: (ExamItem) -> Unit,
    onBack: () -> Unit
) {
    var examTitle by remember { mutableStateOf("") }
    var examDateStr by remember { mutableStateOf("2026-11-15") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Kipima Muda cha Mitihani" else "Exam Countdown",
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isSwahili) "Ongeza Mtihani Mpya" else "Add New Exam",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = examTitle,
                            onValueChange = { examTitle = it },
                            placeholder = { Text(if (isSwahili) "Jina la Mtihani (mfano: NECTA Form IV, Chuo...)" else "Exam Name (e.g. NECTA, University Final)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = examDateStr,
                            onValueChange = { examDateStr = it },
                            label = { Text(if (isSwahili) "Tarehe ya Mtihani (YYYY-MM-DD)" else "Exam Date (YYYY-MM-DD)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (examTitle.isNotBlank()) {
                                    onAddExam(examTitle.trim(), examDateStr.trim(), "Mtihani wa Mwaka")
                                    examTitle = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MsaadaNavy)
                        ) {
                            Text(text = if (isSwahili) "Ongeza Kipima Muda" else "Add Countdown")
                        }
                    }
                }
            }

            items(exams) { exam ->
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val targetDate = try { sdf.parse(exam.examDate) } catch (e: Exception) { null }
                val now = System.currentTimeMillis()
                val diffMillis = (targetDate?.time ?: (now + 86400000L * 14)) - now
                val daysRemaining = TimeUnit.MILLISECONDS.toDays(diffMillis).coerceAtLeast(0)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = exam.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Outlined.Event, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = exam.examDate, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "$daysRemaining",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = if (daysRemaining > 7) MsaadaTeal else Color(0xFFEF4444)
                                    )
                                )
                                Text(
                                    text = if (isSwahili) "Siku Zimebaki" else "Days Left",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                            IconButton(onClick = { onDeleteExam(exam) }) {
                                Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}
