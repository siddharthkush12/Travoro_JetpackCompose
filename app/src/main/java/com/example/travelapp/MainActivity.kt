package com.example.travelapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.travelapp.core.network.safeApiCall
import com.example.travelapp.data.remote.api.TravelApiService
import com.example.travelapp.di.Session
import com.example.travelapp.di.SocketManager
import com.example.travelapp.rootNavigation.NavGraph
import com.example.travelapp.ui.theme.TravelAppTheme
import com.example.travelapp.ui.utils.DarkMode.ThemeViewModel
import com.example.travelapp.ui.utils.RequestLocationPermission
import com.example.travelapp.ui.utils.RequestNotificationPermission
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

enum class PermissionStep {
    REQUESTING_LOCATION,
    REQUESTING_NOTIFICATION,
    FINISHED
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
                            }
                        )
                    }

                    PermissionStep.REQUESTING_NOTIFICATION -> {
                        RequestNotificationPermission(
                            onNotificationPermissionGranted = {
                                permissionStep = PermissionStep.FINISHED
                            },
                            onNotificationPermissionDenied = {
                                permissionStep = PermissionStep.FINISHED
                            }
                        )
                    }

                    PermissionStep.FINISHED -> {
                        val scope=rememberCoroutineScope()
                        LaunchedEffect(Unit) {
                            if(!session.getToken().isNullOrBlank()){
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