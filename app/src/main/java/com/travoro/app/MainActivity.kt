package com.travoro.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.travoro.app.data.remote.api.TravelApiService
import com.travoro.app.di.Session
import com.travoro.app.rootNavigation.NavGraph
import com.travoro.app.ui.theme.TravelAppTheme
import com.travoro.app.ui.utils.RequestLocationPermission
import com.travoro.app.ui.utils.RequestNotificationPermission
import com.travoro.app.ui.utils.darkMode.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

enum class PermissionStep {
    REQUESTING_LOCATION, REQUESTING_NOTIFICATION, FINISHED,
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var travelApiService: TravelApiService
    @Inject
    lateinit var session: Session
    @Inject
    lateinit var appInitializer: AppInitializer

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val rootNavController = rememberNavController()
            var permissionStep by remember { mutableStateOf(PermissionStep.REQUESTING_LOCATION) }
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val isDark by themeViewModel.isDark.collectAsStateWithLifecycle()

            TravelAppTheme(darkTheme = isDark) {
                when (permissionStep) {
                    PermissionStep.REQUESTING_LOCATION -> {
                        RequestLocationPermission(
                            onLocPermissionGranted = {
                                permissionStep = PermissionStep.REQUESTING_NOTIFICATION
                            },
                            onLocPermissionDenied = {
                                permissionStep = PermissionStep.REQUESTING_NOTIFICATION
                            },
                        )
                    }

                    PermissionStep.REQUESTING_NOTIFICATION -> {
                        RequestNotificationPermission(
                            onNotificationPermissionGranted = {
                                permissionStep = PermissionStep.FINISHED
                            },
                            onNotificationPermissionDenied = {
                                permissionStep = PermissionStep.FINISHED
                            },
                        )
                    }

                    PermissionStep.FINISHED -> {
                        val scope = rememberCoroutineScope()
                        LaunchedEffect(Unit) {
                            if (!session.getToken().isNullOrBlank()) {
                                appInitializer.initializeApp(scope)
                            }
                        }

                        NavGraph(
                            rootNavController = rootNavController,
                            session = session,
                            themeViewModel = themeViewModel,
                        )
                    }
                }
            }
        }
    }
}
