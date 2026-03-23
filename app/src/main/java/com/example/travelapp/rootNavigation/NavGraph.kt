package com.example.travelapp.rootNavigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.travelapp.di.Session
import com.example.travelapp.ui.auth.login.LoginScreen
import com.example.travelapp.ui.auth.signup.SignUpScreen
import com.example.travelapp.ui.home.HomeScreen
import com.example.travelapp.ui.onboarding.OnboardingScreen
import com.example.travelapp.ui.splash.SplashScreen
import com.example.travelapp.ui.utils.DarkMode.ThemeViewModel

@Composable
fun NavGraph(
    rootNavController: NavHostController,
    session: Session,
    themeViewModel: ThemeViewModel,
) {

    NavHost(
        navController = rootNavController,
        startDestination = SplashScreen,

        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(400))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400)
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(400))
        }
    ) {

        composable<SplashScreen> {
            SplashScreen(
                session = session,
                onNavigate = { destination ->
                    rootNavController.navigate(destination) {
                        popUpTo(SplashScreen) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Onboarding> {
            OnboardingScreen(
                onFinishOnboarding = {
                    rootNavController.navigate(Login) {
                        popUpTo(Onboarding) {
                            inclusive = true
                        }
                    }
                }
            )
        }


        composable<Login> {
            LoginScreen(rootNavController=rootNavController)
        }


        composable<SignUp> {
            SignUpScreen(rootNavController=rootNavController)
        }


        composable<Home>(
            enterTransition = {
                fadeIn(tween(500)) + scaleIn(initialScale = 0.9f, animationSpec = tween(500))
            },
            exitTransition = {
                fadeOut(tween(500)) + scaleOut(targetScale = 0.9f, animationSpec = tween(500))
            }
        ) {
            HomeScreen(
                rootNavController = rootNavController,
                session = session,
                themeViewModel = themeViewModel
            )
        }
    }
}