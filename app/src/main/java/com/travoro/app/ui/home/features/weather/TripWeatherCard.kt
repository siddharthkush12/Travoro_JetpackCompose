package com.travoro.app.ui.home.features.weather

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.BlurOn
import androidx.compose.material.icons.rounded.Umbrella
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.travoro.app.data.remote.dto.weather.AlertUiModel
import com.travoro.app.data.remote.dto.weather.ForecastUiModel
import com.travoro.app.data.remote.dto.weather.WeatherUiModel
import com.travoro.app.ui.theme.ErrorRed
import com.travoro.app.ui.theme.TealCyan
import com.travoro.app.ui.theme.TealCyanLight
import com.travoro.app.ui.theme.WarningYellow

@Composable
fun TripWeatherSection(
    city: String?,
    tripWeatherViewModel: TripWeatherViewModel,
) {
    val uiState by tripWeatherViewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        when (uiState) {
            is TripWeatherViewModel.WeatherEvent.Loading -> {
                WeatherCardPlaceholder()
            }

            is TripWeatherViewModel.WeatherEvent.Success -> {
                val weather = (uiState as TripWeatherViewModel.WeatherEvent.Success).weather

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    WeatherHeader(weather = weather)

                    if (weather.alerts.isNotEmpty()) {
                        WeatherAlertsSection(alerts = weather.alerts)
                    }
                    if (weather.forecast.isNotEmpty()) {
                        ExpandedForecastTimeline(forecast = weather.forecast)
                    }
                }
            }

            else -> WeatherCardPlaceholder()
        }
    }
}

@Composable
fun WeatherHeader(weather: WeatherUiModel) {
    val dynamicColor = MaterialTheme.colorScheme.onSurface
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "dot",
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(110.dp),
        shape = RoundedCornerShape(24.dp),
        color = dynamicColor.copy(alpha = 0.09f),
        border = BorderStroke(1.dp, dynamicColor.copy(alpha = 0.2f)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(TealCyan.copy(alpha = 0.3f), Color.Transparent),
                        center = Offset(Float.POSITIVE_INFINITY, 0f),
                        radius = 500f,
                    ),
                ),
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = TealCyan,
                        modifier = Modifier.size(14.dp),
                    )

                    Spacer(Modifier.width(5.dp))

                    Text(
                        text = weather.city.uppercase(),
                        style = TextStyle(
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            letterSpacing = 2.sp,
                        ),
                        color = dynamicColor.copy(alpha = 0.9f),
                    )
                }

                Spacer(Modifier.height(5.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(
                                TealCyan.copy(alpha = dotAlpha),
                            ),
                    )
                    Spacer(Modifier.width(6.dp))

                    Text(
                        "CURRENT ATMOSPHERE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealCyan,
                        letterSpacing = 1.sp,
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        "${weather.todayTemp.toInt()}°",
                        style = TextStyle(fontSize = 36.sp, fontWeight = FontWeight.Black),
                        color = dynamicColor,
                    )
                }
                Spacer(Modifier.width(8.dp))

                AsyncImage(
                    model = weather.todayIcon,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.FillBounds,
                )
            }
        }
    }
}

enum class TravelVerdict(val label: String, val color: Color) {
    OPTIMAL("OPTIMAL", TealCyan), CAUTION("CAUTION", WarningYellow), WARNING("HAZARDOUS", ErrorRed),
}

fun analyzeTravelConditions(
    rainChance: Int,
    minTemp: Double,
    maxTemp: Double,
): TravelVerdict {
    return when {
        rainChance >= 80 || minTemp <= -5.0 || maxTemp >= 40.0 -> TravelVerdict.WARNING
        rainChance in 30..79 || minTemp in -4.0..5.0 -> TravelVerdict.CAUTION
        else -> TravelVerdict.OPTIMAL
    }
}

