package com.travoro.app.ui.home.mytrips

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.travoro.app.data.remote.dto.trips.TripDto
import com.travoro.app.di.Session
import com.travoro.app.ui.components.formatDate
import com.travoro.app.ui.components.getTripDays
import com.travoro.app.ui.home.features.weather.TripWeatherSection
import com.travoro.app.ui.home.features.weather.TripWeatherViewModel
import com.travoro.app.ui.home.features.weather.WeatherCardPlaceholder
import com.travoro.app.ui.home.homeNavigation.AddMembersTab
import com.travoro.app.ui.home.homeNavigation.ChatGroupTab
import com.travoro.app.ui.home.homeNavigation.MyTripsTab
import com.travoro.app.ui.home.homeNavigation.SplitExpense
import com.travoro.app.ui.home.travelAI.EditorialSectionHeader
import com.travoro.app.ui.home.travelAI.PaddingWrapper
import com.travoro.app.ui.theme.TealCyan
import com.travoro.app.ui.theme.WarningYellow
import com.travoro.app.ui.utils.LocationHelper
import com.travoro.app.ui.utils.TripLocationService
import com.travoro.app.ui.utils.mapNavigation.openGoogleMapRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTripDetailScreen(
    homeNavController: NavController,
    trip: TripDto,
    onNavigateBack: () -> Unit,
    weatherViewModel: TripWeatherViewModel = hiltViewModel(),
    viewModel: MyTripsViewModel = hiltViewModel(),
    session: Session,
) {
    val dynamicColor = MaterialTheme.colorScheme.onSurface
    val weatherUiState by weatherViewModel.uiState.collectAsStateWithLifecycle()
    val showBottomSheet = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val days = getTripDays(trip.startDate, trip.endDate)

    val updatedTrip =
        (uiState as? MyTripsViewModel.MyTripsState.Success)?.trips?.find { it._id == trip._id }
            ?: trip

    var selectedStatus by remember(updatedTrip._id) {
        mutableStateOf(updatedTrip.status ?: "upcoming")
    }

    var liveLocationEnabled by remember {
        mutableStateOf(
            TripLocationService.isRunning,
        )
    }

    LaunchedEffect(Unit) {
        weatherViewModel.fetchWeather(trip.destination ?: "")
    }
    LaunchedEffect(updatedTrip.status) {
        updatedTrip.status?.let {
            selectedStatus = it
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp),
        ) {
            // 1. HEADER
            item { MissionHeroHeader(trip, onNavigateBack, dynamicColor) }

            // 2. QUICK ACTIONS
            item {
                Spacer(Modifier.height(24.dp))
                TacticalCommandGrid(homeNavController, dynamicColor, trip, showBottomSheet)
            }

            item {
                Spacer(Modifier.height(32.dp))
                SectionTitle("MISSION TELEMETRY", Icons.Rounded.Radar)
                Spacer(Modifier.height(16.dp))
                MissionTelemetryCard(trip, dynamicColor)

                Spacer(Modifier.height(32.dp))

                if (trip.images.isNotEmpty()) {
                    PaddingWrapper {
                        EditorialSectionHeader("Visual Preview", Icons.Default.Collections)
                    }

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        items(trip.images.drop(1).reversed()) { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Trip Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(width = 180.dp, height = 250.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                            )
                        }
                    }
                    Spacer(Modifier.height(32.dp))
                }

                if (!trip.description.isNullOrBlank()) {
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "MISSION BRIEFING",
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp,
                        ),
                        color = TealCyan,
                        modifier = Modifier.padding(horizontal = 24.dp),
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = trip.description,
                        style = TextStyle(fontSize = 13.sp, lineHeight = 20.sp),
                        color = dynamicColor.copy(alpha = 0.7f),
                        modifier = Modifier.padding(horizontal = 24.dp),
                    )
                }
            }

            // 4. CREW ROSTER
            if (trip.members.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(32.dp))
                    SectionTitle("EXPEDITION CREW", Icons.Rounded.Groups)
                    Spacer(Modifier.height(12.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(trip.members) { member ->
                            CrewMemberCard(member, dynamicColor)
                        }
                    }
                }
            }

            // 5. ITINERARY TIMELINE
            if (trip.itinerary.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(32.dp))
                    SectionTitle("TACTICAL ITINERARY", Icons.Rounded.Timeline)
                    Spacer(Modifier.height(16.dp))
                }

                items(trip.itinerary) { dayPlan ->
                    ItineraryNode(dayPlan, dynamicColor)
                }
            }

            // 6. VANI INTELLIGENCE (Hotels & Restaurants)
            if (trip.hotels.isNotEmpty() || trip.restaurants.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(32.dp))
                    SectionTitle("VANI INTELLIGENCE", Icons.Rounded.AutoAwesome)
                    Spacer(Modifier.height(16.dp))
                }

                if (trip.hotels.isNotEmpty()) {
                    item {
                        Text(
                            "SECURED LODGING",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = dynamicColor.copy(alpha = 0.5f),
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            items(trip.hotels) { hotel ->
                                LogisticsCard(
                                    hotel.name,
                                    hotel.priceRange,
                                    hotel.description,
                                    Icons.Rounded.Hotel,
                                    dynamicColor,
                                )
                            }
                        }
                    }
                }

                if (trip.restaurants.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "IDENTIFIED PROVISIONS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = dynamicColor.copy(alpha = 0.5f),
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            items(trip.restaurants) { rest ->
                                LogisticsCard(
                                    rest.name,
                                    rest.specialty,
                                    rest.location,
                                    Icons.Rounded.Restaurant,
                                    dynamicColor,
                                )
                            }
                        }
                    }
                }
            }
            item {
                Spacer(Modifier.height(32.dp))
                SectionTitle("WEATHER FORECAST", Icons.Rounded.Radar)
                Spacer(Modifier.height(16.dp))

                when (weatherUiState) {
                    is TripWeatherViewModel.WeatherEvent.Loading -> {
                        WeatherCardPlaceholder()
                    }

                    is TripWeatherViewModel.WeatherEvent.Success -> {
                        TripWeatherSection(
                            trip.destination,
                            weatherViewModel,
                        )
                    }

                    else -> {}
                }
            }
        }

        if (showBottomSheet.value) {
            val dynamicColor = MaterialTheme.colorScheme.onSurface
            val errorColor = MaterialTheme.colorScheme.error
            val scope = rememberCoroutineScope()

            ModalBottomSheet(
                onDismissRequest = { showBottomSheet.value = false },
                sheetState = rememberModalBottomSheetState(),
                containerColor = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                dragHandle = {
                    Box(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .height(4.dp)
                            .width(40.dp)
                            .background(dynamicColor.copy(alpha = 0.2f), CircleShape),
                    )
                },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = null,
                            tint = TealCyan,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "TRIP CONFIGURATION",
                            style = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp,
                            ),
                            color = TealCyan,
                        )
                    }

                    HorizontalDivider(color = dynamicColor.copy(alpha = 0.1f))


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "EXPEDITION ROSTER",
                                style = TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                ),
                                color = dynamicColor,
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Recruit new personnel to this operation.",
                                fontSize = 11.sp,
                                color = dynamicColor.copy(alpha = 0.5f),
                            )
                        }

                        Spacer(Modifier.width(16.dp))


                        Button(
                            onClick = {
                                homeNavController.navigate(
                                    AddMembersTab(
                                        tripId = trip._id, days = days
                                    )
                                )
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TealCyan.copy(alpha = 0.1f),
                                contentColor = TealCyan,
                            ),
                            border = BorderStroke(1.dp, TealCyan.copy(alpha = 0.3f)),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            modifier = Modifier.height(36.dp),
                            elevation = ButtonDefaults.buttonElevation(0.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PersonAdd,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "ADD",
                                style = TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp,
                                ),
                            )
                        }
                    }

                    HorizontalDivider(color = dynamicColor.copy(alpha = 0.1f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "BROADCAST TELEMETRY",
                                    style = TextStyle(
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp,
                                    ),
                                    color = dynamicColor,
                                )
                                Spacer(Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Rounded.Radar,
                                    contentDescription = null,
                                    tint = if (liveLocationEnabled) {
                                        TealCyan
                                    } else {
                                        dynamicColor.copy(
                                            alpha = 0.3f,
                                        )
                                    },
                                    modifier = Modifier.size(14.dp),
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Warning: Shares real-time GPS with crew.",
                                fontSize = 11.sp,
                                color = errorColor.copy(alpha = 0.8f),
                            )
                        }

                        Spacer(Modifier.width(16.dp))

                        Switch(
                            checked = liveLocationEnabled,
                            enabled = updatedTrip.status == "active",
                            onCheckedChange = {
                                liveLocationEnabled = it
                                viewModel.saveLocationPreference(context, it)
                                if (it) {
                                    viewModel.startLiveLocation(
                                        context = context,
                                        tripId = updatedTrip._id,
                                        userId = session.getUserId(),
                                    )
                                } else {
                                    viewModel.stopLiveLocation(context)
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.surface,
                                checkedTrackColor = TealCyan,
                                uncheckedThumbColor = dynamicColor.copy(alpha = 0.5f),
                                uncheckedTrackColor = dynamicColor.copy(alpha = 0.1f),
                                disabledCheckedTrackColor = TealCyan.copy(alpha = 0.3f),
                                disabledUncheckedTrackColor = dynamicColor.copy(alpha = 0.05f),
                            ),
                        )
                    }

                    HorizontalDivider(color = dynamicColor.copy(alpha = 0.1f))

                    Column {
                        Text(
                            text = "DANGER ZONE",
                            style = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp,
                            ),
                            color = errorColor,
                        )
                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    viewModel.leaveTrip(tripId = updatedTrip._id)
                                    Toast.makeText(context, "Trip Aborted", Toast.LENGTH_SHORT)
                                        .show()
                                    delay(1000)
                                    homeNavController.navigate(MyTripsTab)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = errorColor.copy(alpha = 0.1f),
                            ),
                            border = BorderStroke(1.dp, errorColor.copy(alpha = 0.4f)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            elevation = ButtonDefaults.buttonElevation(0.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Warning,
                                contentDescription = null,
                                tint = errorColor,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "ABORT MISSION (DELETE)",
                                style = TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp,
                                ),
                                color = errorColor,
                            )
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun SectionTitle(
    title: String,
    icon: ImageVector,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 24.dp),
    ) {
        Icon(icon, contentDescription = null, tint = TealCyan, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Text(
            title,
            style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
            ),
            color = TealCyan,
        )
    }
}

