package com.travoro.app.ui.home.travelAI

import androidx.compose.animation.core.EaseInOutQuart
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.travoro.app.data.remote.dto.travelAi.*
import com.travoro.app.ui.components.CustomDatePickerField
import com.travoro.app.ui.components.HomeBarHeaders
import com.travoro.app.ui.home.homeNavigation.AddMembersTab
import com.travoro.app.ui.home.homeNavigation.MyTripsTab
import com.travoro.app.ui.theme.TealCyan
import com.travoro.app.ui.theme.TealCyanLight
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TravelAITabScreen(
    paddingValues: PaddingValues,
    viewModel: TravelAiViewModel = hiltViewModel(),
    navController: NavController,
) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size, uiState) {
        if (messages.isNotEmpty()) {
            listState.scrollToItem(messages.size - 1)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is TravelAiViewModel.TravelAiNavigation.NavigateToAddMembers -> {
                    navController.navigate(AddMembersTab(event.tripId, event.days))
                }

                is TravelAiViewModel.TravelAiNavigation.NavigateToHome -> {
                    navController.navigate(MyTripsTab)
                }
            }
        }
    }

    val isLoading = uiState is TravelAiViewModel.TravelAiEvent.Loading
    val showInput = !isLoading && uiState !is TravelAiViewModel.TravelAiEvent.Success
    val infiniteTransition = rememberInfiniteTransition(label = "ai_pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = EaseInOutQuart),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "iconScale",
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            HomeBarHeaders(
                title = "Travoro  AI",
                subtitle = "INTELLIGENT TRIP ARCHITECT",
                icon = Icons.Default.AutoAwesome,
                topPadding = paddingValues.calculateTopPadding(),
            )

            if (uiState is TravelAiViewModel.TravelAiEvent.DateConflict) {
                var newDate by remember {
                    mutableStateOf("")
                }

                val tripData = (uiState as TravelAiViewModel.TravelAiEvent.DateConflict).tripData

                AlertDialog(
                    onDismissRequest = {},
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.updateTripDate(
                                    tripData,
                                    newDate,
                                )
                            },
                        ) {
                            Text("SAVE")
                        }
                    },
                    title = {
                        Text(
                            "Date Conflict",
                        )
                    },
                    text = {
                        Column {
                            Text(
                                "You already have trip on selected dates. Choose new start date.",
                            )

                            Spacer(
                                Modifier.height(12.dp),
                            )

                            CustomDatePickerField(
                                label = "NEW START DATE",
                                selectedDate = newDate,
                                onDateSelected = {
                                    newDate = it
                                },
                            )
                        }
                    },
                )
            }

            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    color = TealCyan,
                    trackColor = Color.Transparent,
                )
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 10.dp,
                    end = 10.dp,
                    top = 24.dp,
                    bottom = paddingValues.calculateBottomPadding() + if (showInput) 100.dp else 24.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                modifier = Modifier.graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                },
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.AutoAwesome,
                                    contentDescription = null,
                                    tint = TealCyan.copy(alpha = 0.2f),
                                    modifier = Modifier.size(32.dp),
                                )
                                Icon(
                                    imageVector = Icons.Rounded.AutoAwesome,
                                    contentDescription = "AI Activation",
                                    tint = TealCyan,
                                    modifier = Modifier.size(24.dp),
                                )
                            }

                            Spacer(Modifier.width(24.dp))

                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "Plan With ",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.ExtraLight,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                    ),
                                )
                                Text(
                                    text = "Vani.",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = TealCyan,
                                    ),
                                )
                            }
                        }

                        Spacer(Modifier.height(18.dp))

                        AiBubble(
                            text = "I am the Travoro Architect. I’ve been designed to map out the most meaningful experiences for your journey. Where shall we begin?",
                        )
                    }
                }
                items(messages, key = { it.id }) { message ->
                    ChatMessageItem(
                        message = message,
                        isLoading = isLoading,
                        onOptionClick = { viewModel.onOptionSelected(it) },
                        onAccept = {
                            viewModel.acceptTrip(it)
                        },
                    )
                }
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = showInput,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = paddingValues.calculateBottomPadding() + 12.dp),
            ) {
                CustomInputField(
                    onSend = { text ->
                        viewModel.onOptionSelected(text)
                    },
                )
            }
        }
    }
}

