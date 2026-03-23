package com.example.travelapp.ui.home.Features.CreateTrips

import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.travelapp.data.remote.dto.trips.Profile
import com.example.travelapp.ui.components.BackRoundButton
import com.example.travelapp.ui.theme.TealCyan

@Composable
fun CreateTripScreen(
    viewModel: CreateTripViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {

    val title by viewModel.title.collectAsStateWithLifecycle()
    val destination by viewModel.destination.collectAsStateWithLifecycle()
    val description by viewModel.description.collectAsStateWithLifecycle()
    val phone by viewModel.memberPhone.collectAsStateWithLifecycle()
    val members by viewModel.members.collectAsStateWithLifecycle()
    val tripType by viewModel.tripType.collectAsStateWithLifecycle()
    val travelStyle by viewModel.travelStyle.collectAsStateWithLifecycle()
    val tripBudget by viewModel.tripBudget.collectAsStateWithLifecycle()


    val tripTypes = listOf("solo", "friends", "family", "couple")
    val travelStyles = listOf("adventure", "relax", "budget", "luxury", "nature", "spiritual")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 15.dp, vertical = 26.dp)
    ) {

        item {

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(28.dp)
            ) {

                BackRoundButton (
                    onClick = { onNavigateBack() }
                )

                Text(
                    text = "Create New Trip",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(Color.White),
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    OutlinedTextField(
                        value = title,
                        onValueChange = { viewModel.onTitleChange(it) },
                        label = { Text("Trip Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = destination,
                        onValueChange = { viewModel.onDestinationChange(it) },
                        label = { Text("Destination") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TripDropdown(
                        label = "Trip Type",
                        options = tripTypes,
                        selected = tripType,
                        onSelect = { viewModel.onTripTypeChange(it) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TripDropdown(
                        label = "Travel Style",
                        options = travelStyles,
                        selected = travelStyle,
                        onSelect = { viewModel.onTravelStyleChange(it) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { viewModel.onDescriptionChange(it) },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = tripBudget,
                        onValueChange = { viewModel.onBudgetChange(it.toIntOrNull() ?: 0) },
                        label = { Text("Trip Budget") },
                        modifier = Modifier.fillMaxWidth(),
                    )

                }

            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Add Members 👥",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    value = phone,
                    onValueChange = { viewModel.onMemberPhoneChange(it) },
                    label = { Text("Phone number") },
                    modifier = Modifier.weight(1f)
                )


                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    shape = RoundedCornerShape(12.dp),
                    onClick = { viewModel.searchMember() },
                    colors = ButtonDefaults.buttonColors(TealCyan)
                ) {
                    Text("Add")
                }

            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Trip Members",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(10.dp))

        }

        items(members) { member ->
            MemberItem(
                member = member,
                onRemove = { viewModel.removeMember(member) }
            )

        }


        item {

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                shape = RoundedCornerShape(16.dp),
                onClick = { viewModel.createTrip() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(TealCyan)
            ) {
                Text("Create Trip")
            }

            Spacer(modifier = Modifier.height(40.dp))

        }
    }
}

@Composable
fun MemberItem(
    member: Profile,
    onRemove: () -> Unit
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                if (member.profilePic != null) {

                    AsyncImage(
                        model = member.profilePic,
                        contentDescription = null,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                    )

                } else {

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )

                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = member.fullname,
                    style = MaterialTheme.typography.titleMedium
                )

            }

            IconButton(onClick = onRemove) {

                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "remove"
                )

            }

        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDropdown(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {

            options.forEach {

                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onSelect(it)
                        expanded = false
                    }
                )

            }

        }

    }
}