@Composable
fun MissionHeroHeader(
    trip: TripDto,
    onNavigateBack: () -> Unit,
    dynamicColor: Color,
) {
    val statusColor = if (trip.status?.lowercase() == "active") TealCyan else WarningYellow

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp),
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
                        listOf(
                            Color.Black.copy(alpha = 0.5f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.95f),
                        ),
                        startY = 0f,
                        endY = 1000f,
                    ),
                ),
        )

        Box(
            modifier = Modifier
                .padding(top = 48.dp, start = 24.dp)
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.3f))
                .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                .clickable { onNavigateBack() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowBackIos,
                null,
                tint = Color.White,
                modifier = Modifier
                    .size(18.dp)
                    .padding(end = 2.dp),
            )
        }

        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 48.dp, end = 24.dp),
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
                    trip.status?.uppercase() ?: "UNKNOWN",
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
                .padding(24.dp),
        ) {
            Text(
                trip.title.uppercase(),
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                ),
                color = Color.White,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                trip.destination?.uppercase() ?: "CLASSIFIED",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp,
                ),
                color = Color.White.copy(alpha = 0.6f),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Trip Start Date: ${formatDate(trip.startDate)}",
                color = statusColor,
                fontSize = 13.sp,
                letterSpacing = 1.sp,
            )
        }
    }
}

