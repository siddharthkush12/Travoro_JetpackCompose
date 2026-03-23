package com.example.travelapp.ui.auth.login


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.example.travelapp.R
import com.example.travelapp.rootNavigation.Home
import com.example.travelapp.rootNavigation.Login
import com.example.travelapp.rootNavigation.SignUp
import com.example.travelapp.ui.components.AnimatedTravoroTitle
import com.example.travelapp.ui.components.AuthInputText
import com.example.travelapp.ui.components.ErrorAlertDialog
import com.example.travelapp.ui.components.ForgetPasswordSheet
import com.example.travelapp.ui.theme.MidnightBlue
import com.example.travelapp.ui.theme.TealCyan
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    rootNavController: NavController, viewModel: LoginViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    var showForgetSheet by remember { mutableStateOf(false) }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.train))


    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is LoginViewModel.LoginNavigation.NavigationToHome -> {
                    rootNavController.navigate(Home) { popUpTo(Login) { inclusive = true } }
                }
            }
        }
    }

    if (uiState is LoginViewModel.LoginEvent.Error) {
        ErrorAlertDialog(
            message = (uiState as LoginViewModel.LoginEvent.Error).message,
            onDismiss = { viewModel.clearError() })
    }

//  ------------------------------------------UI----------------------------------------------------

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightBlue)
    ) {

        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        1f to MidnightBlue,
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy((-35).dp)
            ) {
                Text(
                    text = "Unfold Your World",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.inknutantiquamedium))

                )

                AnimatedTravoroTitle()
            }

            Text(
                text = "Your digital passport to hidden gems, seamless planning, and the stories that define a lifetime.",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(40.dp))


            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                AuthInputText(
                    value = email,
                    onValueChange = viewModel::onEmailChange,
                    placeholder = "Email Address",
                    label = "Where should we reach you?",
                    keyboardOption = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                AuthInputText(
                    value = password,
                    onValueChange = viewModel::onPasswordChange,
                    placeholder = "Your Secret Key",
                    label = "Password",
                    keyboardOption = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            showForgetSheet = true
                        }
                    ) {
                        Text(
                            text = "Lost your way?",
                            color = TealCyan,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = viewModel::onLoginButtonClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TealCyan),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    if (uiState is LoginViewModel.LoginEvent.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(
                                24.dp
                            )
                        )
                    } else {
                        Text(
                            text = "Begin the Adventure",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Not a member of the travoro?",
                        color = Color.White.copy(alpha = 0.6f)
                    )
                    TextButton(
                        onClick = {
                            rootNavController.navigate(SignUp)
                        }
                    ) {
                        Text(
                            text = "Join us",
                            color = TealCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }


    if (showForgetSheet) {
        ForgetPasswordSheet(
            viewModel = viewModel, onDismiss = {
                viewModel.clearForgetPasswordState()
                showForgetSheet = false
            }
        )
    }
}