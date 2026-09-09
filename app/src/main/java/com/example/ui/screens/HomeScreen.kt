package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.MsaadaCategory
import com.example.domain.MsaadaTool
import com.example.domain.StringsManager
import com.example.domain.ToolRegistry
import com.example.ui.components.CategoryCard
import com.example.ui.components.CategoryGridItem
import com.example.ui.components.EmptyState
import com.example.ui.components.MsaadaLogo
import com.example.ui.components.QuickToolCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.ToolCard
import com.example.ui.theme.MsaadaIcons
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaProGold
import com.example.ui.theme.MsaadaTeal

@Composable
fun HomeScreen(
    isSwahili: Boolean,
    recentTools: List<MsaadaTool>,
    favoriteToolIds: Set<String>,
    onToolClick: (MsaadaTool) -> Unit,
    onCategoryClick: (MsaadaCategory) -> Unit,
    onSearchClick: () -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onViewAllCategories: () -> Unit,
    onProClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val quickTools = ToolRegistry.tools.filter { it.isQuickTool }
    val categories = ToolRegistry.categories

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. Header & Brand
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MsaadaLogo(size = 38, showTagline = true, horizontal = true)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onProClick,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .size(38.dp)
                                .testTag("home_pro_badge_button")
                        ) {
                            Icon(
                                imageVector = MsaadaIcons.Pro,
                                contentDescription = "MSAADA PRO",
                                tint = MsaadaProGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Greeting
                Text(
                    text = if (isSwahili) "Karibu MSAADA" else "Welcome to MSAADA",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Search Bar Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                        .clickable(onClick = onSearchClick)
                        .padding(horizontal = 16.dp)
                        .testTag("home_search_bar"),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = MsaadaIcons.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (isSwahili) "Unatafuta nini?" else "What are you looking for?",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }

        // 2. Vifaa vya Haraka (Quick Tools)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 12.dp)
            ) {
                SectionHeader(
                    title = if (isSwahili) "Vifaa vya Haraka" else "Quick Tools",
                    actionText = if (isSwahili) "Tazama zote" else "View all",
                    onActionClick = onViewAllCategories,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    quickTools.take(4).forEach { tool ->
                        QuickToolCard(
                            tool = tool,
                            isSwahili = isSwahili,
                            onClick = { onToolClick(tool) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // 3. Featured: Passport Photo Banner
        item {
            val passportTool = ToolRegistry.getToolById("passport_photo")
            if (passportTool != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .clickable { onToolClick(passportTool) }
                        .testTag("home_passport_photo_banner"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MsaadaNavy
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(MsaadaTeal.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = MsaadaIcons.PassportPhoto,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isSwahili) "Passport Photo Generator" else "Passport Photo Generator",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(MsaadaTeal)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "NEW",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 9.sp
                                        )
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (isSwahili) "Unda picha za passport na karatasi ya kuchapa" else "Create passport photos and printable sheet",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.8f)
                                ),
                                maxLines = 1
                            )
                        }
                        Icon(
                            imageVector = MsaadaIcons.ChevronRight,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }

        // 4. Kategoria Grid
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                SectionHeader(
                    title = if (isSwahili) "Kategoria" else "Categories",
                    actionText = if (isSwahili) "Tazama zote" else "View all",
                    onActionClick = onViewAllCategories
                )

                // 2 rows of 5 or 2 columns
                val chunked = categories.chunked(5).firstOrNull() ?: emptyList()
                val secondChunk = categories.drop(5).take(5)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    chunked.forEach { cat ->
                        CategoryGridItem(
                            category = cat,
                            isSwahili = isSwahili,
                            onClick = { onCategoryClick(cat) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    secondChunk.forEach { cat ->
                        CategoryGridItem(
                            category = cat,
                            isSwahili = isSwahili,
                            onClick = { onCategoryClick(cat) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // 5. Zilizotumika Hivi Karibuni (Recently Used)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                SectionHeader(
                    title = if (isSwahili) "Zilizotumika Hivi Karibuni" else "Recently Used"
                )

                if (recentTools.isEmpty()) {
                    EmptyState(
                        icon = MsaadaIcons.HistoryIcon,
                        title = if (isSwahili) "Hakuna zana za hivi karibuni" else "No recent tools",
                        description = if (isSwahili)
                            "Zana utakazotumia zitaonekana hapa kwa ufikiaji wa haraka."
                        else
                            "Tools you open will appear here for fast access."
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        recentTools.take(4).forEach { tool ->
                            ToolCard(
                                tool = tool,
                                isSwahili = isSwahili,
                                isFavorite = favoriteToolIds.contains(tool.id),
                                onToolClick = { onToolClick(tool) },
                                onFavoriteToggle = { onFavoriteToggle(tool.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