@Composable
fun TacticalCommandGrid(
    homeNavController: NavController,
    dynamicColor: Color,
    trip: TripDto,
    showBottomSheet: MutableState<Boolean>,
) {
    val context = LocalContext.current
    val locationHelper = remember { LocationHelper(context) }
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CommandButton(
            Icons.Rounded.ChatBubble,
            "COMMS",
            dynamicColor,
            Modifier.weight(1f),
            onClick = {
                homeNavController.navigate(ChatGroupTab)
            },
        )
        CommandButton(
            Icons.Rounded.AccountBalanceWallet,
            "LEDGER",
            dynamicColor,
            Modifier.weight(1f),
            onClick = {
                homeNavController.navigate(SplitExpense(trip = Json.encodeToString(trip)))
            },
        )
        CommandButton(
            Icons.Rounded.Map,
            "MAP",
            dynamicColor,
            Modifier.weight(1f),
            onClick = {
                scope.launch {
                    val location = locationHelper.getCurrentLocation(PRIORITY_HIGH_ACCURACY)

                    location?.let {
                        openGoogleMapRoute(
                            context,
                            it.latitude,
                            it.longitude,
                            trip.latitude ?: 0.0,
                            trip.longitude ?: 0.0,
                        )
                    }
                }
            },
        )
        CommandButton(
            Icons.Rounded.Tune,
            "CONFIG",
            dynamicColor,
            Modifier.weight(1f),
            onClick = {
                showBottomSheet.value = !showBottomSheet.value
            },
        )
    }
}

