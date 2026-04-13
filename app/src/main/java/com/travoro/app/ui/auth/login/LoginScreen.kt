package com.travoro.app.ui.auth.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Fingerprint
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
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
import com.travoro.app.R
import com.travoro.app.rootNavigation.Home
import com.travoro.app.rootNavigation.Login
import com.travoro.app.rootNavigation.SignUp
import com.travoro.app.ui.components.AnimatedTravoroTitle
import com.travoro.app.ui.components.AuthInputText
import com.travoro.app.ui.components.ErrorAlertDialog
import com.travoro.app.ui.components.ForgetPasswordSheet
import com.travoro.app.ui.theme.MidnightBlue
import com.travoro.app.ui.theme.TealCyan
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    rootNavController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
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
            onDismiss = { viewModel.clearError() },
        )
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MidnightBlue),
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier =
                Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter),
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.4f to MidnightBlue.copy(alpha = 0.8f),
                            1f to MidnightBlue,
                        ),
                    ),
        )

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.size(30.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(
                    text = "Unfold Your World",
                    color = Color.White.copy(alpha = 0.95f),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.inknutantiquamedium)),
                )

                Box(modifier = Modifier.offset(y = (-40).dp)) {
                    AnimatedTravoroTitle()
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Your digital passport to hidden gems, seamless planning, and the stories that define a lifetime.",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(end = 16.dp),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier =
                        Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(TealCyan.copy(alpha = 0.05f))
                            .border(1.dp, TealCyan.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Rounded.Memory,
                        contentDescription = null,
                        tint = TealCyan,
                        modifier = Modifier.size(14.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "AI ITINERARY ENGINE STANDING BY",
                        style =
                            TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp,
                            ),
                        color = TealCyan,
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(40.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                color = MidnightBlue.copy(alpha = 0.25f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 32.dp),
                ) {
                    Text(
                        text = ">_ AWAITING CREDENTIALS",
                        style =
                            TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                            ),
                        color = Color.White.copy(alpha = 0.4f),
                        modifier = Modifier.padding(bottom = 24.dp),
                    )

                    AuthInputText(
                        value = email,
                        onValueChange = viewModel::onEmailChange,
                        placeholder = "commander@travoro.com",
                        label = "CREDENTIAL ID",
                        keyboardOption = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    AuthInputText(
                        value = password,
                        onValueChange = viewModel::onPasswordChange,
                        placeholder = "••••••••",
                        label = "SECURITY KEY",
                        keyboardOption = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        TextButton(
                            onClick = { showForgetSheet = true },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        ) {
                            Text(
                                text = "Lost your way?",
                                color = TealCyan,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = viewModel::onLoginButtonClick,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TealCyan),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                    ) {
                        if (uiState is LoginViewModel.LoginEvent.Loading) {
                            CircularProgressIndicator(
                                color = MidnightBlue,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(24.dp),
                            )
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Rounded.Fingerprint,
                                    contentDescription = null,
                                    tint = MidnightBlue,
                                    modifier = Modifier.size(20.dp),
                                )
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    text = "INITIALIZE SEQUENCE",
                                    fontSize = 14.sp,
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
                    }

                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "NEW TO TRAVORO? ",
                            style =
                                TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                ),
                            color = Color.White.copy(alpha = 0.4f),
                        )
                        Text(
                            text = "JOIN THE EXPEDITION",
                            style =
                                TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp,
                                ),
                            color = TealCyan,
                            modifier =
                                Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .clickable { rootNavController.navigate(SignUp) }
                                    .padding(horizontal = 4.dp, vertical = 2.dp),
                        )
                    }
                }
            }
        }
    }

    if (showForgetSheet) {
        ForgetPasswordSheet(
            viewModel = viewModel,
            onDismiss = {
                viewModel.clearForgetPasswordState()
                showForgetSheet = !showForgetSheet
            },
        )
    }
}
