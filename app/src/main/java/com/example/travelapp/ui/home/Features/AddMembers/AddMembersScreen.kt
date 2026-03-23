package com.example.travelapp.ui.home.Features.AddMembers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.travelapp.ui.home.homeNavigation.ChatGroupTab
import com.example.travelapp.ui.theme.TealCyan


@Composable
fun AddMembersScreen(
    tripId: String,
    onNavigateBack: () -> Unit,
    viewModel: AddMembersViewModel = hiltViewModel(),
    navController: NavController

) {

    val members by viewModel.members.collectAsStateWithLifecycle()
    val phone by viewModel.memberPhone.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            TealCyan,
                            TealCyan.copy(alpha = 0.6f)
                        )
                    )
                )
                .padding(24.dp)
        ) {

            Column(
                modifier = Modifier.align(Alignment.Center).padding(top=10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Create Trip Group",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Add friends to this trip",
                    color = Color.White.copy(alpha = 0.8f)
                )

            }

        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                MemberInputCard(
                    value = phone,
                    onValueChange = {
                        viewModel.onPhoneChange(it)
                    }
                )
            }


            item {

                Button(
                    onClick = { viewModel.searchMember() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = TealCyan)
                ) {
                    Text("Add Member")
                }
            }
            items(members.size) { index ->
                val member = members[index]
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation =8.dp),
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            AsyncImage(
                                model = member.profilePic,
                                contentDescription = null,
                                modifier = Modifier.size(50.dp).clip(shape = CircleShape),
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Crop

                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(member.fullname, fontWeight = FontWeight.Bold)
                                Text(
                                    member.phone,
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.removeMember(member)
                            },
                            modifier = Modifier.height(34.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Remove",
                                fontSize = 12.sp
                            )
                        }

                    }

                }

            }

            item {

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        viewModel.addMembersToTrip(tripId)
                        navController.navigate(ChatGroupTab)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TealCyan
                    )
                ) {

                    Text(
                        text = "Create Trip Group",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@Composable
fun MemberInputCard(
    value: String,
    onValueChange: (String) -> Unit
) {

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation =8.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = TealCyan
            )

            Spacer(modifier = Modifier.width(12.dp))

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Enter phone number", fontSize = 14.sp)
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        }
    }
}
