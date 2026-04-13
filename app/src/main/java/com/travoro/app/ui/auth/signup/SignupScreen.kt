package com.travoro.app.ui.auth.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import com.travoro.app.ui.components.AnimatedTravoroTitle
import com.travoro.app.ui.components.AuthInputText
import com.travoro.app.ui.components.BackRoundButton
import com.travoro.app.ui.components.ErrorAlertDialog
import com.travoro.app.ui.theme.MidnightBlue
import com.travoro.app.ui.theme.TealCyan
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpScreen(
    rootNavController: NavController,
    viewModel: SignupViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val name by viewModel.fullName.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.train))

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest {
            when (it) {
                is SignupViewModel.SignUpNavigation.NavigationToHome -> {
                    rootNavController.navigate(Home)
                }
            }
        }
    }

    if (uiState is SignupViewModel.SignUpEvent.Error) {
        ErrorAlertDialog(
            message = (uiState as SignupViewModel.SignUpEvent.Error).message,
            onDismiss = { viewModel.onClearError() },
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightBlue),
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter),
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        1f to MidnightBlue,
                    ),
                ),
        )

        BackRoundButton(onClick = { rootNavController.popBackStack() })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Column {
                Text(
                    text = "Begin Your Journey",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.inknutantiquamedium)),
                )

                Box(modifier = Modifier.offset(y = (-40).dp)) {
                    AnimatedTravoroTitle()
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Create your travel hub to plan trips, collaborate with friends, and explore the world effortlessly.",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 15.sp,
                lineHeight = 22.sp,
                modifier = Modifier.padding(end = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(TealCyan.copy(alpha = 0.05f))
                    .border(1.dp, TealCyan.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
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
                    text = "AI ENGINE STANDING BY",
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                    ),
                    color = TealCyan,
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = ">_ INITIALIZING NEW COMMANDER",
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                    ),
                    color = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.padding(bottom = 16.dp),
                )

                AuthInputText(
                    value = name,
                    onValueChange = viewModel::onNameChange,
                    placeholder = "Full Name",
                    label = "What should we call you?",
                    keyboardOption = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(20.dp))

                AuthInputText(
                    value = email,
                    onValueChange = viewModel::onEmailChange,
                    placeholder = "Email Address",
                    label = "Where should we reach you?",
                    keyboardOption = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(20.dp))

                AuthInputText(
                    value = password,
                    onValueChange = viewModel::onPasswordChange,
                    placeholder = "Create password",
                    label = "Your secret key",
                    keyboardOption = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = viewModel::onSignUpButtonClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TealCyan),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                ) {
                    if (uiState is SignupViewModel.SignUpEvent.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(24.dp),
                        )
                    } else {
                        Text(
                            text = "Create your journey",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 0.5.sp,
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, bottom = 24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "AUTHORIZED PERSONNEL? ",
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                        ),
                        color = Color.White.copy(alpha = 0.4f),
                    )
                    Text(
                        text = "SECURE LOGIN",
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp,
                        ),
                        color = TealCyan,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { rootNavController.popBackStack() }
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                    )
                }
            }
        }
    }
}