@Composable
fun CustomInputField(onSend: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, TealCyan.copy(alpha = 0.3f)),
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 20.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
        ) {
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (text.isNotBlank()) {
                            onSend(text)
                            text = ""
                        }
                    },
                ),
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    if (text.isEmpty()) {
                        Text(
                            text = "Give answer here...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                    innerTextField()
                },
                singleLine = true,
            )

            IconButton(
                onClick = {
                    if (text.isNotBlank()) {
                        onSend(text)
                        text = ""
                    }
                },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                TealCyan,
                                TealCyan.copy(alpha = 0.8f),
                            ),
                        ),
                    ),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Send,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier.size(25.dp),
                )
            }
        }
    }
}

@Composable
fun ChatMessageItem(
    message: AiChatMessage,
    isLoading: Boolean,
    onOptionClick: (String) -> Unit,
    onAccept: (AiTripData) -> Unit,
) {
    val isUser = message.isUser
    val alignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment,
    ) {
        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.fillMaxWidth(),
        ) {
            when {
                message.tripResult != null -> TripResultCard(
                    message.tripResult,
                    onAccept,
                    isLoading,
                )

                isUser -> UserBubble(message.text ?: "")

                message.text == "🤖 Generating your trip..." -> StaticTypingBubble()

                else -> AiBubble(message.text ?: "")
            }

            message.options?.let { optionsList ->
                var selectedDate by remember { mutableStateOf("") }
                if (optionsList.contains("Pick Date")) {
                    CustomDatePickerField(
                        onDateSelected = {
                            onOptionClick(it)
                        },
                        label = "Trip Start Date",
                        selectedDate = selectedDate,
                    )
                } else if (optionsList.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    @OptIn(ExperimentalLayoutApi::class) FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth(0.95f),
                    ) {
                        optionsList.forEach { option ->
                            SelectionButton(
                                text = option,
                                enabled = !isLoading,
                                onClick = { onOptionClick(option) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SelectionButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val isPrimaryAction = text == "Regenerate Trip" || text == "Restart Trip"

    Box(
        modifier = Modifier
            .widthIn(min = 100.dp)
            .alpha(if (enabled) 1f else 0.5f)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = if (isPrimaryAction) {
                        listOf(TealCyan.copy(alpha = 0.15f), TealCyan.copy(alpha = 0.05f))
                    } else {
                        listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        )
                    },
                ),
            )
            .clickable(enabled = enabled, onClick = onClick)
            .border(
                width = 1.3.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        TealCyan.copy(alpha = 0.4f),
                        TealCyan.copy(alpha = 0.3f),
                        TealCyan.copy(alpha = 0.4f),
                    ),
                ),
                shape = RoundedCornerShape(24.dp),
            )
            .padding(horizontal = 18.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text.uppercase(),
            color = TealCyan,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelLarge.copy(
                letterSpacing = 0.8.sp,
                fontSize = 11.sp,
            ),
        )
    }
}

@Composable
fun AiBubble(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            TealCyan.copy(alpha = 0.2f),
                            TealCyan.copy(alpha = 0.1f),
                        ),
                    ),
                )
                .border(1.dp, TealCyan.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Rounded.SmartToy,
                contentDescription = "AI Bot",
                tint = TealCyan,
                modifier = Modifier.size(20.dp),
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Surface(
            shape = RoundedCornerShape(
                18.dp,
                18.dp,
                18.dp,
                2.dp,
            ),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
            border = BorderStroke(1.dp, TealCyan.copy(alpha = 0.25f)),
            modifier = Modifier.widthIn(max = 280.dp),
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
            )
        }
    }
}

