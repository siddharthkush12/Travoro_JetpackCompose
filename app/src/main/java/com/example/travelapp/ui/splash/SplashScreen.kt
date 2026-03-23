package com.example.travelapp.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.travelapp.R
import com.example.travelapp.di.Session
import com.example.travelapp.rootNavigation.Home
import com.example.travelapp.rootNavigation.Onboarding
import com.example.travelapp.ui.theme.MidnightBlue
import com.example.travelapp.ui.theme.TealCyan
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    session: Session, onNavigate: (Any) -> Unit
) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.travoro))
    val zoomScale = remember { Animatable(1f) }
    val fadeAlpha = remember { Animatable(1f) }
    val topColor = MidnightBlue
    val middleColor = TealCyan.copy(0.5f)
    val bottomColor = MidnightBlue


    val progress by animateLottieCompositionAsState(
        composition = composition, iterations = 1
    )

    LaunchedEffect(progress) {
        if (progress == 1f) {
            delay(200)

            val zoomJob = async {
                zoomScale.animateTo(
                    targetValue = 15f,
                    animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
                )
            }
            val fadeJob = async {
                fadeAlpha.animateTo(
                    targetValue = 0f, animationSpec = tween(durationMillis = 500)
                )
            }

            zoomJob.await()
            fadeJob.await()

            val destination = if (session.getToken().isNullOrBlank()) Onboarding else Home
            onNavigate(destination)
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(topColor, middleColor, bottomColor)
                )
            ), contentAlignment = Alignment.Center
    ) {

        LottieAnimation(
            composition = composition,
            progress = { progress },
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(350.dp)
                .graphicsLayer {
                    scaleX = zoomScale.value
                    scaleY = zoomScale.value
                    alpha = fadeAlpha.value
                })

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp)
                .graphicsLayer {
                    alpha = fadeAlpha.value
                }, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = TealCyan, strokeWidth = 2.5.dp, modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Discovering destinations...",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp
            )
        }
    }
}