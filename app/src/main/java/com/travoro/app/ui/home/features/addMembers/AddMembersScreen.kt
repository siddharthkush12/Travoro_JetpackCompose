package com.travoro.app.ui.home.features.addMembers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.rounded.GroupAdd
import androidx.compose.material.icons.rounded.LinkOff
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.travoro.app.data.remote.dto.trips.Profile
import com.travoro.app.ui.components.CustomDatePickerField
import com.travoro.app.ui.components.CustomTopBar
import com.travoro.app.ui.components.ErrorAlertDialog
import com.travoro.app.ui.components.calculateEndDate
import com.travoro.app.ui.components.formatDate
import com.travoro.app.ui.home.homeNavigation.MyTripsTab
import com.travoro.app.ui.theme.ErrorRed
import com.travoro.app.ui.theme.TealCyan
import com.travoro.app.ui.theme.TealCyanLight
import com.travoro.app.ui.theme.WarningYellow

@Composable
fun AddMembersScreen(
    tripId: String,
    onNavigateBack: () -> Unit,
    days: Int,
    viewModel: AddMembersViewModel = hiltViewModel(),
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val members by viewModel.members.collectAsStateWithLifecycle()
    val phone by viewModel.memberPhone.collectAsStateWithLifecycle()
    val dynamicColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f)),
    ) {
        CustomTopBar(
            title = "SQUAD INVITATION",
            icon = Icons.Rounded.GroupAdd,
            onBackClick = onNavigateBack,
        )

        if (uiState is AddMembersViewModel.AddMembersEvent.Error) {
            val message = (uiState as AddMembersViewModel.AddMembersEvent.Error).message
            ErrorAlertDialog(message = message, onDismiss = viewModel::resetState)
        }

        if (uiState is AddMembersViewModel.AddMembersEvent.SearchConflict) {
            val profile = (uiState as AddMembersViewModel.AddMembersEvent.SearchConflict).profile
            var showDatePicker by remember { mutableStateOf(false) }
            var newDate by remember { mutableStateOf("") }



            if (!showDatePicker) {
                AlertDialog(
                    onDismissRequest = { viewModel.resetState() },
                    shape = RoundedCornerShape(16.dp),
                    containerColor = MaterialTheme.colorScheme.surface,
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Rounded.WarningAmber,
                                contentDescription = null,
                                tint = WarningYellow,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "SCHEDULING ANOMALY", style = TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                ), color = WarningYellow
                            )
                        }
                    },
                    text = {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(dynamicColor.copy(alpha = 0.05f))
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                AsyncImage(
                                    model = profile.profilePic,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .border(1.dp, dynamicColor.copy(alpha = 0.2f), CircleShape),
                                    contentScale = ContentScale.Crop,
                                )
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = profile.fullname.uppercase(), style = TextStyle(
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Black,
                                            color = dynamicColor
                                        )
                                    )
                                    Text(
                                        text = profile.phone, style = TextStyle(
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 11.sp,
                                            color = dynamicColor.copy(alpha = 0.6f)
                                        )
                                    )
                                }
                            }

                            Spacer(Modifier.height(16.dp))

                            Text(
                                text = "CONFLICTING EXPEDITION DETECTED:", style = TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                ), color = dynamicColor.copy(alpha = 0.5f)
                            )
                            Spacer(Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = WarningYellow.copy(alpha = 0.05f),
                                border = BorderStroke(1.dp, WarningYellow.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = profile.conflictTrip?.title?.uppercase()
                                            ?: "CLASSIFIED TRIP", style = TextStyle(
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = dynamicColor
                                        )
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = "${formatDate(profile.conflictTrip?.startDate)} → ${
                                            formatDate(
                                                profile.conflictTrip?.endDate
                                            )
                                        }", style = TextStyle(
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 12.sp,
                                            color = WarningYellow
                                        )
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { showDatePicker = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = WarningYellow, contentColor = Color.Black
                                )
                            ) {
                                Text(
                                    "OVERRIDE DATE", style = TextStyle(
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp
                                    )
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            OutlinedButton(
                                onClick = { viewModel.resetState() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, dynamicColor.copy(alpha = 0.2f)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = dynamicColor)
                            ) {
                                Text(
                                    "ABORT", style = TextStyle(
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                )
                            }
                        }
                    })
            }

            if (showDatePicker) {
                AlertDialog(
                    onDismissRequest = {
                        showDatePicker = false
                        viewModel.resetState()
                    },
                    shape = RoundedCornerShape(16.dp),
                    containerColor = MaterialTheme.colorScheme.surface,
                    title = {
                        Text(
                            text = "CALIBRATE NEW TIMELINE", style = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            ), color = TealCyan
                        )
                    },
                    text = {
                        CustomDatePickerField(
                            label = "NEW START DATE",
                            selectedDate = newDate,
                            onDateSelected = { newDate = it },
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (newDate.isNotBlank()) {
                                    val endDate = calculateEndDate(newDate, days)
                                    viewModel.changeTripDate(tripId, newDate, endDate)
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TealCyan)
                        ) {
                            Text(
                                "CONFIRM", style = TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black
                                )
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text(
                                "CANCEL", style = TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    color = dynamicColor.copy(alpha = 0.7f)
                                )
                            )
                        }
                    })
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Text(
                    text = "AUTHORIZE EXTERNAL NODES",
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                        color = TealCyan,
                    ),
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
                )
            }

            item {
                MemberInputCard(
                    value = phone,
                    onValueChange = { viewModel.onPhoneChange(it) },
                    onSearchClick = { viewModel.searchMember(tripId) },
                )
            }

            if (members.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "VERIFIED CREW (${members.size})",
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = dynamicColor.copy(alpha = 0.5f),
                        ),
                        modifier = Modifier.padding(start = 4.dp),
                    )
                }
            }

            items(members) { member ->
                AuthorizedNodeItem(
                    member = member,
                    onRemoveClick = { viewModel.removeMember(member) },
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        viewModel.addMembersToTrip(tripId)
                        navController.navigate(MyTripsTab)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .border(1.dp, TealCyan.copy(alpha = 0.4f), RoundedCornerShape(18.dp)),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TealCyan),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "INITIALIZE SECURE COMMS",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = Color.White,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
fun MemberInputCard(
    value: String,
    onValueChange: (String) -> Unit,
    onSearchClick: () -> Unit,
) {
    val dynamicColor = MaterialTheme.colorScheme.onSurface

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        "Enter node identifier (phone)...",
                        fontSize = 13.sp,
                        color = dynamicColor.copy(alpha = 0.4f),
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = dynamicColor,
                    unfocusedTextColor = dynamicColor.copy(alpha = 0.9f),
                    focusedContainerColor = dynamicColor.copy(alpha = 0.05f),
                    unfocusedContainerColor = dynamicColor.copy(alpha = 0.02f),
                    focusedBorderColor = TealCyan,
                    unfocusedBorderColor = dynamicColor.copy(alpha = 0.15f),
                    cursorColor = TealCyan,
                ),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(TealCyan)
                    .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center,
            ) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search",
                        tint = Color.White,
                    )
                }
            }
        }
    }
}

@Composable
fun AuthorizedNodeItem(
    member: Profile,
    onRemoveClick: () -> Unit,
) {
    val dynamicColor = MaterialTheme.colorScheme.onSurface

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.border(
                        2.dp,
                        Brush.sweepGradient(listOf(TealCyan, TealCyanLight, TealCyan)),
                        CircleShape,
                    ),
                ) {
                    AsyncImage(
                        model = member.profilePic,
                        contentDescription = null,
                        modifier = Modifier
                            .size(46.dp)
                            .padding(3.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = member.fullname.uppercase(),
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp,
                            color = dynamicColor.copy(alpha = 0.9f),
                        ),
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = member.phone,
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.sp,
                            color = dynamicColor.copy(alpha = 0.5f),
                        ),
                    )
                }
            }

            OutlinedButton(
                onClick = onRemoveClick,
                modifier = Modifier.height(34.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, ErrorRed.copy(alpha = 0.3f)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = ErrorRed,
                    containerColor = ErrorRed.copy(alpha = 0.05f),
                ),
            ) {
                Icon(
                    imageVector = Icons.Rounded.LinkOff,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "UNLINK",
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                    ),
                )
            }
        }
    }
}