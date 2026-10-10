package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.navigation.BottomNavItem

@Composable
fun DocBottomNavBar(
  currentRoute: String?,
  onNavigateToRoute: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    modifier = modifier.testTag("bottom_nav_bar"),
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 8.dp
  ) {
    BottomNavItem.values().forEach { item ->
      val isSelected = currentRoute == item.route

      val icon = when (item) {
        BottomNavItem.HOME -> if (isSelected) Icons.Filled.Home else Icons.Outlined.Home
        BottomNavItem.SEARCH -> if (isSelected) Icons.Filled.Search else Icons.Outlined.Search
        BottomNavItem.BOT -> if (isSelected) Icons.AutoMirrored.Filled.Chat else Icons.Outlined.ChatBubbleOutline
        BottomNavItem.SAVED -> if (isSelected) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder
        BottomNavItem.ABOUT -> if (isSelected) Icons.Filled.Info else Icons.Outlined.Info
      }

      NavigationBarItem(
        modifier = Modifier.testTag("nav_item_${item.route}"),
        selected = isSelected,
        onClick = {
          if (!isSelected) {
            onNavigateToRoute(item.route)
          }
        },
        icon = {
          Icon(
            imageVector = icon,
            contentDescription = item.title
          )
        },
        label = {
          Text(
            text = item.title,
            style = MaterialTheme.typography.labelSmall
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = MaterialTheme.colorScheme.primary,
          selectedTextColor = MaterialTheme.colorScheme.primary,
          indicatorColor = MaterialTheme.colorScheme.primaryContainer,
          unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
          unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )
    }
  }
}
