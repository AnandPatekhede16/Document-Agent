package com.example.ui.screens.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.rememberLazyListState
import com.example.ui.components.OnScreenScrollController
import com.example.ui.components.webMouseWheelScroll
import com.example.ui.components.ServiceCard
import com.example.viewmodel.DocBotViewModel

@Composable
fun SavedScreen(
  viewModel: DocBotViewModel,
  onNavigateToService: (String) -> Unit,
  onNavigateToSearch: () -> Unit,
  modifier: Modifier = Modifier
) {
  val bookmarkedServices by viewModel.bookmarkedServices.collectAsState()
  val bookmarkedIds by viewModel.bookmarkedServiceIds.collectAsState()
  val checkedKeys by viewModel.checkedDocumentKeys.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("saved_screen")
  ) {
    // Header
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surface)
        .padding(16.dp)
    ) {
      Text(
        text = "Saved Services",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Quick access to your bookmarked document checklists.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    if (bookmarkedServices.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(24.dp)
          .testTag("saved_empty_state"),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Box(
            modifier = Modifier
              .size(72.dp)
              .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Outlined.BookmarkBorder,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(36.dp)
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "No saved services yet",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Tap the bookmark icon on any service card or details page to save it for quick offline reference.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(20.dp))

          Button(
            onClick = onNavigateToSearch,
            modifier = Modifier.testTag("saved_browse_services_button")
          ) {
            Text("Browse Services")
          }
        }
      }
    } else {
      val savedListState = rememberLazyListState()
      Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
          state = savedListState,
          modifier = Modifier
            .fillMaxSize()
            .webMouseWheelScroll(savedListState),
          contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "My Saved Services (${bookmarkedServices.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }

          items(bookmarkedServices, key = { it.id }) { service ->
            val (readyCount, totalCount) = viewModel.getCheckedCountForService(service, checkedKeys)
            ServiceCard(
              service = service,
              isBookmarked = true,
              readyCount = readyCount,
              totalCount = totalCount,
              onClick = {
                viewModel.onServiceOpened(service.id)
                onNavigateToService(service.id)
              },
              onBookmarkClick = {
                viewModel.toggleBookmark(service.id)
              }
            )
          }
        }

        OnScreenScrollController(
          listState = savedListState,
          modifier = Modifier.align(Alignment.BottomEnd)
        )
      }
    }
  }
}