@Composable
fun ExpandedForecastTimeline(forecast: List<ForecastUiModel>) {
    val dynamicColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "7-DAY INTELLIGENCE BRIEF",
                style = TextStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                ),
                color = dynamicColor.copy(alpha = 0.4f),
            )
            Icon(
                Icons.Default.Air,
                contentDescription = null,
                tint = dynamicColor.copy(alpha = 0.2f),
                modifier = Modifier.size(14.dp),
            )
        }

        forecast.forEach { day ->

            val verdict = analyzeTravelConditions(day.rainChance, day.minTemp, day.maxTemp)

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = dynamicColor.copy(alpha = 0.03f),
                border = BorderStroke(1.dp, verdict.color.copy(alpha = 0.2f)),
            ) {
                Box(
                    modifier = Modifier.background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                verdict.color.copy(alpha = 0.05f), Color.Transparent
                            ),
                        ),
                    ),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.width(60.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(verdict.color),
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                day.date.uppercase(),
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                color = dynamicColor.copy(alpha = 0.9f),
                                letterSpacing = 1.sp,
                            )
                        }

                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AsyncImage(
                                model = day.icon,
                                contentDescription = null,
                                modifier = Modifier.size(38.dp),
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = day.condition,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = dynamicColor.copy(alpha = 0.6f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                lineHeight = 14.sp,
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "SYS: ${verdict.label}",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = verdict.color,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(bottom = 4.dp),
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (day.rainChance > 0) {
                                    Icon(
                                        Icons.Rounded.Umbrella,
                                        contentDescription = "Rain",
                                        tint = TealCyanLight,
                                        modifier = Modifier.size(10.dp),
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        "${day.rainChance}%",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = TealCyan,
                                    )
                                    Spacer(Modifier.width(10.dp))
                                }
                                Text(
                                    "${day.maxTemp.toInt()}°",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp,
                                    color = dynamicColor,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherAlertsSection(alerts: List<AlertUiModel>) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "CRITICAL SYSTEM ALERTS",
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
            ),
            color = ErrorRed,
        )

        alerts.forEach { alert ->
            Surface(
                color = ErrorRed.copy(alpha = 0.05f),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, ErrorRed.copy(alpha = 0.3f)),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Alert",
                            tint = ErrorRed,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = alert.headline.uppercase(),
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            color = ErrorRed,
                            letterSpacing = 0.5.sp,
                            lineHeight = 16.sp,
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AlertBadge("SEVERITY: ${alert.severity.uppercase()}")
                        AlertBadge("URGENCY: ${alert.urgency.uppercase()}")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "IMPACTED ZONES: ${alert.areas}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        lineHeight = 14.sp,
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = alert.description,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        lineHeight = 18.sp,
                    )

                    if (!alert.instruction.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            color = ErrorRed.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            Text(
                                text = ">> PROTOCOL: ${alert.instruction.uppercase()}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = ErrorRed,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                letterSpacing = 0.5.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AlertBadge(text: String) {
    Surface(
        color = Color.Transparent,
        border = BorderStroke(1.dp, ErrorRed.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(4.dp),
    ) {
        Text(
            text = text,
            fontSize = 8.sp,
            fontWeight = FontWeight.Black,
            color = ErrorRed,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
            letterSpacing = 1.sp,
        )
    }
}

@Composable
fun WeatherCardPlaceholder() {
    val dynamicColor = MaterialTheme.colorScheme.onSurface
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .height(110.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(dynamicColor.copy(alpha = 0.05f))
            .border(1.dp, dynamicColor.copy(alpha = 0.1f), RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Rounded.BlurOn,
                contentDescription = null,
                tint = TealCyan.copy(alpha = 0.5f),
                modifier = Modifier.size(32.dp),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "SYNCING ATMOSPHERE...",
                color = dynamicColor.copy(alpha = 0.4f),
                fontWeight = FontWeight.Black,
                fontSize = 10.sp,
                letterSpacing = 2.sp,
            )
        }
    }
}
