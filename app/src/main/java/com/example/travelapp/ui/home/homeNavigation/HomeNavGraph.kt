package com.example.travelapp.ui.home.homeNavigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.travelapp.di.Session
import com.example.travelapp.ui.home.Features.AddMembers.AddMembersScreen
import com.example.travelapp.ui.home.Features.CreateTrips.CreateTripScreen
import com.example.travelapp.ui.home.Search.SearchTabScreen
import com.example.travelapp.ui.home.travelAI.TravelAITabScreen
import com.example.travelapp.ui.home.dashboard.HomeTabScreen
import com.example.travelapp.ui.home.dashboard.HomeViewModel
import com.example.travelapp.ui.home.message.ChatGroupScreen
import com.example.travelapp.ui.home.message.MessageScreen
import com.example.travelapp.ui.home.mytrips.MyTripsScreen
import com.example.travelapp.ui.home.navigationDrawer.notification.NotificationScreen
import com.example.travelapp.ui.home.navigationDrawer.support.SupportScreen
import com.example.travelapp.ui.home.navigationDrawer.myaccount.MyAccountScreen
import com.example.travelapp.ui.home.navigationDrawer.options.DeveloperScreen
import com.example.travelapp.ui.home.profile.ProfileTabScreen
import com.example.travelapp.ui.home.profile.ProfileViewModel


@Composable
fun HomeNavGraph(
    homeNavController: NavHostController,
    rootNavController: NavController,
    profileViewModel: ProfileViewModel,
    homeViewModel: HomeViewModel,
    paddingValues: PaddingValues,
    session: Session
){
    NavHost(
        navController=homeNavController,
        startDestination = HomeTab,

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

    ){
        composable<HomeTab>{
            HomeTabScreen(paddingValues,homeNavController = homeNavController,homeViewModel = homeViewModel,profileViewModel = profileViewModel)
        }
        composable<TravelAITab>{
            TravelAITabScreen(paddingValues=paddingValues,navController=homeNavController)
        }

        composable<MyTripsTab>{
            MyTripsScreen()
        }
        composable<SearchTab> {
            SearchTabScreen()
        }
        composable<MyProfileTab> {
            ProfileTabScreen(
                onNavigateBack = {homeNavController.popBackStack()}
            )
        }
        composable<MyAccount>{
            MyAccountScreen(navController = homeNavController,rootNavController = rootNavController,session = session)
        }

        composable<Notification>{
            NotificationScreen(navController = homeNavController)
        }

        composable<Support>{
            SupportScreen(navController = homeNavController,profileViewModel=profileViewModel)
        }


        composable<DeveloperTab> {
            DeveloperScreen(navController = homeNavController)
        }


        composable< ChatGroupTab>{
            ChatGroupScreen(navController = homeNavController)
        }


        composable<CreateTripTab> {
            CreateTripScreen(
                onNavigateBack = { homeNavController.popBackStack() }
            )
        }


        composable<AddMembersTab> {backStackEntry->
            val args=backStackEntry.toRoute<AddMembersTab>()
            AddMembersScreen(
                tripId = args.tripId,
                onNavigateBack = { homeNavController.popBackStack() },
                navController = homeNavController
            )
        }

        composable<MessageScreenTab> {backStackEntry->
            val args=backStackEntry.toRoute<MessageScreenTab>()
            MessageScreen(
                groupId = args.groupId,
                userId = requireNotNull(session.getUserId()),
                onClickBack = { homeNavController.popBackStack() },
                session = session
            )
        }

    }
}