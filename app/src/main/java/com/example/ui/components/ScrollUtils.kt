package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

/**
 * Modifier that listens for mouse wheel scroll events (common in Web Streaming Emulators)
 * and dispatches the delta to the LazyListState so users can scroll using their mouse wheel.
 */
fun Modifier.webMouseWheelScroll(listState: LazyListState): Modifier {
  return this.pointerInput(Unit) {
    awaitPointerEventScope {
      while (true) {
        val event = awaitPointerEvent()
        if (event.type == PointerEventType.Scroll) {
          val change = event.changes.firstOrNull()
          val scrollDeltaY = change?.scrollDelta?.y ?: 0f
          if (scrollDeltaY != 0f) {
            // Scroll delta is usually 1f or -1f per notch; multiply by 120dp equivalent pixels
            listState.dispatchRawDelta(scrollDeltaY * 120f)
            change?.consume()
          }
        }
      }
    }
  }
}

/**
 * Visible Floating On-Screen Scroll Controller.
 * Provides explicit "↓ Down" and "↑ Up" buttons so users who cannot scroll
 * via mouse wheel or touchpad can easily tap or click on-screen buttons to scroll!
 */
@Composable
fun OnScreenScrollController(
  listState: LazyListState,
  modifier: Modifier = Modifier,
  canScrollDown: Boolean = true
) {
  val coroutineScope = rememberCoroutineScope()

  Box(
    modifier = modifier.padding(16.dp),
    contentAlignment = Alignment.BottomEnd
  ) {
    Surface(
      shape = RoundedCornerShape(24.dp),
      color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.95f),
      border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
      tonalElevation = 6.dp,
      shadowElevation = 8.dp
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        // Scroll Up Button
        SmallFloatingActionButton(
          onClick = {
            coroutineScope.launch {
              val currentFirstIndex = listState.firstVisibleItemIndex
              val targetIndex = (currentFirstIndex - 2).coerceAtLeast(0)
              listState.animateScrollToItem(targetIndex)
            }
          },
          containerColor = MaterialTheme.colorScheme.surface,
          contentColor = MaterialTheme.colorScheme.primary,
          elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 2.dp),
          modifier = Modifier
            .size(36.dp)
            .testTag("on_screen_scroll_up")
        ) {
          Icon(
            imageVector = Icons.Default.KeyboardArrowUp,
            contentDescription = "Scroll Up",
            modifier = Modifier.size(20.dp)
          )
        }

        // Label
        Text(
          text = "Scroll",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onPrimaryContainer,
          fontSize = 11.sp,
          modifier = Modifier.padding(horizontal = 4.dp)
        )

        // Scroll Down Button
        SmallFloatingActionButton(
          onClick = {
            coroutineScope.launch {
              val currentFirstIndex = listState.firstVisibleItemIndex
              val totalItems = listState.layoutInfo.totalItemsCount
              val targetIndex = (currentFirstIndex + 2).coerceAtMost((totalItems - 1).coerceAtLeast(0))
              listState.animateScrollToItem(targetIndex)
            }
          },
          containerColor = MaterialTheme.colorScheme.primary,
          contentColor = MaterialTheme.colorScheme.onPrimary,
          elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 2.dp),
          modifier = Modifier
            .size(36.dp)
            .testTag("on_screen_scroll_down")
        ) {
          Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Scroll Down",
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}
