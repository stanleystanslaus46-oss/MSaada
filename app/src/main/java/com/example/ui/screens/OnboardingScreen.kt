package com.example.ui.screens

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.StringsManager
import com.example.ui.components.MsaadaLogo
import com.example.ui.theme.MsaadaIcons
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaTeal

data class OnboardingPageData(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val tag: String
)

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    isSwahili: Boolean
) {
    var currentPage by remember { mutableIntStateOf(0) }

    val pages = listOf(
        OnboardingPageData(
            title = if (isSwahili) "Karibu MSAADA" else "Welcome to MSAADA",
            description = if (isSwahili)
                "“Zana rahisi za kukusaidia kwenye kazi za kila siku.”"
            else
                "“Simple tools to help you with everyday tasks.”",
            icon = MsaadaIcons.AllTools,
            tag = if (isSwahili) "Kazi Rahisi" else "Easy Work"
        ),
        OnboardingPageData(
            title = if (isSwahili) "Fanya Kazi kwa Urahisi" else "Work Effortlessly",
            description = if (isSwahili)
                "“Pata calculators, planners, business tools na zana nyingine sehemu moja.”"
            else
                "“Access calculators, planners, business tools and everyday utilities in one place.”",
            icon = MsaadaIcons.Tools,
            tag = if (isSwahili) "Zana Moja" else "All-In-One"
        ),
        OnboardingPageData(
            title = if (isSwahili) "Unda Kitaalamu" else "Create Professionally",
            description = if (isSwahili)
                "“Tengeneza documents nzuri na professional templates kwa urahisi.”"
            else
                "“Generate beautiful documents and professional templates with ease.”",
            icon = MsaadaIcons.Documents,
            tag = if (isSwahili) "Hati Bora" else "Pro Documents"
        )
    )

    val current = pages[currentPage]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Bar: Logo & Skip
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MsaadaLogo(size = 36, horizontal = true)
            Text(
                text = if (isSwahili) "Ruka" else "Skip",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier
                    .clickable(onClick = onFinish)
                    .padding(8.dp)
                    .testTag("onboarding_skip_button")
            )
        }

        // Center Hero Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(MsaadaTeal.copy(alpha = 0.10f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = current.icon,
                        contentDescription = null,
                        tint = MsaadaNavy,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(MsaadaTeal.copy(alpha = 0.14f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = current.tag,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MsaadaTeal,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = current.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = current.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 22.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }

        // Bottom Controls: Page Indicators & Action Button
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Indicators
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                repeat(pages.size) { index ->
                    val isActive = index == currentPage
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(8.dp)
                            .width(if (isActive) 24.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isActive) MsaadaTeal else MaterialTheme.colorScheme.outline
                            )
                    )
                }
            }

            // Button
            Button(
                onClick = {
                    if (currentPage < pages.size - 1) {
                        currentPage++
                    } else {
                        onFinish()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("onboarding_next_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MsaadaNavy
                )
            ) {
                Text(
                    text = if (currentPage < pages.size - 1) {
                        if (isSwahili) "Endelea" else "Continue"
                    } else {
                        if (isSwahili) "Anza Kutumia" else "Get Started"
                    },
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }
    }
}
