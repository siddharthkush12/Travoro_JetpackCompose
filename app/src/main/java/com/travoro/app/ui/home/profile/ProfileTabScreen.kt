package com.travoro.app.ui.home.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.travoro.app.ui.components.CustomDatePickerField
import com.travoro.app.ui.components.CustomDropdownField
import com.travoro.app.ui.components.CustomEditText
import com.travoro.app.ui.components.CustomTopBar
import com.travoro.app.ui.theme.TealCyan
import com.travoro.app.ui.utils.uriToMultipart

@Composable
fun ProfileTabScreen(
    profileViewModel: ProfileViewModel,
    onNavigateBack: () -> Unit,
) {
    val profile by profileViewModel.profile.collectAsStateWithLifecycle()
    val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri -> selectedImageUri = uri }

    LaunchedEffect(Unit) {
        profileViewModel.fetchProfile()
    }

    var fullname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var profilePic by remember { mutableStateOf("") }

    LaunchedEffect(profile) {
        profile?.let {
            profilePic = it.profilePic ?: ""
            fullname = it.fullname
            email = it.email
            dob = it.dob ?: ""
            phone = it.phone ?: ""
            gender = it.gender ?: ""
            city = it.city ?: ""
            state = it.state ?: ""
            country = it.country ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        CustomTopBar(
            title = "USER IDENTITY",
            icon = Icons.Rounded.VerifiedUser,
            onBackClick = onNavigateBack,
        )

        if (uiState is ProfileViewModel.ProfileEvent.Loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = TealCyan, strokeWidth = 2.dp)
            }
            return
        }

        profile?.let {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                ProfileImageCard(
                    profilePic = selectedImageUri ?: profilePic,
                    onImageClick = { launcher.launch("image/*") },
                )

                SectionCard(title = "CORE IDENTITY") {
                    GeneralDetails(
                        fullname,
                        dob,
                        gender,
                        { fullname = it },
                        { dob = it },
                        { gender = it },
                    )
                }

                SectionCard(title = "COMMUNICATION") {
                    ContactDetails(phone, email, { phone = it })
                }

                SectionCard(title = "LOCATION") {
                    AddressDetails(
                        city,
                        state,
                        country,
                        { city = it },
                        { state = it },
                        { country = it },
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val imagePart = selectedImageUri?.let { uriToMultipart(context, it) }
                        profileViewModel.updateProfile(
                            imagePart,
                            fullname,
                            dob,
                            gender,
                            phone,
                            city,
                            state,
                            country,
                        )
                        profileViewModel.fetchProfile()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TealCyan),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                ) {
                    Text(
                        "UPDATE IDENTITY DATA",
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    color = TealCyan,
                ),
            )
            Spacer(modifier = Modifier.height(20.dp))
            content()
        }
    }
}

@Composable
fun ProfileImageCard(
    profilePic: Any?,
    onImageClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Box {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        brush = Brush.sweepGradient(
                            listOf(
                                TealCyan, Color(0xFF00D9F5), TealCyan
                            )
                        ),
                        shape = CircleShape,
                    )
                    .padding(6.dp),
            ) {
                AsyncImage(
                    model = profilePic,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-4).dp, y = (-4).dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape)
                    .padding(2.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(TealCyan)
                        .clickable { onImageClick() },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CameraAlt,
                        contentDescription = "Edit Profile",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun ContactDetails(
    phone: String,
    email: String,
    onPhoneChange: (String) -> Unit,
) {
    val dynamicColor = MaterialTheme.colorScheme.onSurface

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        CustomEditText(
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Encrypted Phone") },
            value = phone,
            onValueChange = onPhoneChange,
        )

        Column {
            OutlinedTextField(
                value = email,
                onValueChange = {},
                readOnly = true,
                label = { Text("System Email (Locked)", fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = dynamicColor.copy(alpha = 0.3f),
                        modifier = Modifier.size(18.dp),
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = dynamicColor.copy(alpha = 0.1f),
                    unfocusedBorderColor = dynamicColor.copy(alpha = 0.1f),
                    focusedLabelColor = dynamicColor.copy(alpha = 0.4f),
                    unfocusedLabelColor = dynamicColor.copy(alpha = 0.4f),
                    focusedContainerColor = dynamicColor.copy(alpha = 0.02f),
                    unfocusedContainerColor = dynamicColor.copy(alpha = 0.02f),
                    focusedTextColor = dynamicColor.copy(alpha = 0.5f),
                    unfocusedTextColor = dynamicColor.copy(alpha = 0.5f),
                ),
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Primary email is permanently bound to the Core System.",
                fontSize = 11.sp,
                color = dynamicColor.copy(alpha = 0.5f),
                modifier = Modifier.padding(start = 4.dp),
            )
        }
    }
}

@Composable
fun GeneralDetails(
    fullname: String,
    dob: String,
    gender: String,
    onFullNameChange: (String) -> Unit,
    onDOBChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        CustomEditText(
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Designation (Full Name)") },
            value = fullname,
            onValueChange = onFullNameChange,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CustomDatePickerField(
                modifier = Modifier.weight(1.5f),
                selectedDate = dob,
                onDateSelected = onDOBChange,
            )
            CustomDropdownField(
                modifier = Modifier.weight(1f),
                selectedValue = gender,
                options = listOf("Male", "Female", "Other"),
                label = "Identity",
                onValueChange = onGenderChange,
            )
        }
    }
}

@Composable
fun AddressDetails(
    city: String,
    state: String,
    country: String,
    onCityChange: (String) -> Unit,
    onStateChange: (String) -> Unit,
    onCountryChange: (String) -> Unit,
) {
    val indianStatesAndUTs = listOf(
        "Andaman and Nicobar Islands",
        "Andhra Pradesh",
        "Arunachal Pradesh",
        "Assam",
        "Bihar",
        "Chandigarh",
        "Chhattisgarh",
        "Dadra and Nagar Haveli and Daman and Diu",
        "Delhi",
        "Goa",
        "Gujarat",
        "Haryana",
        "Himachal Pradesh",
        "Jammu and Kashmir",
        "Jharkhand",
        "Karnataka",
        "Kerala",
        "Ladakh",
        "Lakshadweep",
        "Madhya Pradesh",
        "Maharashtra",
        "Manipur",
        "Meghalaya",
        "Mizoram",
        "Nagaland",
        "Odisha",
        "Puducherry",
        "Punjab",
        "Rajasthan",
        "Sikkim",
        "Tamil Nadu",
        "Telangana",
        "Tripura",
        "Uttar Pradesh",
        "Uttarakhand",
        "West Bengal",
    )
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CustomEditText(
                modifier = Modifier.weight(1f),
                label = { Text("City") },
                value = city,
                onValueChange = onCityChange,
            )
            CustomDropdownField(
                modifier = Modifier.weight(1f),
                selectedValue = state,
                options = indianStatesAndUTs,
                label = "State/Province",
                onValueChange = onStateChange,
            )
        }
        CustomEditText(
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Global Region (Country)") },
            value = country,
            onValueChange = onCountryChange,
        )
    }
}