@Composable
fun UserBubble(text: String) {
    val bubbleShape = RoundedCornerShape(
        20.dp,
        20.dp,
        4.dp,
        20.dp,
    )

    Box(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .widthIn(max = 285.dp)
            .clip(bubbleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        TealCyanLight,
                        TealCyan,
                    ),
                ),
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.5f),
                        Color.White.copy(alpha = 0.0f),
                    ),
                ),
                shape = bubbleShape,
            )
            .padding(horizontal = 18.dp, vertical = 14.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 15.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.2.sp,
                fontWeight = FontWeight.Medium,
            ),
            color = Color.White,
        )
    }
}

@Composable
fun StaticTypingBubble() {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp),
        modifier = Modifier.padding(vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            (0..2).forEach { _ ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(TealCyan.copy(alpha = 0.4f)),
                )
            }
        }
    }
}

@Composable
fun TripResultCard(
    trip: AiTripData,
    onAccept: (AiTripData) -> Unit,
    isLoading: Boolean,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(320.dp)
                    .fillMaxWidth(),
            ) {
                AsyncImage(
                    model = trip.coverImage.ifEmpty { trip.images.firstOrNull() },
                    contentDescription = "Destination Cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(0.95f)),
                                startY = 150f,
                            ),
                        ),
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(24.dp),
                ) {
                    Text(
                        text = "VANI'S CHOICE • ${trip.destination.uppercase()}",
                        color = TealCyan,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        fontSize = 11.sp,
                    )

                    Text(
                        text = trip.title,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Black,
                        lineHeight = 34.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(Modifier.height(5.dp))

                    Text(
                        text = trip.description,
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Column(
                modifier = Modifier.padding(vertical = 24.dp),
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StaticBentoTile(
                            Modifier.weight(1f),
                            "Budget",
                            "₹${trip.budget}",
                            Icons.Default.Payments,
                        )
                        StaticBentoTile(
                            Modifier.weight(1f),
                            "Duration",
                            "${trip.days} Days",
                            Icons.Default.Timer,
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StaticBentoTile(
                            Modifier.weight(1f),
                            "Season",
                            trip.bestTimeToVisit,
                            Icons.Default.CalendarMonth,
                        )
                        StaticBentoTile(
                            Modifier.weight(1f),
                            "Style",
                            trip.travelStyle,
                            Icons.Default.Star,
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StaticBentoTile(
                            Modifier.weight(1f),
                            "Group",
                            trip.groupType,
                            Icons.Default.Group,
                        )
                        StaticBentoTile(
                            Modifier.weight(1f),
                            "Transport",
                            trip.recommendedTransport,
                            Icons.Default.DirectionsTransit,
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))

                if (trip.images.isNotEmpty()) {
                    PaddingWrapper {
                        EditorialSectionHeader("Visual Preview", Icons.Default.Collections)
                    }

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        items(trip.images) { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Trip Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(width = 150.dp, height = 200.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                            )
                        }
                    }
                    Spacer(Modifier.height(32.dp))
                }

                PaddingWrapper {
                    EditorialSectionHeader("Budget Breakdown", Icons.Default.AccountBalance)
                }
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.09f),
                    ),
                    modifier = Modifier.padding(horizontal = 24.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        BudgetRow(
                            "Transport",
                            trip.budgetBreakdown.transport,
                            Icons.Default.DirectionsTransit,
                        )
                        BudgetRow("Stays", trip.budgetBreakdown.accommodation, Icons.Default.Hotel)
                        BudgetRow("Food", trip.budgetBreakdown.food, Icons.Default.Restaurant)
                        BudgetRow(
                            "Activities",
                            trip.budgetBreakdown.activities,
                            Icons.Default.LocalActivity,
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            if (trip.hotels.isNotEmpty()) {
                PaddingWrapper {
                    EditorialSectionHeader("Recommended Stays", Icons.Default.Hotel)
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(trip.hotels) { hotel ->
                        PlaceCard(
                            title = hotel.name,
                            subtitle = hotel.priceRange,
                            description = hotel.description,
                            icon = Icons.Default.Hotel,
                        )
                    }
                }
                Spacer(Modifier.height(32.dp))
            }

            if (trip.restaurants.isNotEmpty()) {
                PaddingWrapper {
                    EditorialSectionHeader("Local Dining", Icons.Default.Restaurant)
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(trip.restaurants) { restaurant ->
                        PlaceCard(
                            title = restaurant.name,
                            subtitle = restaurant.specialty,
                            description = restaurant.location,
                            icon = Icons.Default.RestaurantMenu,
                        )
                    }
                }
                Spacer(Modifier.height(32.dp))
            }

            if (trip.itinerary.isNotEmpty()) {
                PaddingWrapper {
                    EditorialSectionHeader("Itinerary", Icons.Default.Route)
                }

                trip.itinerary.forEach {
                    StaticItineraryItem(it)
                }

                Spacer(Modifier.height(32.dp))
            }

            if (trip.travelTips.isNotEmpty()) {
                PaddingWrapper {
                    EditorialSectionHeader("Pro Tips", Icons.Default.Lightbulb)
                }
                Surface(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    color = TealCyan.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, TealCyan.copy(alpha = 0.3f)),
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        trip.travelTips.forEach { tip ->
                            Row(
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = TealCyan,
                                    modifier = Modifier.size(18.dp),
                                )
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    text = tip,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 20.sp,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))
            }

            PaddingWrapper {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(bottom = 10.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isLoading) {
                                SolidColor(TealCyan.copy(alpha = 0.5f))
                            } else {
                                Brush.horizontalGradient(
                                    listOf(
                                        TealCyan,
                                        TealCyan.copy(alpha = 0.8f),
                                    ),
                                )
                            },
                        )
                        .clickable(enabled = !isLoading) { onAccept(trip) },
                    contentAlignment = Alignment.Center,
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                        )
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Secure Trip",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = Color.White,
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaddingWrapper(content: @Composable () -> Unit) {
    Box(modifier = Modifier.padding(horizontal = 24.dp)) {
        content()
    }
}

@Composable
fun EditorialSectionHeader(
    title: String,
    icon: ImageVector,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(TealCyan.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = TealCyan,
                modifier = Modifier.size(18.dp),
            )
        }

        Spacer(Modifier.width(12.dp))

        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
        )
    }
}

