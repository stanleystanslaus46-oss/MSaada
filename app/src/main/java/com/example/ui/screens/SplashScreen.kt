package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateNext: (destination: String) -> Unit,
    onboardingCompleted: Boolean
) {
    LaunchedEffect(Unit) {
        delay(1500)
        if (onboardingCompleted) {
            onNavigateNext("main_home")
        } else {
            onNavigateNext("onboarding")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.msaada_splash_screen),
            contentDescription = "MSAADA",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}
