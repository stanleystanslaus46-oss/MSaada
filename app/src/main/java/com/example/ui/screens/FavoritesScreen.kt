package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.MsaadaTool
import com.example.domain.ToolRegistry
import com.example.ui.components.EmptyState
import com.example.ui.components.MsaadaTopBar
import com.example.ui.components.ToolCard
import com.example.ui.theme.MsaadaIcons

@Composable
fun FavoritesScreen(
    isSwahili: Boolean,
    favoriteToolIds: Set<String>,
    onToolClick: (MsaadaTool) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onExploreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val favoriteTools = ToolRegistry.tools.filter { favoriteToolIds.contains(it.id) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Vipendwa" else "Favorites"
        )

        if (favoriteTools.isEmpty()) {
            EmptyState(
                icon = MsaadaIcons.FavoritesBorder,
                title = if (isSwahili) "Bado hujaongeza vipendwa" else "No favorites added yet",
                description = if (isSwahili)
                    "Bofya alama ya moyo kwenye zana yoyote unayoipenda ili iweze kuonekana hapa kwa ufikiaji wa haraka."
                else
                    "Tap the heart icon on any tool you love to access it quickly from here.",
                actionButtonText = if (isSwahili) "Gundua Zana" else "Explore Tools",
                onActionClick = onExploreClick
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(favoriteTools) { tool ->
                    ToolCard(
                        tool = tool,
                        isSwahili = isSwahili,
                        isFavorite = true,
                        onToolClick = { onToolClick(tool) },
                        onFavoriteToggle = { onFavoriteToggle(tool.id) }
                    )
                }
            }
        }
    }
}
