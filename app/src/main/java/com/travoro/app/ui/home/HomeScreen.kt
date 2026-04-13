package com.travoro.app.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.travoro.app.di.Session
import com.travoro.app.ui.home.dashboard.HomeViewModel
import com.travoro.app.ui.home.homeNavigation.HomeBottomNavigation
import com.travoro.app.ui.home.homeNavigation.HomeNavGraph
import com.travoro.app.ui.home.homeNavigation.HomeTab
import com.travoro.app.ui.home.navigationDrawer.NavigationDrawer
import com.travoro.app.ui.home.profile.ProfileViewModel
import com.travoro.app.ui.theme.TealCyan
import com.travoro.app.ui.utils.darkMode.ThemeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    rootNavController: NavController,
    session: Session,
    themeViewModel: ThemeViewModel,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val homeNavController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val city by homeViewModel.city.collectAsStateWithLifecycle()
    val profile by profileViewModel.profile.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        homeViewModel.fetchLocationAndCity()
    }

    LaunchedEffect(profile) {
        profile?.let {
            session.storeProfileImage(it.profilePic)
        }
    }

    val showTopBar = currentRoute?.contains("HomeTab") == true ||
            currentRoute?.contains("SearchTab") == true

    val showBottomBar = currentRoute?.contains("HomeTab") == true ||
            currentRoute?.contains("TravelAITab") == true ||
            currentRoute?.contains("MyTripsTab") == true ||
            currentRoute?.contains("ChatGroupTab") == true

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawer(
                homeNavController = homeNavController,
                rootNavController = rootNavController,
                session = session,
                drawerState = drawerState,
                profileViewModel = profileViewModel,
                themeViewModel = themeViewModel,
            )
        },
    ) {
        Scaffold(containerColor = MaterialTheme.colorScheme.background, topBar = {
            if (showTopBar) {
                val gradientFade = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                        MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                        Color.Transparent,
                    ),
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(gradientFade),
                ) {
                    TravoroTopBar(
                        city = city,
                        session,
                        onOpenDrawer = { scope.launch { drawerState.open() } },
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }, bottomBar = {
            if (showBottomBar) {
                HomeBottomBar(navController = homeNavController)
            }
        }) { paddingValues ->
            HomeNavGraph(
                homeNavController = homeNavController,
                profileViewModel = profileViewModel,
                homeViewModel = homeViewModel,
                paddingValues = paddingValues,
                session = session,
                rootNavController = rootNavController,
            )
        }
    }
}

@Composable
fun TravoroTopBar(
    city: String?,
    session: Session,
    onOpenDrawer: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onOpenDrawer,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                    CircleShape,
                ),
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menu",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp),
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Current Location",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = TealCyan,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = city ?: "Discovering...",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(2.dp, TealCyan, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model = session.getProfileImage(),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
fun HomeBottomBar(navController: NavHostController) {
    val items = listOf(
        HomeBottomNavigation.Home,
        HomeBottomNavigation.TravelAI,
        HomeBottomNavigation.Messages,
        HomeBottomNavigation.MyTrips,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 24.dp,
            )
            .height(72.dp),
    ) {
        GlassBackground(modifier = Modifier.matchParentSize())

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            items.forEach { item ->
                val selected = currentDestination?.route?.contains(
                    item.destination::class.simpleName ?: "",
                ) == true

                val iconScale by animateFloatAsState(
                    targetValue = if (selected) 1.15f else 1.0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow,
                    ),
                    label = "scale_anim",
                )

                val contentColor by animateColorAsState(
                    targetValue = if (selected) {
                        TealCyan
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(
                            alpha = 0.6f,
                        )
                    },
                    label = "color_anim",
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                if (!selected) {
                                    navController.navigate(item.destination) {
                                        popUpTo(HomeTab) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(24.dp)
                            .scale(iconScale),
                        tint = contentColor,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelSmall,
                        color = contentColor,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    )
                }
            }
        }
    }
}

@Composable
fun GlassBackground(modifier: Modifier = Modifier) {
    val surfaceColor = MaterialTheme.colorScheme.surface
    val glassColor = surfaceColor.copy(alpha = 0.85f)
    val borderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(glassColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(28.dp),
            ),
    )
}
