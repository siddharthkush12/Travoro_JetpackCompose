package com.travoro.app.ui.home.mytrips

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.travoro.app.data.remote.dto.trips.TripDto
import com.travoro.app.ui.components.HomeBarHeaders
import com.travoro.app.ui.theme.TealCyan
import com.travoro.app.ui.theme.WarningYellow

@Composable
fun MyTripsScreen(
    myTripViewModel: MyTripsViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    onTripClick: (TripDto) -> Unit,
) {
    val uiState by myTripViewModel.uiState.collectAsStateWithLifecycle()
    val dynamicColor = MaterialTheme.colorScheme.onSurface

    LaunchedEffect(Unit) {
        myTripViewModel.fetchMyTrips()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HomeBarHeaders(
                title = "JOURNEY ARCHIVE",
                subtitle = "EXPEDITIONS IN PROGRESS",
                icon = Icons.Default.FlightTakeoff,
                topPadding = paddingValues.calculateTopPadding(),
            )

            Box(modifier = Modifier.weight(1f)) {
                when (uiState) {
                    is MyTripsViewModel.MyTripsState.Loading -> {
                        CircularProgressIndicator(
                            color = TealCyan,
                            strokeWidth = 2.dp,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(56.dp)
                                .fillMaxWidth(),
                        )
                    }

                    is MyTripsViewModel.MyTripsState.Success -> {
                        val trips = (uiState as MyTripsViewModel.MyTripsState.Success).trips

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 20.dp,
                                end = 20.dp,
                                top = 16.dp,
                                bottom = paddingValues.calculateBottomPadding() + 40.dp,
                            ),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                        ) {
                            items(trips) { trip ->
                                PremiumHeroTripCard(
                                    trip = trip,
                                    dynamicColor = dynamicColor,
                                    onClick = { onTripClick(trip) },
                                )
                            }
                        }
                    }

                    else -> Unit
                }
            }
        }
    }
}

@Composable
fun PremiumHeroTripCard(
    trip: TripDto,
    dynamicColor: Color,
    onClick: () -> Unit,
) {
    val statusColor = when (trip.status?.lowercase()) {
        "active" -> TealCyan
        "upcoming" -> WarningYellow
        else -> dynamicColor.copy(alpha = 0.3f)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        border = BorderStroke(1.dp, dynamicColor.copy(alpha = 0.06f)),
        shadowElevation = 6.dp,
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            ) {
                AsyncImage(
                    model = trip.coverImage,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.4f),
                                    Color.Black.copy(alpha = 0.9f),
                                ),
                                startY = 150f,
                            ),
                        ),
                )

                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    shape = RoundedCornerShape(50),
                    color = Color.Black.copy(alpha = 0.4f),
                    border = BorderStroke(0.5.dp, statusColor.copy(alpha = 0.3f)),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(statusColor),
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = trip.status?.uppercase() ?: "UNKNOWN",
                            style = TextStyle(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp,
                            ),
                            color = statusColor,
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp),
                ) {
                    Text(
                        text = trip.title.uppercase(),
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                        ),
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocationOn,
                            null,
                            tint = TealCyan,
                            modifier = Modifier.size(12.dp),
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = trip.destination?.uppercase() ?: "CLASSIFIED",
                            style = TextStyle(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                            ),
                            color = Color.White.copy(alpha = 0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }

            PremiumCardFooter(trip, dynamicColor)
        }
    }
}

@Composable
fun PremiumCardFooter(
    trip: TripDto,
    dynamicColor: Color,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = "EXPEDITION CREW",
                style = TextStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp,
                ),
                color = dynamicColor.copy(alpha = 0.4f),
            )

            Spacer(Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy((-12).dp),
            ) {
                trip.members.take(4).forEach { member ->
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                    ) {
                        AsyncImage(
                            model = member.user.profilePic,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(dynamicColor.copy(alpha = 0.1f)),
                            contentScale = ContentScale.Crop,
                        )
                    }
                }

                if (trip.members.size > 4) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(dynamicColor.copy(alpha = 0.05f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                "+${trip.members.size - 4}",
                                style = TextStyle(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TealCyan,
                                ),
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(dynamicColor.copy(alpha = 0.03f))
                .border(1.dp, dynamicColor.copy(alpha = 0.08f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null,
                tint = TealCyan,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}