@Composable
fun StaticBentoTile(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: ImageVector,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.09f)),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TealCyan,
                modifier = Modifier.size(22.dp),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = value.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun BudgetRow(
    category: String,
    amount: String,
    icon: ImageVector,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp),
        )

        Spacer(Modifier.width(12.dp))

        Text(
            category,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
        )
        Text(
            amount,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
fun PlaceCard(
    title: String,
    subtitle: String,
    description: String,
    icon: ImageVector,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.09f)),
        modifier = Modifier
            .width(260.dp)
            .wrapContentHeight(),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = TealCyan,
                    modifier = Modifier.size(24.dp),
                )

                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 20.sp,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        subtitle,
                        color = TealCyan,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Spacer(Modifier.height(12.dp))

            Text(
                description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp,
            )
        }
    }
}

@Composable
fun StaticItineraryItem(day: Itinerary) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            TealCyan.copy(alpha = 0.2f),
                            TealCyan.copy(alpha = 0.05f),
                        ),
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    "DAY",
                    style = MaterialTheme.typography.labelSmall,
                    color = TealCyan,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    "${day.day}",
                    color = TealCyan,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = "Day ${day.day} Highlights",
                fontWeight = FontWeight.ExtraBold,
                color = TealCyan,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = day.plan,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 22.sp,
            )
        }
    }
}
