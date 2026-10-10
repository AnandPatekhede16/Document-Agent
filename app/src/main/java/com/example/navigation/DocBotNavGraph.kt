package com.example.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.ui.components.DocBottomNavBar
import com.example.ui.screens.about.AboutScreen
import com.example.ui.screens.bot.ChatbotScreen
import com.example.ui.screens.details.ServiceDetailScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.saved.SavedScreen
import com.example.ui.screens.search.SearchScreen
import com.example.ui.screens.splash.SplashScreen
import com.example.viewmodel.DocBotViewModel

@Composable
fun DocBotNavGraph(
  navController: NavHostController,
  viewModel: DocBotViewModel,
  modifier: Modifier = Modifier
) {
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  // Hide bottom bar on Splash and ServiceDetail screens
  val shouldShowBottomBar = currentRoute in listOf(
    Screen.Home.route,
    Screen.Search.route,
    Screen.Bot.route,
    Screen.Saved.route,
    Screen.About.route
  )

  Scaffold(
    modifier = modifier.fillMaxSize(),
    bottomBar = {
      if (shouldShowBottomBar) {
        DocBottomNavBar(
          currentRoute = currentRoute,
          onNavigateToRoute = { route ->
            navController.navigate(route) {
              popUpTo(Screen.Home.route) {
                saveState = true
              }
              launchSingleTop = true
              restoreState = true
            }
          }
        )
      }
    }
  ) { innerPadding ->
    NavHost(
      navController = navController,
      startDestination = Screen.Splash.route,
      modifier = Modifier.padding(innerPadding)
    ) {
      // 1. Splash Screen
      composable(Screen.Splash.route) {
        SplashScreen(
          onNavigateToHome = {
            navController.navigate(Screen.Home.route) {
              popUpTo(Screen.Splash.route) { inclusive = true }
            }
          }
        )
      }

      // 2. Home Screen
      composable(Screen.Home.route) {
        HomeScreen(
          viewModel = viewModel,
          onNavigateToService = { serviceId ->
            navController.navigate(Screen.ServiceDetail.createRoute(serviceId))
          },
          onNavigateToSearch = {
            navController.navigate(Screen.Search.route)
          },
          onNavigateToBot = {
            navController.navigate(Screen.Bot.route)
          }
        )
      }

      // 3. Search Screen
      composable(Screen.Search.route) {
        SearchScreen(
          viewModel = viewModel,
          onNavigateToService = { serviceId ->
            navController.navigate(Screen.ServiceDetail.createRoute(serviceId))
          }
        )
      }

      // 4. Chatbot Screen
      composable(Screen.Bot.route) {
        ChatbotScreen(
          viewModel = viewModel,
          onNavigateToService = { serviceId ->
            navController.navigate(Screen.ServiceDetail.createRoute(serviceId))
          }
        )
      }

      // 5. Saved Services Screen
      composable(Screen.Saved.route) {
        SavedScreen(
          viewModel = viewModel,
          onNavigateToService = { serviceId ->
            navController.navigate(Screen.ServiceDetail.createRoute(serviceId))
          },
          onNavigateToSearch = {
            navController.navigate(Screen.Search.route)
          }
        )
      }

      // 6. About Screen
      composable(Screen.About.route) {
        AboutScreen()
      }

      // 7. Service Detail Screen
      composable(
        route = Screen.ServiceDetail.route,
        arguments = listOf(
          navArgument("serviceId") { type = NavType.StringType }
        )
      ) { backStackEntry ->
        val serviceId = backStackEntry.arguments?.getString("serviceId") ?: ""
        ServiceDetailScreen(
          serviceId = serviceId,
          viewModel = viewModel,
          onNavigateBack = {
            navController.popBackStack()
          },
          onNavigateToBot = {
            navController.navigate(Screen.Bot.route)
          }
        )
      }
    }
  }
}
