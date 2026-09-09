package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.domain.MsaadaTool
import com.example.domain.ToolRegistry
import com.example.ui.components.EmptyState
import com.example.ui.components.MsaadaTopBar
import com.example.ui.components.ToolCard
import com.example.ui.theme.MsaadaIcons

@Composable
fun SearchScreen(
    isSwahili: Boolean,
    favoriteToolIds: Set<String>,
    onToolClick: (MsaadaTool) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onBack: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val results = if (query.isBlank()) emptyList() else ToolRegistry.searchTools(query)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Tafuta Zana" else "Search Tools",
            onBack = onBack
        )

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("search_screen_input"),
            placeholder = {
                Text(
                    text = if (isSwahili) "Tafuta zana (mfano: bajeti, invoice, zaka...)"
                    else "Search tools (e.g., budget, invoice, tithe...)"
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = MsaadaIcons.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(
                            imageVector = MsaadaIcons.Close,
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            singleLine = true
        )

        if (query.isBlank()) {
            EmptyState(
                icon = MsaadaIcons.Search,
                title = if (isSwahili) "Anza kutafuta" else "Start searching",
                description = if (isSwahili)
                    "Andika jina la zana au neno lolote linalohusiana na kazi yako."
                else
                    "Type a tool name or keyword related to your task."
            )
        } else if (results.isEmpty()) {
            EmptyState(
                icon = MsaadaIcons.SearchOff,
                title = if (isSwahili) "Hakuna zana iliyopatikana" else "No tools found",
                description = if (isSwahili)
                    "Jaribu kutafuta kwa neno lingine au angalia kategoria zetu."
                else
                    "Try searching with another keyword or explore our categories."
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = if (isSwahili) "Matokeo ${results.size}" else "${results.size} results found",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                items(results) { tool ->
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
