package com.travoro.app.ui.home.homeNavigation

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
import com.travoro.app.data.remote.dto.trips.TripDto
import com.travoro.app.di.Session
import com.travoro.app.ui.home.dashboard.HomeTabScreen
import com.travoro.app.ui.home.dashboard.HomeViewModel
import com.travoro.app.ui.home.features.addMembers.AddMembersScreen
import com.travoro.app.ui.home.features.aiSearch.AiSearchScreen
import com.travoro.app.ui.home.features.billSplit.BillSplitScreen
import com.travoro.app.ui.home.features.billSplit.SplitExpenseScreen
import com.travoro.app.ui.home.features.createTrips.CreateTripScreen
import com.travoro.app.ui.home.features.findMember.FindMemberScreen
import com.travoro.app.ui.home.features.sos.SosScreen
import com.travoro.app.ui.home.message.ChatGroupScreen
import com.travoro.app.ui.home.message.MessageScreen
import com.travoro.app.ui.home.mytrips.MyTripDetailScreen
import com.travoro.app.ui.home.mytrips.MyTripsScreen
import com.travoro.app.ui.home.navigationDrawer.myaccount.MyAccountScreen
import com.travoro.app.ui.home.navigationDrawer.notification.NotificationScreen
import com.travoro.app.ui.home.navigationDrawer.options.DeveloperScreen
import com.travoro.app.ui.home.navigationDrawer.support.SupportScreen
import com.travoro.app.ui.home.profile.ProfileTabScreen
import com.travoro.app.ui.home.profile.ProfileViewModel
import com.travoro.app.ui.home.travelAI.TravelAITabScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun HomeNavGraph(
    homeNavController: NavHostController,
    rootNavController: NavController,
    profileViewModel: ProfileViewModel,
    homeViewModel: HomeViewModel,
    paddingValues: PaddingValues,
    session: Session,
) {
    NavHost(
        navController = homeNavController,
        startDestination = HomeTab,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400),
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(400))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400),
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(400))
        },
    ) {
        composable<HomeTab> {
            HomeTabScreen(
                paddingValues,
                homeNavController = homeNavController,
                homeViewModel = homeViewModel,
                profileViewModel = profileViewModel,
                session = session,
            )
        }
        composable<TravelAITab> {
            TravelAITabScreen(paddingValues = paddingValues, navController = homeNavController)
        }

        composable<MyTripsTab> {
            MyTripsScreen(
                paddingValues = paddingValues,
                onTripClick = { trip ->
                    homeNavController.navigate(MyTripDetail(trip = Json.encodeToString(trip)))
                },
            )
        }

        composable<MyTripDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<MyTripDetail>()
            val trip = Json.decodeFromString<TripDto>(args.trip)
            MyTripDetailScreen(
                homeNavController = homeNavController,
                trip = trip,
                onNavigateBack = { homeNavController.popBackStack() },
                session = session,
            )
        }

        composable<MyProfileTab> {
            ProfileTabScreen(
                profileViewModel = profileViewModel,
                onNavigateBack = { homeNavController.popBackStack() },
            )
        }
        composable<MyAccount> {
            MyAccountScreen(
                navController = homeNavController,
                rootNavController = rootNavController,
                session = session,
            )
        }

        composable<Notification> {
            NotificationScreen(navController = homeNavController)
        }

        composable<Support> {
            SupportScreen(navController = homeNavController, profileViewModel = profileViewModel)
        }

        composable<DeveloperTab> {
            DeveloperScreen(navController = homeNavController)
        }

        composable<ChatGroupTab> {
            ChatGroupScreen(navController = homeNavController)
        }

        composable<CreateTripTab> {
            CreateTripScreen(
                homeNavController = homeNavController,
            )
        }

        composable<Sos> {
            SosScreen(
                onNavigateBack = { homeNavController.popBackStack() },
            )
        }

        composable<AiSearch> {
            AiSearchScreen(
                homeNavController = homeNavController,
            )
        }

        composable<FindMember> {
            FindMemberScreen(
                homeNavController = homeNavController,
            )
        }

        composable<AddMembersTab> { backStackEntry ->
            val args = backStackEntry.toRoute<AddMembersTab>()
            AddMembersScreen(
                tripId = args.tripId,
                onNavigateBack = { homeNavController.popBackStack() },
                navController = homeNavController,
                days = args.days,
            )
        }

        composable<MessageScreenTab> { backStackEntry ->
            val args = backStackEntry.toRoute<MessageScreenTab>()
            MessageScreen(
                groupId = args.groupId,
                userId = requireNotNull(session.getUserId()),
                onClickBack = { homeNavController.popBackStack() },
                session = session,
            )
        }

        composable<BillSplit> {
            BillSplitScreen(
                onTripClick = { trip ->
                    homeNavController.navigate(SplitExpense(trip = Json.encodeToString(trip)))
                },
                homeNavController = homeNavController,
            )
        }

        composable<SplitExpense> { backStackEntry ->
            val args = backStackEntry.toRoute<SplitExpense>()
            val trip = Json.decodeFromString<TripDto>(args.trip)
            SplitExpenseScreen(
                trip = trip,
                onNavigateBack = { homeNavController.popBackStack() },
            )
        }
    }
}
