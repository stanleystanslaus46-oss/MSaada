package com.example.ui.screens.tools.finance

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
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Share
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.KikobaGroup
import com.example.ui.MsaadaViewModel
import com.example.ui.components.EmptyState
import com.example.ui.components.MsaadaTopBar
import com.example.ui.components.ProBadge
import com.example.ui.components.ProFeatureGateDialog
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaTeal
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Locale

data class KikobaMemberItem(
    val roundNumber: Int,
    var name: String,
    var isPaid: Boolean = false
)

@Composable
fun KikobaCalculatorScreen(
    viewModel: MsaadaViewModel,
    isSwahili: Boolean,
    onBack: () -> Unit,
    onOpenPro: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val isProUser by viewModel.isProUser.collectAsState()
    val savedGroups by viewModel.kikobaGroups.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Kikoba Calculator, 1: Vikundi Vyangu
    var showProGate by remember { mutableStateOf(false) }

    var groupName by remember { mutableStateOf("Kikundi cha Maendeleo") }
    var memberCountText by remember { mutableStateOf("10") }
    var contributionText by remember { mutableStateOf("50000") }
    var socialFundText by remember { mutableStateOf("5000") }
    var frequency by remember { mutableStateOf("mwezi") } // wiki, wiki2, mwezi

    val memberCountParsed = memberCountText.toIntOrNull()
    val memberCount = memberCountParsed ?: 0
    val contributionAmount = contributionText.toDoubleOrNull() ?: 0.0
    val socialFund = socialFundText.toDoubleOrNull() ?: 0.0

    // Calculations
    val totalPoolGross = memberCount * contributionAmount
    val totalSocialFundCycle = memberCount * socialFund
    val netPayout = totalPoolGross - totalSocialFundCycle
    val totalInvestmentPerMember = (contributionAmount + socialFund) * memberCount

    val frequencyLabel = when (frequency) {
        "wiki" -> if (isSwahili) "Wiki" else "Weeks"
        "wiki2" -> if (isSwahili) "Wiki 2 (Nusu Mwezi)" else "Bi-weekly"
        else -> if (isSwahili) "Mwezi" else "Months"
    }

    val membersList = remember(memberCount) {
        mutableStateListOf<KikobaMemberItem>().apply {
            for (i in 1..memberCount.coerceAtMost(30)) {
                add(KikobaMemberItem(i, "Mwanachama $i", false))
            }
        }
    }

    fun formatTsh(amount: Double): String {
        return "TSh " + NumberFormat.getNumberInstance(Locale.US).format(amount.toLong())
    }

    fun buildSummaryText(): String {
        val sb = StringBuilder()
        sb.append("MSAADA KIKOBA CALCULATOR\n")
        sb.append("Kikundi: $groupName\n")
        sb.append("Wanachama: $memberCount\n")
        sb.append("Mchango kwa Mwanachama kwa Mzunguko: ${formatTsh(contributionAmount)}\n")
        if (socialFund > 0) sb.append("Mfuko wa Jamii: ${formatTsh(socialFund)}\n")
        sb.append("Mzunguko: Kila $frequencyLabel\n")
        sb.append("--------------------------------\n")
        sb.append("KIASI ANACHOPATA MPOKEAJI: ${formatTsh(netPayout)}\n")
        sb.append("Jumla ya Mfuko wa Jamii kwa Mzunguko: ${formatTsh(totalSocialFundCycle)}\n")
        sb.append("Muda wa Mzunguko Mzima: $memberCount $frequencyLabel\n")
        sb.append("Jumla Anayotoa Mwanachama kwa Mzunguko: ${formatTsh(contributionAmount + socialFund)}\n")
        sb.append("--------------------------------\n")
        sb.append("RATIBA YA MPOKEAJI:\n")
        membersList.forEach { m ->
            sb.append("Zunguko ${m.roundNumber}: ${m.name} [${if (m.isPaid) "Amelipwa" else "Bado"}]\n")
        }
        return sb.toString()
    }

    Scaffold(
        topBar = {
            MsaadaTopBar(
                title = if (isSwahili) "Kikoba Calculator" else "Kikoba Calculator",
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
                            text = if (isSwahili) "Hesabu & Ratiba" else "Calculate & Roster",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = if (isSwahili) "Vikundi (${savedGroups.size})" else "Groups (${savedGroups.size})",
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
                    // Result Overview Card
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("kikoba_result_card"),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MsaadaNavy)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text(
                                    text = if (isSwahili) "Kiasi Anachopata Anayepokea" else "Net Payout per Round",
                                    style = MaterialTheme.typography.titleSmall.copy(color = Color.White.copy(alpha = 0.8f))
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = formatTsh(netPayout),
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = if (isSwahili) "Jumla ya Mzunguko" else "Total Gross Pool",
                                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f))
                                        )
                                        Text(
                                            text = formatTsh(totalPoolGross),
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = if (isSwahili) "Muda wa Mzunguko Mzima" else "Cycle Duration",
                                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f))
                                        )
                                        Text(
                                            text = "$memberCount $frequencyLabel",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = MsaadaTeal
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Form Inputs Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (isSwahili) "Taarifa za Kikundi / Kikoba" else "Group Details",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = groupName,
                                    onValueChange = { groupName = it },
                                    label = { Text(if (isSwahili) "Jina la Kikundi" else "Group Name") },
                                    modifier = Modifier.fillMaxWidth().testTag("kikoba_group_name"),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = memberCountText,
                                        onValueChange = {
                                            val count = it.toIntOrNull() ?: 0
                                            if (count > 15 && !isProUser) {
                                                showProGate = true
                                            } else {
                                                memberCountText = it
                                            }
                                        },
                                        label = { Text(if (isSwahili) "Idadi ya Wanachama" else "Member Count") },
                                        modifier = Modifier.weight(1f).testTag("kikoba_member_count"),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true
                                    )

                                    OutlinedTextField(
                                        value = contributionText,
                                        onValueChange = { contributionText = it },
                                        label = { Text(if (isSwahili) "Mchango (TSh)" else "Contribution") },
                                        modifier = Modifier.weight(1f).testTag("kikoba_contribution_input"),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = socialFundText,
                                    onValueChange = { socialFundText = it },
                                    label = { Text(if (isSwahili) "Mfuko wa Jamii/Dharura (TSh)" else "Social / Emergency Fund") },
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = if (isSwahili) "Mzunguko wa Michango:" else "Contribution Frequency:",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf("wiki" to "Kila Wiki", "wiki2" to "Kila Wiki 2", "mwezi" to "Kila Mwezi").forEach { (key, label) ->
                                        FilterChip(
                                            selected = frequency == key,
                                            onClick = { frequency = key },
                                            label = { Text(label, fontSize = 11.sp) }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Member Roster Section
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (isSwahili) "Mfuatano wa Wanachama & Upokeaji" else "Member Roster & Payouts",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                membersList.forEachIndexed { index, m ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "#${m.roundNumber}",
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                            modifier = Modifier.width(28.dp)
                                        )
                                        OutlinedTextField(
                                            value = m.name,
                                            onValueChange = {
                                                membersList[index] = m.copy(name = it)
                                            },
                                            modifier = Modifier.weight(1f),
                                            singleLine = true
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        IconButton(
                                            onClick = {
                                                membersList[index] = m.copy(isPaid = !m.isPaid)
                                            }
                                        ) {
                                            Icon(
                                                imageVector = if (m.isPaid) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                                                contentDescription = "Hali ya Malipo",
                                                tint = if (m.isPaid) MsaadaTeal else Color.Gray
                                            )
                                        }
                                    }
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
                            Button(
                                onClick = {
                                    if (!isProUser) {
                                        showProGate = true
                                        return@Button
                                    }
                                    if (groupName.isBlank() || memberCount < 2 || memberCount > 30 || contributionAmount <= 0 || socialFund < 0) {
                                        Toast.makeText(
                                            context,
                                            if (isSwahili) "Weka wanachama 2–30 na kiasi sahihi cha mchango." else "Enter 2–30 members and valid contribution amounts.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@Button
                                    }
                                    val membersArr = JSONArray()
                                    membersList.forEach {
                                        val obj = JSONObject()
                                        obj.put("round", it.roundNumber)
                                        obj.put("name", it.name)
                                        obj.put("paid", it.isPaid)
                                        membersArr.put(obj)
                                    }

                                    val group = KikobaGroup(
                                        groupName = groupName,
                                        memberCount = memberCount,
                                        contributionAmount = contributionAmount,
                                        frequency = frequency,
                                        roundsCount = memberCount,
                                        totalPool = totalPoolGross,
                                        payoutPerMember = netPayout,
                                        membersJson = membersArr.toString()
                                    )

                                    viewModel.saveKikobaGroup(group)
                                    Toast.makeText(
                                        context,
                                        if (isSwahili) "Kikoba kimehifadhiwa kikamilifu!" else "Kikoba group saved!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MsaadaNavy),
                                modifier = Modifier.weight(1f).testTag("kikoba_save_btn")
                            ) {
                                Icon(Icons.Outlined.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isSwahili) "Hifadhi Kikoba" else "Save Group")
                            }

                            Button(
                                onClick = {
                                    val text = buildSummaryText()
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, "Ratiba ya Kikoba: $groupName")
                                        putExtra(Intent.EXTRA_TEXT, text)
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Shiriki Ratiba ya Kikoba"))
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MsaadaTeal),
                                modifier = Modifier.weight(1f).testTag("kikoba_share_btn")
                            ) {
                                Icon(Icons.Outlined.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isSwahili) "Shiriki Ratiba" else "Share Schedule")
                            }
                        }
                    }
                }
            } else {
                // Saved Groups Tab
                if (savedGroups.isEmpty()) {
                    EmptyState(
                        icon = Icons.Outlined.Groups,
                        title = if (isSwahili) "Hakuna Vikundi Vilivyohifadhiwa" else "No Saved Groups",
                        description = if (isSwahili) "Vikoba utakavyopanga na kuhifadhi vitaonekana hapa kwa ajili ya kufuatilia malipo na mizunguko." else "Saved Kikoba groups will appear here.",
                        actionButtonText = if (isSwahili) "Panga Kikoba Kipya" else "Create New Kikoba",
                        onActionClick = { selectedTab = 0 }
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(savedGroups) { _, g ->
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
                                                text = g.groupName,
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Text(
                                                text = "${g.memberCount} Wanachama | Mchango: ${formatTsh(g.contributionAmount)}",
                                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            )
                                            Text(
                                                text = "Upokeaji: ${formatTsh(g.payoutPerMember)}",
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = MsaadaTeal
                                                )
                                            )
                                        }

                                        IconButton(
                                            onClick = { viewModel.deleteKikobaGroup(g) }
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

    if (showProGate) {
        ProFeatureGateDialog(
            isSwahili = isSwahili,
            featureName = "Vikundi vya Wanachama Zaidi ya 15",
            onDismiss = { showProGate = false },
            onUpgrade = {
                showProGate = false
                onOpenPro()
            }
        )
    }
}
