package com.example.navigation

sealed class Screen(val route: String) {
  object Splash : Screen("splash")
  object Home : Screen("home")
  object Search : Screen("search")
  object Bot : Screen("bot")
  object Saved : Screen("saved")
  object About : Screen("about")
  object ServiceDetail : Screen("service/{serviceId}") {
    fun createRoute(serviceId: String): String = "service/$serviceId"
  }
}

enum class BottomNavItem(
  val route: String,
  val title: String
) {
  HOME("home", "Home"),
  SEARCH("search", "Search"),
  BOT("bot", "Bot"),
  SAVED("saved", "Saved"),
  ABOUT("about", "About")
}
