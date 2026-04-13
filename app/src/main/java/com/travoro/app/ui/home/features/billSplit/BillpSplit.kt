package com.travoro.app.ui.home.features.billSplit

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
import androidx.compose.material.icons.automirrored.rounded.ReceiptLong
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.BlurOn
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.travoro.app.data.remote.dto.trips.TripDto
import com.travoro.app.ui.components.CustomErrorMessage
import com.travoro.app.ui.components.CustomTopBar
import com.travoro.app.ui.home.mytrips.MyTripsViewModel
import com.travoro.app.ui.theme.TealCyan

@Composable
fun BillSplitScreen(
    onTripClick: (TripDto) -> Unit,
    homeNavController: NavController,
    viewModel: MyTripsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dynamicColor = MaterialTheme.colorScheme.onSurface

    LaunchedEffect(Unit) {
        viewModel.fetchMyTrips()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            CustomTopBar(
                title = "FINANCIAL LEDGER",
                subtitle = "SELECT A JOURNEY TO INITIATE BILL SPLIT PROTOCOL",
                icon = Icons.AutoMirrored.Rounded.ReceiptLong,
                onBackClick = { homeNavController.popBackStack() },
                subTitleSize = 11.sp,
            )
            Spacer(modifier = Modifier.size(10.dp))

            when (uiState) {
                is MyTripsViewModel.MyTripsState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        SyncingLedgerPlaceholder()
                    }
                }

                is MyTripsViewModel.MyTripsState.Success -> {
                    val trips = (uiState as MyTripsViewModel.MyTripsState.Success).trips
                    if (trips.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                "NO ACTIVE JOURNEYS FOUND",
                                color = dynamicColor.copy(alpha = 0.4f),
                                letterSpacing = 2.sp,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                            )
                        }
                    } else {
                        TripsList(
                            trips = trips,
                            onTripClick = onTripClick,
                            dynamicColor = dynamicColor,
                        )
                    }
                }

                is MyTripsViewModel.MyTripsState.Error -> {
                    CustomErrorMessage(
                        message = (uiState as MyTripsViewModel.MyTripsState.Error).message.uppercase(),
                    )
                }

                else -> Unit
            }
        }
    }
}

@Composable
private fun TripsList(
    trips: List<TripDto>,
    onTripClick: (TripDto) -> Unit,
    dynamicColor: Color,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 100.dp),
    ) {
        items(trips) { trip ->
            FinancialTripCard(
                trip = trip,
                onClick = { onTripClick(trip) },
                dynamicColor = dynamicColor,
            )
        }
    }
}

@Composable
private fun FinancialTripCard(
    trip: TripDto,
    onClick: () -> Unit,
    dynamicColor: Color,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() },
        color = dynamicColor.copy(alpha = 0.03f),
        border = BorderStroke(1.dp, dynamicColor.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, TealCyan.copy(alpha = 0.3f), RoundedCornerShape(18.dp)),
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
                        .background(Color.Black.copy(alpha = 0.2f)),
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = trip.title.uppercase(),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                    ),
                    color = dynamicColor.copy(alpha = 0.9f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = dynamicColor.copy(alpha = 0.4f),
                        modifier = Modifier.size(12.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = trip.destination?.uppercase() ?: "UNKNOWN ZONE",
                        style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                        color = dynamicColor.copy(alpha = 0.5f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.Group,
                        contentDescription = null,
                        tint = TealCyan,
                        modifier = Modifier.size(12.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${trip.members.size} PERSONNEL",
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp,
                        ),
                        color = TealCyan,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(TealCyan.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.AccountBalanceWallet,
                    contentDescription = "Split",
                    tint = TealCyan,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

@Composable
fun SyncingLedgerPlaceholder() {
    val dynamicColor = MaterialTheme.colorScheme.onSurface
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Rounded.BlurOn,
            contentDescription = null,
            tint = TealCyan.copy(alpha = 0.5f),
            modifier = Modifier.size(40.dp),
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "DECRYPTING FINANCIAL DATA...",
            color = dynamicColor.copy(alpha = 0.4f),
            fontWeight = FontWeight.Black,
            fontSize = 10.sp,
            letterSpacing = 2.sp,
        )
    }
}
