package com.travoro.app.ui.home.features.sos

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.travoro.app.ui.components.CustomTopBar
import com.travoro.app.ui.theme.ErrorRed
import com.travoro.app.ui.theme.SuccessGreen
import com.travoro.app.ui.utils.TripLocationService

@Composable
fun SosScreen(
    onNavigateBack: () -> Unit
) {
    val dynamicColor = MaterialTheme.colorScheme.onSurface
    var isTriggered by remember { mutableStateOf(false) }
    val liveLocationRunning = TripLocationService.isRunning

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            CustomTopBar(
                title = "EMERGENCY PROTOCOL",
                icon = Icons.Rounded.Warning,
                onBackClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth(),
                subtitle = "CRITICAL RESPONSE SYSTEM",
                subTitleSize = 11.sp,
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 24.dp,
                    end = 24.dp,
                    top = 20.dp,
                    bottom = 100.dp,
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    EnableLiveLocationCard(liveLocationRunning, dynamicColor)
                }

                item {
                    Box(
                        modifier = Modifier.size(240.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(220.dp)
                                .clip(CircleShape)
                                .border(1.dp, ErrorRed.copy(alpha = 0.2f), CircleShape)
                                .background(ErrorRed.copy(alpha = 0.03f)),
                        )
                        Box(
                            modifier = Modifier
                                .size(190.dp)
                                .clip(CircleShape)
                                .border(2.dp, ErrorRed.copy(alpha = 0.4f), CircleShape)
                                .background(ErrorRed.copy(alpha = 0.05f)),
                        )

                        Surface(
                            modifier = Modifier
                                .size(160.dp)
                                .clickable { isTriggered = !isTriggered },
                            shape = CircleShape,
                            color = ErrorRed.copy(alpha = 0.1f),
                            border = BorderStroke(2.dp, ErrorRed),
                            shadowElevation = if (isTriggered) 0.dp else 12.dp,
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isTriggered) ErrorRed else ErrorRed.copy(
                                            alpha = 0.2f
                                        )
                                    ),
                                contentAlignment = Alignment.Center,
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.CellTower,
                                        contentDescription = null,
                                        tint = if (isTriggered) Color.White else ErrorRed,
                                        modifier = Modifier.size(36.dp),
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        text = if (isTriggered) "BROADCASTING" else "INITIATE\nS.O.S",
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 2.sp,
                                        ),
                                        color = if (isTriggered) Color.White else ErrorRed,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(50.dp))
                }

                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "LOCAL AUTHORITIES (INDIA)",
                            style = TextStyle(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.5.sp,
                            ),
                            color = dynamicColor.copy(alpha = 0.4f),
                        )
                        Spacer(Modifier.height(12.dp))

                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                AuthorityButton(
                                    "POLICE",
                                    "100",
                                    Icons.Rounded.LocalPolice,
                                    ErrorRed,
                                    Modifier.weight(1f),
                                )

                                AuthorityButton(
                                    "MEDICAL",
                                    "108",
                                    Icons.Rounded.MedicalServices,
                                    ErrorRed,
                                    Modifier.weight(1f),
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                AuthorityButton(
                                    "FIRE",
                                    "101",
                                    Icons.Rounded.FireTruck,
                                    ErrorRed,
                                    Modifier.weight(1f),
                                )

                                AuthorityButton(
                                    "EMERGENCY",
                                    "112",
                                    Icons.Rounded.Emergency,
                                    ErrorRed,
                                    Modifier.weight(1f),
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun EnableLiveLocationCard(
    liveLocationStatus: Boolean,
    dynamicColor: Color,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = ErrorRed.copy(alpha = 0.05f),
        border = BorderStroke(1.dp, ErrorRed.copy(alpha = 0.2f)),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(ErrorRed.copy(alpha = 0.1f))
                    .border(1.dp, ErrorRed.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    if (liveLocationStatus) Icons.Rounded.LocationOn else Icons.Rounded.LocationOff,
                    contentDescription = null,
                    tint = if (liveLocationStatus) SuccessGreen else ErrorRed.copy(alpha = 0.3f),
                    modifier = Modifier.size(20.dp),
                )
            }

            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    text = "LIVE LOCATION",
                    style = TextStyle(
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp,
                    ),
                    color = ErrorRed.copy(alpha = 0.8f),
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = if (liveLocationStatus) "Your trip members can track you" else "Location sharing is disabled",
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                    ),
                    color = dynamicColor,
                )
            }
        }
    }
}

fun callNumber(
    context: Context,
    number: String,
) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = "tel:$number".toUri()
    context.startActivity(intent)
}

@Composable
fun AuthorityButton(
    title: String,
    number: String,
    icon: ImageVector,
    ErrorRed: Color,
    modifier: Modifier,
) {
    val context = LocalContext.current
    Surface(
        modifier = modifier.clickable {
            callNumber(
                context = context,
                number = number,
            )
        },
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(icon, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(28.dp))
            Spacer(Modifier.height(12.dp))
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = number,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Black),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
