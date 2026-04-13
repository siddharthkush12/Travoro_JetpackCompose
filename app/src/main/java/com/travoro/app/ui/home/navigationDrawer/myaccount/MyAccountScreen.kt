package com.travoro.app.ui.home.navigationDrawer.myaccount

import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.travoro.app.di.Session
import com.travoro.app.di.SocketManager
import com.travoro.app.rootNavigation.Home
import com.travoro.app.rootNavigation.Login
import com.travoro.app.ui.components.CustomTopBar
import com.travoro.app.ui.home.homeNavigation.MyProfileTab
import com.travoro.app.ui.theme.ErrorRed
import com.travoro.app.ui.theme.TealCyan
import com.travoro.app.ui.theme.TealCyanDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAccountScreen(
    navController: NavController,
    viewModel: MyAccountViewModel = hiltViewModel(),
    rootNavController: NavController,
    session: Session,
) {
    val sheetState = rememberModalBottomSheetState()
    val showBottomSheet = remember { mutableStateOf(false) }
    val showChangePasswordDialog = remember { mutableStateOf(false) }
    val showDeleteDialog = remember { mutableStateOf(false) }
    val showLogoutDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        CustomTopBar(
            title = "MY ACCOUNT",
            icon = Icons.Rounded.VerifiedUser,
            onBackClick = { navController.popBackStack() },
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.02f),
                ),
                shadowElevation = 2.dp,
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(86.dp)
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                brush = Brush.sweepGradient(
                                    listOf(
                                        TealCyanDark,
                                        TealCyan.copy(alpha = 0.2f),
                                        TealCyanDark,
                                    ),
                                ),
                                shape = CircleShape,
                            )
                            .padding(4.dp),
                    ) {
                        AsyncImage(
                            model = session.getProfileImage() ?: "",
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "SIDDHARTH KUSHWAHA",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp,
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Spacer(Modifier.width(6.dp))

                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(TealCyan),
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        ContactDataRow(icon = Icons.Rounded.PhoneIphone, text = "+91 7380339254")

                        Spacer(modifier = Modifier.height(4.dp))
                        ContactDataRow(
                            icon = Icons.Rounded.Email,
                            text = "siddharthkush12@gmail.com",
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            modifier = Modifier.clickable { navController.navigate(MyProfileTab) },
                            shape = RoundedCornerShape(8.dp),
                            color = TealCyan.copy(alpha = 0.1f),
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    Icons.Rounded.Edit,
                                    contentDescription = null,
                                    tint = TealCyan,
                                    modifier = Modifier.size(12.dp),
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "EDIT PROFILE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TealCyan,
                                    letterSpacing = 1.sp,
                                )
                            }
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "ACCESS PROTOCOLS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.padding(start = 8.dp),
                )

                SystemActionTile(
                    title = "ACCOUNT MANAGEMENT",
                    icon = Icons.Rounded.Security,
                    iconTint = TealCyan,
                    onClick = { showBottomSheet.value = true },
                )

                SystemActionTile(
                    title = "TERMINATE SESSION",
                    icon = Icons.Rounded.PowerSettingsNew,
                    iconTint = ErrorRed,
                    onClick = { showLogoutDialog.value = true },
                )
            }
        }

        if (showBottomSheet.value) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet.value = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "SECURITY OVERRIDE",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    BottomSheetOption(
                        icon = Icons.Rounded.VpnKey,
                        title = "Update Passkey",
                        color = TealCyan,
                        onClick = {
                            showChangePasswordDialog.value = true
                            showBottomSheet.value = false
                        },
                    )

                    BottomSheetOption(
                        icon = Icons.Rounded.DeleteForever,
                        title = "Delete Account",
                        color = ErrorRed,
                        onClick = {
                            showDeleteDialog.value = true
                            showBottomSheet.value = false
                        },
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        if (showChangePasswordDialog.value) {
            val oldPassword by viewModel.oldPassword.collectAsStateWithLifecycle()
            val newPassword by viewModel.newPassword.collectAsStateWithLifecycle()

            SystemDialog(
                title = "UPDATE PASSKEY",
                icon = Icons.Rounded.LockReset,
                iconTint = TealCyan,
                onDismiss = {
                    showChangePasswordDialog.value = false
                },
                onConfirm = {
                    viewModel.changePassword()

                    showChangePasswordDialog.value = false
                },
                confirmText = "UPDATE",
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    OutlinedTextField(
                        value = oldPassword,
                        onValueChange = {
                            viewModel.onOldPasswordChange(it)
                        },
                        label = {
                            Text("Current Passkey")
                        },
                        placeholder = {
                            Text("Enter current...")
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = {
                            viewModel.onNewPasswordChange(it)
                        },
                        label = {
                            Text("New Passkey")
                        },
                        placeholder = {
                            Text("Enter new...")
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }

        if (showDeleteDialog.value) {
            val context = LocalContext.current
            SystemDialog(
                title = "DELETE ACCOUNT",
                icon = Icons.Rounded.Warning,
                iconTint = ErrorRed,
                onDismiss = { showDeleteDialog.value = false },
                onConfirm = {
                    Toast.makeText(
                        context,
                        "Request for account deletion is successfully submitted it will deleted within 7 days",
                        Toast.LENGTH_LONG,
                    ).show()
                },
                confirmText = "DELETE DATA",
                confirmColor = ErrorRed,
            ) {
                Text(
                    text = "This action is irreversible. All architecture, itineraries, and preferences will be permanently wiped from the Vani Core.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        if (showLogoutDialog.value) {
            SystemDialog(
                title = "LOGOUT SESSION",
                icon = Icons.AutoMirrored.Rounded.ExitToApp,
                iconTint = TealCyan,
                onDismiss = { showLogoutDialog.value = false },
                onConfirm = {
                    SocketManager.disconnect()
                    session.removeToken()
                    session.removeUserId()
                    session.removeProfileImage()
                    rootNavController.navigate(Login) { popUpTo(Home) { inclusive = true } }
                },
                confirmText = "LOGOUT",
            ) {
                Text(
                    text = "Are you sure you want to disconnect from the Vani System?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
fun ContactDataRow(
    icon: ImageVector,
    text: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(14.dp),
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
        )
    }
}

@Composable
fun SystemActionTile(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
            ),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.02f)),
        shadowElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconTint.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp),
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            )
        }
    }
}

@Composable
fun BottomSheetOption(
    icon: ImageVector,
    title: String,
    color: Color,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
fun SystemDialog(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    confirmText: String,
    confirmColor: Color = TealCyan,
    content: @Composable () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                    ),
                )
            }
        },
        text = { content() },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = confirmColor),
            ) {
                Text(
                    text = confirmText,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    color = Color.White,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "CANCEL",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                )
            }
        },
    )
}
