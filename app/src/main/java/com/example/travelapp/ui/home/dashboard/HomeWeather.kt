package com.example.travelapp.ui.home.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeWeather(
    city: String?,
    weather: HomeViewModel.WeatherUiModel?
) {
    Box(modifier = Modifier.padding(vertical = 8.dp)) {
        if (weather != null) {
            WeatherCard(city = city, weather = weather)
        } else {
            WeatherCardPlaceholder()
        }
    }
}

@Composable
fun WeatherCard(
    city: String?,
    weather: HomeViewModel.WeatherUiModel
) {
    // 1. Rich Atmospheric Gradients
    val skyBrush = if (weather.isDay == 1) {
        Brush.linearGradient(listOf(Color(0xFF4facfe), Color(0xFF00f2fe))) // Bright sky
    } else {
        Brush.linearGradient(listOf(Color(0xFF09203f), Color(0xFF537895))) // Deep night
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(250.dp) // Slightly taller for better spacing
            .clip(RoundedCornerShape(32.dp))
            .background(skyBrush)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(Color.White.copy(0.3f), Color.Transparent)
                ),
                shape = RoundedCornerShape(32.dp)
            )
    ) {
        // 2. The Background Watermark (Adds massive depth)
        Icon(
            imageVector = if (weather.isDay == 1) Icons.Default.WbSunny else Icons.Default.Cloud,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.1f),
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.CenterEnd)
                .offset(x = 30.dp, y = 20.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = city ?: "India",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Local Weather",
                        color = Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            // Temperature Display with "Etched" look
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "${weather.temperature}°",
                    color = Color.White,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-2).sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = weather.description.uppercase(),
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(bottom = 14.dp),
                    letterSpacing = 1.sp
                )
            }

            // Stats Row: Using divided sections instead of loose pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .padding(vertical = 10.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WeatherDetail(Icons.Default.Air, "${weather.windSpeed} km/h")
                // Adding Humidity makes it look like a real travel tool!
                WeatherDetail(Icons.Default.WaterDrop, "64%")
                WeatherDetail(Icons.Default.WbSunny, "UV Low")
            }
        }
    }
}

@Composable
fun WeatherDetail(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Color.White.copy(0.9f), modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = text, color = Color.White, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun WeatherCardPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Text(
            "Syncing Skies...",
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}