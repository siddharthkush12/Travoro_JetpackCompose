package com.example.travelapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.travelapp.R
import com.example.travelapp.ui.auth.login.LoginViewModel
import com.example.travelapp.ui.theme.MidnightBlue
import com.example.travelapp.ui.theme.TealCyan
import kotlinx.coroutines.delay

@Composable
fun AuthInputText(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    label: String,
    keyboardOption: KeyboardOptions,
    modifier: Modifier
) {

    val isPasswordField = keyboardOption.keyboardType == KeyboardType.Password
    var passwordVisible by remember { mutableStateOf(false) }

    val dynamicColor = MaterialTheme.colorScheme.onSurface

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                style = TextStyle(fontSize = 14.sp)
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                color = dynamicColor.copy(alpha = 0.3f)
            )
        },
        maxLines = 1,
        singleLine = true,
        textStyle = TextStyle(
            color = dynamicColor,
            fontSize = 16.sp
        ),
        shape = RoundedCornerShape(20.dp),

        visualTransformation = if (isPasswordField && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },

        trailingIcon = if (isPasswordField) {
            {
                val image = if (passwordVisible) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = "Toggle password visibility",
                        tint = if (passwordVisible) TealCyan else dynamicColor.copy(alpha = 0.5f)
                    )
                }
            }
        } else null,

        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = TealCyan,
            unfocusedBorderColor = dynamicColor.copy(alpha = 0.2f),
            focusedLabelColor = TealCyan,
            unfocusedLabelColor = dynamicColor.copy(alpha = 0.6f),
            cursorColor = TealCyan,
            focusedContainerColor = dynamicColor.copy(alpha = 0.05f),
            unfocusedContainerColor = dynamicColor.copy(alpha = 0.05f),
        ),
        keyboardOptions = keyboardOption,
        modifier = modifier
    )
}


@Composable
fun AnimatedTravoroTitle() {

    val word = "Travoro"
    var visibleText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            for (i in 1..word.length) {
                visibleText = word.substring(0, i)
                delay(125)
            }

            delay(1400)

            for (i in word.length downTo 0) {
                visibleText = word.substring(0, i)
                delay(125)
            }
            delay(250)
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "with ",
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inknutantiquamedium))
        )

        Text(
            text = visibleText,
            color = TealCyan,
            fontSize = 34.sp,
            letterSpacing = 5.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily(Font(R.font.inknutantiquamedium))
        )
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgetPasswordSheet(
    viewModel: LoginViewModel,
    onDismiss: () -> Unit
) {

    val forgetEmail by viewModel.forgetEmail.collectAsStateWithLifecycle()
    val forgotState by viewModel.forgotState.collectAsStateWithLifecycle()

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.email))

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MidnightBlue,
        shape = RoundedCornerShape(
            topStart = 35.dp,
            topEnd = 35.dp
        ),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .height(9.dp)
                    .width(50.dp)
                    .background(
                        Color.White,
                        RoundedCornerShape(50)
                    )
            )
        }
    ) {

        if (forgotState is LoginViewModel.ForgotPasswordState.Success) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimation(
                        composition = composition,
                        iterations = 1,
                        modifier = Modifier.size(170.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Check your inbox",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Password reset link sent successfully",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 14.sp
                    )
                }
            }

            LaunchedEffect(Unit) {
                delay(3500)
                viewModel.clearForgetPasswordState()
                onDismiss()
            }
            return@ModalBottomSheet
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 20.dp)
        ) {
            Text(
                text = "Reset your Password",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Enter your email and we'll guide you back on track.",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            AuthInputText(
                value = forgetEmail,
                onValueChange = viewModel::onForgetEmailChange,
                placeholder = "Where should we send the link?",
                label = "Email Address",
                keyboardOption = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(28.dp))


            Button(
                onClick = viewModel::onForgetPasswordButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TealCyan
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp
                ),
                enabled = forgotState !is LoginViewModel.ForgotPasswordState.Loading
            ) {

                if (forgotState is LoginViewModel.ForgotPasswordState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.5.dp,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Text(
                        text = "Send Link to Email",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (forgotState is LoginViewModel.ForgotPasswordState.Error) {
                Text(
                    text = (forgotState as LoginViewModel.ForgotPasswordState.Error).message,
                    color = Color.Red,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(25.dp))
        }
    }
}