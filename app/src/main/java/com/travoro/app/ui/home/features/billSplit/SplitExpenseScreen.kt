package com.travoro.app.ui.home.features.billSplit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.RequestQuote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.travoro.app.data.remote.dto.trips.MemberDto
import com.travoro.app.data.remote.dto.trips.TripDto
import com.travoro.app.ui.components.Avatar
import com.travoro.app.ui.components.CustomErrorMessage
import com.travoro.app.ui.components.CustomTopBar
import com.travoro.app.ui.theme.TealCyan
import com.travoro.app.ui.theme.WarningYellow

@Composable
fun SplitExpenseScreen(
    trip: TripDto,
    onNavigateBack: () -> Unit,
    viewModel: BillSplitViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val payments by viewModel.payments.collectAsStateWithLifecycle()

    val members = trip.members
    val dynamicColor = MaterialTheme.colorScheme.onSurface

    LaunchedEffect(Unit) {
        val savedSplit = trip.expenseSplits.lastOrNull()
        val contributionsMap =
            savedSplit?.contributions?.associate { it.user to it.amount.toInt() } ?: emptyMap()
        viewModel.setMembersWithContribution(members.map { it.user._id }, contributionsMap)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column {
            CustomTopBar(
                title = "EXPENSE INPUT",
                subtitle = trip.destination?.uppercase() ?: "UNKNOWN ZONE",
                icon = Icons.Rounded.RequestQuote,
                onBackClick = onNavigateBack,
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    top = 10.dp,
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 120.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    Text(
                        text = "DECLARE INDIVIDUAL EXPENDITURES",
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                        ),
                        color = dynamicColor.copy(alpha = 0.4f),
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                }

                items(members) { member ->
                    val payment = payments.find { it.user == member.user._id }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, dynamicColor.copy(alpha = 0.06f)),
                        color = dynamicColor.copy(alpha = 0.03f),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Avatar(
                                imageUrl = member.user.profilePic,
                                size = 40.dp,
                            )

                            Spacer(Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = member.user.fullname.uppercase(),
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp,
                                    ),
                                    color = dynamicColor.copy(alpha = 0.9f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }

                            OutlinedTextField(
                                value = payment?.amount?.toString() ?: "",
                                onValueChange = {
                                    viewModel.setExpense(
                                        member.user._id,
                                        it.toIntOrNull() ?: 0,
                                    )
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier
                                    .width(110.dp)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                textStyle = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = dynamicColor,
                                ),
                                prefix = {
                                    Text(
                                        text = "₹ ",
                                        color = TealCyan,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = TealCyan,
                                    unfocusedBorderColor = dynamicColor.copy(alpha = 0.1f),
                                    focusedContainerColor = dynamicColor.copy(alpha = 0.05f),
                                    unfocusedContainerColor = Color.Transparent,
                                    cursorColor = TealCyan,
                                ),
                            )
                        }
                    }
                }

                val showStoredSettlement =
                    uiState !is BillSplitViewModel.BillSplitState.Success && uiState !is BillSplitViewModel.BillSplitState.Loading

                if (showStoredSettlement && trip.expenseSplits.isNotEmpty()) {
                    val storedSplit = trip.expenseSplits.last()
                    val contributionsMap =
                        storedSplit.contributions.associate { it.user to it.amount.toInt() }

                    item {
                        SettlementDirectiveCard(
                            title = "CURRENT SETTLEMENT DIRECTIVE",
                            totalExpense = storedSplit.totalExpense?.toInt() ?: 0,
                            perPerson = storedSplit.perPerson?.toInt() ?: 0,
                            contributions = contributionsMap,
                            settlements = storedSplit.settlements.map {
                                Triple(
                                    it.from,
                                    it.to,
                                    it.amount.toInt(),
                                )
                            },
                            members = members,
                            dynamicColor = dynamicColor,
                        )
                    }
                }

                when (uiState) {
                    is BillSplitViewModel.BillSplitState.Loading -> {
                        item {
                            LinearProgressIndicator(
                                color = TealCyan,
                                trackColor = TealCyan.copy(alpha = 0.2f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(50)),
                            )
                        }
                    }

                    is BillSplitViewModel.BillSplitState.Success -> {
                        val result = (uiState as BillSplitViewModel.BillSplitState.Success).data
                        val contributionsMap =
                            result.contributions.associate { it.user to it.amount.toInt() }

                        item {
                            SettlementDirectiveCard(
                                title = "UPDATED SETTLEMENT DIRECTIVE",
                                totalExpense = result.totalExpense,
                                perPerson = result.perPerson,
                                contributions = contributionsMap,
                                settlements = result.settlements?.filterNotNull()?.map {
                                    Triple(it.from, it.to, it.amount.toInt())
                                } ?: emptyList(),
                                members = members,
                                dynamicColor = dynamicColor,
                            )
                        }
                    }

                    is BillSplitViewModel.BillSplitState.Error -> {
                        item {
                            CustomErrorMessage(
                                message = (uiState as BillSplitViewModel.BillSplitState.Error).message.uppercase(),
                            )
                        }
                    }

                    else -> {}
                }
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
            border = BorderStroke(1.dp, dynamicColor.copy(alpha = 0.05f)),
        ) {
            Button(
                onClick = { viewModel.getBillSplit(trip._id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 54.dp, vertical = 10.dp)
                    .height(65.dp)
                    .navigationBarsPadding(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealCyan),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
            ) {
                Text(
                    text = "INITIATE SETTLEMENT PROTOCOL",
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp,
                    fontSize = 15.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun SettlementDirectiveCard(
    title: String,
    totalExpense: Int,
    perPerson: Int,
    contributions: Map<String, Int>,
    settlements: List<Triple<String, String, Int>>,
    members: List<MemberDto>,
    dynamicColor: Color,
) {
    Spacer(Modifier.height(8.dp))
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = TealCyan.copy(alpha = 0.05f),
        border = BorderStroke(1.dp, TealCyan.copy(alpha = 0.3f)),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = TealCyan,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                    ),
                    color = TealCyan,
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(
                        "TOTAL EXPENDITURE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = dynamicColor.copy(0.5f),
                        letterSpacing = 1.sp,
                    )
                    Text(
                        "₹$totalExpense",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = dynamicColor,
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "EQUALIZED SHARE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = dynamicColor.copy(0.5f),
                        letterSpacing = 1.sp,
                    )
                    Text(
                        "₹$perPerson",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = dynamicColor,
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = TealCyan.copy(alpha = 0.3f))
            Spacer(Modifier.height(12.dp))

            Text(
                text = "RECORDED CONTRIBUTIONS",
                style = TextStyle(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp,
                ),
                color = TealCyan.copy(alpha = 0.8f),
            )
            Spacer(Modifier.height(8.dp))

            contributions.forEach { (userId, amount) ->
                val user = members.find { it.user._id == userId }?.user
                if (user != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Avatar(
                            imageUrl = user.profilePic,
                            size = 26.dp,
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = user.fullname.uppercase(),
                            style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                            color = dynamicColor.copy(alpha = 0.7f),
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = "₹ $amount",
                            style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
                            color = dynamicColor,
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = TealCyan.copy(alpha = 0.3f))
            Spacer(Modifier.height(12.dp))

            Text(
                text = "TRANSACTION DIRECTIVES",
                style = TextStyle(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp,
                ),
                color = TealCyan.copy(alpha = 0.8f),
            )
            Spacer(Modifier.height(8.dp))

            if (settlements.isEmpty()) {
                Text(
                    text = "ACCOUNTS BALANCED. NO TRANSACTIONS PENDING.",
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                    ),
                    color = dynamicColor.copy(alpha = 0.5f),
                    modifier = Modifier.padding(top = 8.dp),
                )
            } else {
                settlements.forEach { (fromId, toId, amount) ->
                    val fromUser = members.find { m -> m.user._id == fromId }?.user
                    val toUser = members.find { m -> m.user._id == toId }?.user

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = dynamicColor.copy(alpha = 0.02f),
                        border = BorderStroke(1.dp, dynamicColor.copy(alpha = 0.05f)),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Avatar(
                                    imageUrl = fromUser?.profilePic,
                                    size = 32.dp,
                                )
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = fromUser?.fullname?.uppercase() ?: "UNKNOWN",
                                        style = TextStyle(
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                        ),
                                        color = dynamicColor.copy(alpha = 0.9f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.width(75.dp),
                                    )
                                }
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 8.dp),
                            ) {
                                Text(
                                    "₹$amount",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Black,
                                    ),
                                    color = WarningYellow,
                                )
                                Icon(
                                    Icons.AutoMirrored.Rounded.ArrowForward,
                                    contentDescription = null,
                                    tint = dynamicColor.copy(alpha = 0.3f),
                                    modifier = Modifier.size(16.dp),
                                )
                            }

                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,
                            ) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = toUser?.fullname?.uppercase() ?: "UNKNOWN",
                                        style = TextStyle(
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                        ),
                                        color = TealCyan,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.width(75.dp),
                                    )
                                }
                                Spacer(Modifier.width(8.dp))
                                Avatar(
                                    imageUrl = toUser?.profilePic,
                                    size = 32.dp,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