@Composable
fun CommandButton(
    icon: ImageVector,
    label: String,
    dynamicColor: Color,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(dynamicColor.copy(alpha = 0.03f))
            .border(1.dp, dynamicColor.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(icon, null, tint = TealCyan, modifier = Modifier.size(24.dp))
        Spacer(Modifier.height(8.dp))
        Text(
            label,
            style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp),
            color = dynamicColor.copy(alpha = 0.8f),
        )
    }
}

@Composable
fun MissionTelemetryCard(
    trip: TripDto,
    dynamicColor: Color,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(20.dp),
        color = dynamicColor.copy(alpha = 0.02f),
        border = BorderStroke(1.dp, dynamicColor.copy(alpha = 0.06f)),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(
                        "MISSION TYPE",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp,
                        color = dynamicColor.copy(alpha = 0.4f),
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        trip.tripType?.uppercase() ?: "STANDARD",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = dynamicColor,
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "TRAVEL STYLE",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp,
                        color = dynamicColor.copy(alpha = 0.4f),
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        trip.travelStyle?.uppercase() ?: "UNASSIGNED",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = dynamicColor,
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            HorizontalDivider(color = dynamicColor.copy(alpha = 0.05f))
            Spacer(Modifier.height(20.dp))

            val budget = trip.budget?.toFloat() ?: 1f
            val spent = trip.totalExpense?.toFloat() ?: 0f
            val progress = if (budget > 0) (spent / budget).coerceIn(0f, 1f) else 0f

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    "BUDGET CONSUMPTION",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    color = dynamicColor.copy(alpha = 0.4f),
                )
                Text(
                    "${(progress * 100).roundToInt()}%",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    color = TealCyan,
                )
            }
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(dynamicColor.copy(alpha = 0.05f)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(6.dp)
                        .clip(CircleShape)
                        .background(if (progress > 0.9f) Color(0xFFFF4C4C) else TealCyan),
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    "SPENT: ₹${spent.toInt()}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = dynamicColor.copy(alpha = 0.6f),
                )
                Text(
                    "LIMIT: ₹${budget.toInt()}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = dynamicColor.copy(alpha = 0.6f),
                )
            }
        }
    }
}

@Composable
fun CrewMemberCard(
    member: com.travoro.app.data.remote.dto.trips.MemberDto,
    dynamicColor: Color,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(dynamicColor.copy(alpha = 0.03f))
            .border(1.dp, dynamicColor.copy(alpha = 0.08f), RoundedCornerShape(50))
            .padding(end = 16.dp, top = 4.dp, bottom = 4.dp, start = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = member.user.profilePic,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                member.user.fullname.uppercase(),
                style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Black),
                color = dynamicColor,
            )
            Text(
                member.role.uppercase(),
                style = TextStyle(fontSize = 8.sp, fontWeight = FontWeight.Bold),
                color = if (member.role == "admin") TealCyan else dynamicColor.copy(alpha = 0.5f),
            )
        }
    }
}

@Composable
fun ItineraryNode(
    dayPlan: com.travoro.app.data.remote.dto.trips.ItineraryDto,
    dynamicColor: Color,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 4.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(TealCyan.copy(alpha = 0.2f))
                    .border(2.dp, TealCyan, CircleShape),
            )
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(80.dp)
                    .background(TealCyan.copy(alpha = 0.2f)),
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp),
        ) {
            Text(
                text = "DAY ${dayPlan.day}",
                style = TextStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                ),
                color = dynamicColor.copy(alpha = 0.4f),
            )

            Spacer(Modifier.height(4.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = dynamicColor.copy(alpha = 0.02f),
                border = BorderStroke(1.dp, dynamicColor.copy(alpha = 0.05f)),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = dayPlan.plan,
                    style = TextStyle(fontSize = 13.sp, lineHeight = 20.sp),
                    color = dynamicColor.copy(alpha = 0.8f),
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
    }
}

@Composable
fun LogisticsCard(
    title: String,
    sub1: String?,
    sub2: String?,
    icon: ImageVector,
    dynamicColor: Color,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = dynamicColor.copy(alpha = 0.02f),
        border = BorderStroke(1.dp, dynamicColor.copy(alpha = 0.06f)),
        modifier = Modifier.width(200.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, null, tint = TealCyan, modifier = Modifier.size(20.dp))
            Spacer(Modifier.height(12.dp))
            Text(
                title.uppercase(),
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Black),
                color = dynamicColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                sub1?.uppercase() ?: "INFO PENDING",
                style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                color = TealCyan,
            )
            if (!sub2.isNullOrBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    sub2,
                    style = TextStyle(fontSize = 9.sp),
                    color = dynamicColor.copy(alpha = 0.5f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
