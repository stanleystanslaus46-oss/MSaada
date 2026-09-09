package com.example.ui.screens.tools.church

import android.content.Intent
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChurchEventItem
import com.example.data.model.PrayerItem
import com.example.ui.components.MsaadaTopBar
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaTeal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// -------------------------------------------------------------
// 1. BIBLIA TAKATIFU (SW HIBLI BIBLE READER)
// -------------------------------------------------------------
data class BibleChapter(
    val book: String,
    val chapter: Int,
    val verses: List<String>
)

val sampleSwahiliBible = listOf(
    BibleChapter(
        book = "Zaburi",
        chapter = 23,
        verses = listOf(
            "Bwana ndiye mchungaji wangu, Sitapungukiwa na kitu.",
            "Katika malisho ya majani mabichi hunilaza, Kando ya maji ya utulivu huniongoza.",
            "Hunihuisha nafsi yangu; Huniongoza katika njia za haki kwa ajili ya jina lake.",
            "Naam, nijapopita kati ya bonde la uvuli wa mauti, Sitaogopa mabaya; Kwa maana Wewe upo pamoja nami, Fimbo yako na gongo lako vyanifariji.",
            "Waandaa meza mbele yangu, Machoni pa watesi wangu. Umenipaka mafuta kichwani pangu, Na kikombe changu kinafurika.",
            "Hakika wema na fadhili zitanifuata Siku zote za maisha yangu; Nami nitakaa nyumbani mwa Bwana milele."
        )
    ),
    BibleChapter(
        book = "Yohana",
        chapter = 3,
        verses = listOf(
            "Palikuwa na mtu mmoja wa Mafarisayo, jina lake Nikodemo, mkuu wa Wayahudi.",
            "Huyo alimjia usiku, akamwambia, Rabi, twajua ya kuwa u mwalimu uliyetoka kwa Mungu; kwa maana hakuna mtu awezaye kuzifanya ishara hizi uzifanyazo wewe, isipokuwa Mungu yupo pamoja naye.",
            "Yesu akajibu, akamwambia, Amin, amin, nakuambia, Mtu asipozaliwa mara ya pili, hawezi kuuona ufalme wa Mungu.",
            "Nikodemo akamwambia, Awezaje mtu kuzaliwa, akiwa mzee? Aweza kuingia tumboni mwa mamaye mara ya pili akazaliwa?",
            "Yesu akajibu, Amin, amin, nakuambia, Mtu asipozaliwa kwa maji na kwa Roho, hawezi kuuingia ufalme wa Mungu.",
            "Kwa maana jinsi hii Mungu aliupenda ulimwengu, hata akamtoa Mwanawe pekee, ili kila mtu amwaminiye asipotee, bali awe na uzima wa milele.",
            "Maana Mungu hakumtuma Mwana ulimwenguni ili auhukumu ulimwengu, bali ulimwengu uokolewe katika yeye."
        )
    ),
    BibleChapter(
        book = "Mithali",
        chapter = 3,
        verses = listOf(
            "Mwanangu, usiisahau sheria yangu, Bali moyo wako uzishike amri zangu.",
            "Maana zitakuongezea siku za wingi, Na miaka ya uzima, na amani.",
            "Rehema na kweli zisifarakane nawe; Zifunge shingoni mwako; Ziandike juu ya kibao cha moyo wako.",
            "Ndivyo utakavyopata kibali na akili nzuri, Mbele za Mungu na mbele ya wanadamu.",
            "Mtumaini Bwana kwa moyo wako wote, Wala usizitegemee akili zako mwenyewe.",
            "Katika njia zako zote mkiri yeye, Naye atayanyosha mapito yako.",
            "Usiwe mwenye hekima machoni pako; Mche Bwana, ukajiepushe na uovu."
        )
    ),
    BibleChapter(
        book = "Mathayo",
        chapter = 6,
        verses = listOf(
            "Basi ninyi salini hivi: Baba yetu uliye mbinguni, Jina lako litukuzwe,",
            "Ufalme wako uje, Mapenzi yako yatimizwe, hapa duniani kama huko mbinguni.",
            "Utupe leo riziki yetu ya kila siku.",
            "Utusamehe deni zetu, kama sisi nasi tuwasamehevyo wadeni wetu.",
            "Na usitutie majaribuni, lakini utuokoe na yule mwovu. Kwa kuwa ufalme ni wako, na nguvu, na utukufu, hata milele. Amina."
        )
    ),
    BibleChapter(
        book = "Warumi",
        chapter = 8,
        verses = listOf(
            "Sasa, basi, hakuna hukumu ya adhabu juu yao walio katika Kristo Yesu.",
            "Kwa maana sheria ya Roho wa uzima ule ulio katika Kristo Yesu imeniacha huru mbali na sheria ya dhambi na mauti.",
            "Nasi twajua ya kuwa katika mambo yote Mungu hufanya kazi pamoja na wale wampendao katika kuwapatia mema, yaani, wale walioitwa kwa kusudi lake.",
            "Basi, tuseme nini juu ya hayo? Mungu akiwa upande wetu, ni nani aliye juu yetu?",
            "Ni nani atakayetutenga na upendo wa Kristo? Je! Ni dhiki, au shida, au adha, au njaa, au uchi, au hatari, au upanga?",
            "Lakini katika mambo hayo yote tunashinda, na zaidi ya kushinda, kwa yeye aliyetupenda."
        )
    )
)

