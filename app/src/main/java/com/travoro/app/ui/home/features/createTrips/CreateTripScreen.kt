package com.travoro.app.ui.home.features.createTrips

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.travoro.app.ui.components.CustomDatePickerField
import com.travoro.app.ui.components.CustomDropdownField
import com.travoro.app.ui.components.CustomEditText
import com.travoro.app.ui.components.CustomTopBar
import com.travoro.app.ui.components.ErrorAlertDialog
import com.travoro.app.ui.home.dashboard.AIBannerCard
import com.travoro.app.ui.home.homeNavigation.AddMembersTab
import com.travoro.app.ui.home.homeNavigation.TravelAITab
import com.travoro.app.ui.theme.ErrorRed
import com.travoro.app.ui.theme.MidnightBlue
import com.travoro.app.ui.theme.TealCyan

@Composable
fun CreateTripScreen(
    viewModel: CreateTripViewModel = hiltViewModel(),
    homeNavController: NavController,
) {
    val title by viewModel.title.collectAsStateWithLifecycle()
    val destination by viewModel.destination.collectAsStateWithLifecycle()
    val description by viewModel.description.collectAsStateWithLifecycle()
    val tripType by viewModel.tripType.collectAsStateWithLifecycle()
    val travelStyle by viewModel.travelStyle.collectAsStateWithLifecycle()
    val tripBudget by viewModel.tripBudget.collectAsStateWithLifecycle()
    val tripStartDate by viewModel.tripStartDate.collectAsStateWithLifecycle()
    val tripEndDate by viewModel.tripEndDate.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val tripTypes = listOf("solo", "friends", "family", "couple")
    val travelStyles = listOf("adventure", "relax", "budget", "luxury", "nature", "spiritual")
    val dynamicColor = MaterialTheme.colorScheme.onSurface

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is CreateTripViewModel.CreateTripNavigation.NavigateToAddMembers -> {
                    homeNavController.navigate(AddMembersTab(event.tripId, event.days))
                }

                is CreateTripViewModel.CreateTripNavigation.NavigateBack -> {
                    homeNavController.popBackStack()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        if (uiState is CreateTripViewModel.CreateTripEvent.Error) {
            val message = (uiState as CreateTripViewModel.CreateTripEvent.Error).message
            ErrorAlertDialog(message = message, onDismiss = viewModel::clearError)
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            CustomTopBar(
                title = "CREATE JOURNEY",
                subtitle = "DEFINE PARAMETERS FOR NEW JOURNEY",
                icon = Icons.Rounded.Map,
                onBackClick = { homeNavController.popBackStack() },
                subTitleSize = 11.sp,
            )

            Spacer(modifier = Modifier.size(24.dp))

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
            ) {
                AIBannerCard(onClick = { homeNavController.navigate(TravelAITab) })

                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier.padding(horizontal = 24.dp),
                ) {
                    Text(
                        "Build your Custom dream itinerary.",
                        color = dynamicColor.copy(alpha = 0.4f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, dynamicColor.copy(alpha = 0.1f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        CustomEditText(
                            value = title,
                            onValueChange = viewModel::onTitleChange,
                            label = { Text("TRIP TITLE", fontSize = 14.sp) },
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        CustomEditText(
                            value = destination,
                            onValueChange = viewModel::onDestinationChange,
                            label = { Text("WHERE TO? GO", fontSize = 14.sp) },
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        CustomDropdownField(
                            selectedValue = tripType.uppercase(),
                            options = tripTypes.map { it.uppercase() },
                            label = "WHO ARE YOU WITH?",
                            onValueChange = { viewModel.onTripTypeChange(it.lowercase()) },
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        CustomDropdownField(
                            selectedValue = travelStyle.uppercase(),
                            options = travelStyles.map { it.uppercase() },
                            label = "TRIP VIBE / STYLE",
                            onValueChange = { viewModel.onTravelStyleChange(it.lowercase()) },
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        CustomEditText(
                            value = description,
                            onValueChange = viewModel::onDescriptionChange,
                            label = { Text("SHORT DESCRIPTION", fontSize = 14.sp) },
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        CustomDatePickerField(
                            label = "TRIP START DATE",
                            selectedDate = tripStartDate,
                            onDateSelected = { viewModel.onTripStartDateChange(it) },
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        CustomDatePickerField(
                            label = "TRIP END DATE",
                            selectedDate = tripEndDate,
                            onDateSelected = { viewModel.onTripEndDateChange(it) },
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        CustomEditText(
                            value = tripBudget,
                            onValueChange = { viewModel.onBudgetChange(it.toIntOrNull() ?: 0) },
                            label = { Text("ALLOCATED BUDGET (₹) ", fontSize = 14.sp) },
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "All fields are mandatory*",
                            color = ErrorRed.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(72.dp))

                Button(
                    shape = RoundedCornerShape(16.dp),
                    onClick = {
                        viewModel.createTrip()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TealCyan),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.Map,
                            contentDescription = null,
                            tint = MidnightBlue,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "INITIALIZE TRIP",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = MidnightBlue,
                        )
                        Spacer(Modifier.weight(1f))
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            tint = MidnightBlue,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.size(30.dp))
            }
        }
    }
}
