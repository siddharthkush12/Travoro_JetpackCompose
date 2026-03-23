package com.example.travelapp.ui.home.navigationDrawer

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
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.travelapp.R
import com.example.travelapp.data.remote.dto.home.profile.Data
import com.example.travelapp.di.Session
import com.example.travelapp.ui.home.homeNavigation.*
import com.example.travelapp.ui.home.profile.ProfileViewModel
import com.example.travelapp.ui.theme.MidnightBlue
import com.example.travelapp.ui.theme.Purple80
import com.example.travelapp.ui.theme.TealCyan
import com.example.travelapp.ui.theme.TealCyanDark
import com.example.travelapp.ui.theme.TealCyanLight
import com.example.travelapp.ui.utils.DarkMode.ThemeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawer(
    homeNavController: NavController,
    rootNavController: NavController,
    session: Session,
    drawerState: DrawerState,
    viewModel: ProfileViewModel,
    themeViewModel: ThemeViewModel
) {

    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.background,
        drawerTonalElevation = 0.dp
    ) {

        val profile by viewModel.profile.collectAsStateWithLifecycle()
        val scope = rememberCoroutineScope()


        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            AppNameTextNavigationDrawer()

            ProfileNavigationDrawer(homeNavController, drawerState, profile)

            MyAccountNavigationDrawer(homeNavController, drawerState, scope)


            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                DrawerSectionGroup(title = "My Journeys") {
                    DrawerMenuItem(
                        icon = Icons.Rounded.Badge,
                        label = "View/Manage Trips",
                        onClick = { }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = DividerDefaults.Thickness,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.09f)
                    )
                    DrawerMenuItem(
                        icon = Icons.Rounded.FavoriteBorder,
                        label = "Wishlist",
                        onClick = { }
                    )
                }

                DrawerSectionGroup(title = "Preferences") {
                    SettingNavigationDrawer(themeViewModel)
                }

                DrawerSectionGroup(title = "About") {
                    DrawerMenuItem(
                        icon = Icons.Rounded.Code,
                        label = "About Developer",
                        onClick = {
                            homeNavController.navigate(DeveloperTab)
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            AppDetailNavigationDrawer()

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


@Composable
fun DrawerSectionGroup(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.09f)),
            shadowElevation = 2.dp
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun DrawerMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    trailingContent: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(20.dp)
        )
    }
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(TealCyan.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TealCyan,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        trailingContent()
    }
}


@Composable
fun ProfileNavigationDrawer(
    homeNavController: NavController,
    drawerState: DrawerState,
    profile: Data?
) {
    val scope = rememberCoroutineScope()
    var profilePic by remember { mutableStateOf("") }
    var fullname by remember { mutableStateOf("Loading...") }
    var email by remember { mutableStateOf("") }

    LaunchedEffect(profile) {
        profile?.let {
            profilePic = it.profilePic ?: ""
            fullname = it.fullname
            email = it.email
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                scope.launch {
                    homeNavController.navigate(MyProfileTab)
                    drawerState.close()
                }
            },
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(TealCyan.copy(alpha = 0.15f), Color.Transparent),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(2.dp, TealCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = profilePic,
                    contentDescription = "Profile Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = fullname,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = TealCyan
            )
        }
    }
}


@Composable
fun MyAccountNavigationDrawer(
    homeNavController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.09f)),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            QuickActionApplet(
                iconRes = Icons.Rounded.Person,
                label = "ACCOUNT",
                onClick = {
                    scope.launch {
                        homeNavController.navigate(MyAccount);
                        drawerState.close()
                    }
                }
            )

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(30.dp)
                    .background(Color.White.copy(alpha = 0.05f))
            )

            QuickActionApplet(
                iconRes = Icons.Rounded.SupportAgent,
                label = "SUPPORT",
                onClick = { scope.launch { homeNavController.navigate(Support); drawerState.close() } }
            )

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(30.dp)
                    .background(Color.White.copy(alpha = 0.05f))
            )

            QuickActionApplet(
                iconRes = Icons.Rounded.NotificationsActive,
                label = "NOTIFICATION",
                onClick = { scope.launch { homeNavController.navigate(Notification); drawerState.close() } }
            )
        }
    }
}


@Composable
fun QuickActionApplet(
    iconRes: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(TealCyan.copy(alpha = 0.1f))
                .border(1.dp, TealCyan.copy(alpha = 0.2f), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = iconRes,
                contentDescription = label,
                modifier = Modifier.size(23.dp),
                tint = TealCyan
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun SettingNavigationDrawer(
    themeViewModel: ThemeViewModel
) {

    val isDark by themeViewModel.isDark.collectAsStateWithLifecycle()

    DrawerMenuItem(
        icon = Icons.Rounded.DarkMode,
        label = "Dark Mode",
        onClick = { },
        trailingContent = {
            Switch(
                checked = isDark,
                onCheckedChange = { themeViewModel.toggleTheme() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = TealCyan,
                    checkedTrackColor = TealCyan.copy(alpha = 0.5f)
                )
            )
        }
    )


}


@Composable
fun AppDetailNavigationDrawer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Box(
            modifier = Modifier
                .width(48.dp)
                .height(2.dp)
                .clip(CircleShape)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent, TealCyan.copy(alpha = 0.4f), Color.Transparent
                        )
                    )
                )
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RATE APP",
                style = MaterialTheme.typography.labelMedium.copy(
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Black
                ),
                color = TealCyan,
                modifier = Modifier.clickable { }
            )

            Box(
                modifier = Modifier
                    .size(3.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
            )

            Text(
                text = "PRIVACY",
                style = MaterialTheme.typography.labelMedium.copy(
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Black
                ),
                color = TealCyan,
                modifier = Modifier.clickable { }
            )
        }


        Text(
            text = "TRAVORO BY KUSH v1.0.0",
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = 9.sp,
                letterSpacing = 3.sp,
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )
    }
}


@Composable
fun AppNameTextNavigationDrawer() {

    val coreGradient = Brush.linearGradient(
        colors = listOf(
            TealCyan,
            MidnightBlue.copy(alpha = 0.5f),
            TealCyanDark
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "TRAVORO",
                style = TextStyle(
                    brush = coreGradient,
                    fontSize = 38.sp,
                    letterSpacing = 6.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily(Font(R.font.inknutantiquamedium)),
                    shadow = Shadow(
                        color = MidnightBlue.copy(alpha = 0.4f),
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(TealCyanLight, CircleShape)
                    .shadow(8.dp, CircleShape, spotColor = TealCyanLight)
            )
            Text(
                text = "POWERED BY VANI(AI)",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    letterSpacing = 4.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            )
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(TealCyanLight, CircleShape)
                    .shadow(8.dp, CircleShape, spotColor = TealCyanLight)
            )
        }
    }
}