@Composable
fun BibleReaderScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var selectedBookIndex by remember { mutableIntStateOf(0) }
    var fontSizeMultiplier by remember { mutableFloatStateOf(16f) }
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    val currentChapter = sampleSwahiliBible[selectedBookIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Biblia Takatifu (Kiswahili)" else "Holy Bible (Swahili)",
            onBack = onBack,
            actions = {
                Row(modifier = Modifier.padding(end = 8.dp)) {
                    IconButton(onClick = { fontSizeMultiplier = (fontSizeMultiplier - 2).coerceAtLeast(12f) }) {
                        Text(text = "A-", fontWeight = FontWeight.Bold, color = MsaadaNavy)
                    }
                    IconButton(onClick = { fontSizeMultiplier = (fontSizeMultiplier + 2).coerceAtMost(26f) }) {
                        Text(text = "A+", fontWeight = FontWeight.Bold, color = MsaadaNavy)
                    }
                }
            }
        )

        ScrollableTabRow(
            selectedTabIndex = selectedBookIndex,
            containerColor = MaterialTheme.colorScheme.surface,
            edgePadding = 16.dp
        ) {
            sampleSwahiliBible.forEachIndexed { index, ch ->
                Tab(
                    selected = selectedBookIndex == index,
                    onClick = { selectedBookIndex = index },
                    text = { Text(text = "${ch.book} ${ch.chapter}") }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MsaadaTeal.copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MenuBook,
                            contentDescription = null,
                            tint = MsaadaTeal,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "${currentChapter.book} Sura ${currentChapter.chapter}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MsaadaNavy)
                            )
                            Text(
                                text = if (isSwahili) "Bofya mstari wowote kunakili au kushiriki." else "Tap any verse to copy or share.",
                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }
                }
            }

            items(currentChapter.verses.size) { idx ->
                val verseNum = idx + 1
                val verseText = currentChapter.verses[idx]

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            val shareText = "“$verseText” — ${currentChapter.book} ${currentChapter.chapter}:$verseNum (MSAADA Biblia)"
                            clipboard.setText(AnnotatedString(shareText))
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Shiriki Neno"))
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "$verseNum",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MsaadaTeal
                            ),
                            modifier = Modifier.width(28.dp)
                        )
                        Text(
                            text = verseText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = fontSizeMultiplier.sp,
                                lineHeight = (fontSizeMultiplier * 1.4f).sp
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. NYIMBO ZA WOKOVU / NYIMBO ZA KRISTO (HYMN BOOK)
// -------------------------------------------------------------
data class HymnSong(
    val number: Int,
    val title: String,
    val chorus: String,
    val stanzas: List<String>
)

val sampleHymns = listOf(
    HymnSong(
        number = 1,
        title = "Mwamba Wenye Imara",
        chorus = "",
        stanzas = listOf(
            "Mwamba wenye imara, Kwako nitajificha; Maji nayo damu pia, Yaliyotoka ubavuni; Yaponye roho yangu, Na kunisafisha kabisa.",
            "Kazi zangu zote pia, Hazikidhi sheria; Nijapolia daima, Kujitahidi sana; Hayafuti dhambi zangu; Wewe tu unayeokoa.",
            "Sina cha mkononi, Msalabani naja; Nili uchi, nivike, Mnyonge, nitegemeze; Nichafu, naja kwako, Nioshe, nisife kamwe."
        )
    ),
    HymnSong(
        number = 2,
        title = "Yesu Kwetu ni Rafiki",
        chorus = "",
        stanzas = listOf(
            "Yesu kwetu ni Rafiki, Huchukua dhambi pia; Ni fadhili tukimwomba, Mungu kutosikia; Mara nyingi twajitenga, Na amani ya moyoni; Sababu hatukumwomba, Mungu kwa njia ya maombi.",
            "Je, unayo majaribu, Una mashaka moyoni? Usivunjike moyo kamwe, Mpe Yesu maombini; Je, yupo rafiki mwaminifu, Atakayebeba huzuni? Yesu anajua yote, Mpe Yeye maombini."
        )
    ),
    HymnSong(
        number = 3,
        title = "Salama Rohoni Mwangu",
        chorus = "Salama rohoni, Salama rohoni mwangu.",
        stanzas = listOf(
            "Amani kama mto inaponijaza, Au huzuni zikivuma kama mawimbi; Lolote litakalonipata, umenifundisha kusema: Ni salama, ni salama rohoni mwangu.",
            "Ingawa Shetani atajaribu, na majaribu yakija, Wacha hakika hii yenye heri idumu; Kristo ameona hali yangu ya unyonge, Na amemwaga damu yake kwa ajili ya nafsi yangu."
        )
    ),
    HymnSong(
        number = 4,
        title = "Cha Kutumaini Sina",
        chorus = "Kwenye mwamba nimesimama, Ngome nyingine zote ni mchanga.",
        stanzas = listOf(
            "Cha kutumaini sina, Ila damu yake Bwana; Sina wema wa kutosha, Dhambi zangu kuziosha.",
            "Damu yake na haki yake, Ndiyo msaada wangu pekee; Dhoruba inapovuma, Nanga yangu imezama ndani Yake."
        )
    ),
    HymnSong(
        number = 5,
        title = "Neema ya Ajabu (Amazing Grace)",
        chorus = "",
        stanzas = listOf(
            "Neema ya ajabu jinsi gani, Iliyoniokoa mimi! Nilipotea, sasa nimepatikana; Nilikuwa kipofu, sasa naona.",
            "Neema hiyo ilinifundisha kuogopa, Na neema ikaondoa woga wangu; Jinsi neema hiyo ilivyo ya thamani, Saa ile niliyoamini kwanza!"
        )
    )
)

@Composable
fun HymnsScreen(
    isSwahili: Boolean,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedHymn by remember { mutableStateOf<HymnSong?>(null) }

    val filteredHymns = sampleHymns.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
        it.number.toString().contains(searchQuery)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Kitabu cha Nyimbo za Wokovu" else "Hymns Book",
            onBack = {
                if (selectedHymn != null) selectedHymn = null else onBack()
            }
        )

        if (selectedHymn == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(if (isSwahili) "Tafuta wimbo kwa namba au jina..." else "Search hymn by number or title...") },
                    leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = null, tint = MsaadaTeal) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredHymns) { hymn ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { selectedHymn = hymn },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(MsaadaTeal.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "#${hymn.number}",
                                        fontWeight = FontWeight.Bold,
                                        color = MsaadaTeal,
                                        fontSize = 14.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = hymn.title,
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = hymn.stanzas.firstOrNull()?.take(45)?.plus("...") ?: "",
                                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // View selected hymn
            val hymn = selectedHymn!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Text(
                    text = "#${hymn.number} - ${hymn.title}",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = MsaadaNavy
                    )
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp))

                if (hymn.chorus.isNotBlank()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MsaadaTeal.copy(alpha = 0.1f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "KIPEKEE (CHORUS):", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = MsaadaTeal))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = hymn.chorus, style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic, fontWeight = FontWeight.SemiBold))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                hymn.stanzas.forEachIndexed { idx, stanza ->
                    Column(modifier = Modifier.padding(bottom = 18.dp)) {
                        Text(
                            text = "Ubeti ${idx + 1}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = MsaadaTeal)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stanza,
                            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 26.sp)
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 3. PRAYER REQUESTS SCREEN (MAOMBI NA SALA ZANGU)
// -------------------------------------------------------------
@Composable
fun PrayerRequestsScreen(
    prayers: List<PrayerItem>,
    isSwahili: Boolean,
    onAddPrayer: (title: String, description: String, date: String) -> Unit,
    onToggleAnswered: (PrayerItem) -> Unit,
    onDeletePrayer: (PrayerItem) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Daftari la Maombi & Sala" else "Prayer Requests",
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
                            text = if (isSwahili) "Weka Ombi au Sala Yako" else "Add Your Prayer Request",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            placeholder = { Text(if (isSwahili) "Kichwa cha Ombi (mfano: Uponyaji wa Mama...)" else "Prayer Title") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            placeholder = { Text(if (isSwahili) "Maelezo ya ziada ya sala..." else "Details / Scripture reference...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            minLines = 2
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (title.isNotBlank()) {
                                    onAddPrayer(
                                        title.trim(),
                                        description.trim(),
                                        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
                                    )
                                    title = ""
                                    description = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MsaadaTeal)
                        ) {
                            Text(text = if (isSwahili) "Hifadhi Ombi Langu" else "Save Prayer")
                        }
                    }
                }
            }

            items(prayers) { prayer ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onToggleAnswered(prayer) },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (prayer.isAnswered) Color(0xFFD1FAE5) else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                imageVector = if (prayer.isAnswered) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (prayer.isAnswered) Color(0xFF059669) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = prayer.title,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (prayer.isAnswered) Color(0xFF065F46) else MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                if (prayer.description.isNotBlank()) {
                                    Text(
                                        text = prayer.description,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = if (prayer.isAnswered) Color(0xFF047857) else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                                Text(
                                    text = if (prayer.isAnswered) (if (isSwahili) "Mungu Amejibu!" else "Answered!") else prayer.date,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (prayer.isAnswered) Color(0xFF059669) else MsaadaTeal,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        IconButton(onClick = { onDeletePrayer(prayer) }) {
                            Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 4. CHURCH ANNOUNCEMENTS SCREEN
// -------------------------------------------------------------
@Composable
fun ChurchAnnouncementsScreen(
    announcements: List<ChurchEventItem>,
    isSwahili: Boolean,
    onAddAnnouncement: (title: String, date: String, time: String, location: String, notes: String) -> Unit,
    onDeleteAnnouncement: (ChurchEventItem) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var dateString by remember { mutableStateOf("Jumapili Ijayo") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Matangazo ya Kanisa" else "Church Announcements",
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
                            text = if (isSwahili) "Chapisha Tangazo Jipya" else "Add New Announcement",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            placeholder = { Text(if (isSwahili) "Kichwa (mfano: Semina ya Wanandoa)" else "Title (e.g. Couples Seminar)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = dateString,
                            onValueChange = { dateString = it },
                            placeholder = { Text(if (isSwahili) "Tarehe au Siku (mfano: Jumamosi 10:00 Asubuhi)" else "Date / Time") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = details,
                            onValueChange = { details = it },
                            placeholder = { Text(if (isSwahili) "Maelezo ya tangazo..." else "Announcement details...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            minLines = 2
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (title.isNotBlank()) {
                                    onAddAnnouncement(
                                        title.trim(),
                                        dateString.trim(),
                                        "10:00 AM",
                                        "Kanisani",
                                        details.trim()
                                    )
                                    title = ""
                                    details = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MsaadaNavy)
                        ) {
                            Text(text = if (isSwahili) "Weka Tangazo" else "Post Announcement")
                        }
                    }
                }
            }

            items(announcements) { ann ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = ann.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MsaadaNavy)
                            )
                            IconButton(onClick = { onDeleteAnnouncement(ann) }) {
                                Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Event,
                                contentDescription = null,
                                tint = MsaadaTeal,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = ann.date,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = MsaadaTeal)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = ann.notes.ifBlank { ann.location },
                            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface)
                        )
                    }
                }
            }
        }
    }
}
