package com.example.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ServiceCategory
import com.example.data.repository.ServiceRepository
import com.example.ui.components.CompactServiceCard
import com.example.ui.components.OnScreenScrollController
import com.example.ui.components.ServiceCard
import com.example.ui.components.StepByStepWizardDialog
import com.example.ui.components.getServiceIcon
import com.example.ui.components.webMouseWheelScroll
import com.example.viewmodel.DocBotViewModel
import kotlinx.coroutines.launch

enum class ServiceBatch(val title: String) {
  ALL("All 12 Services"),
  BATCH_ONE("1-6: ID & Travel"),
  BATCH_TWO("7-12: Certs & Schemes")
}

@Composable
fun HomeScreen(
  viewModel: DocBotViewModel,
  onNavigateToService: (String) -> Unit,
  onNavigateToSearch: () -> Unit,
  onNavigateToBot: () -> Unit,
  modifier: Modifier = Modifier
) {
  val allCategoryServices by viewModel.homeServices.collectAsState()
  val bookmarkedIds by viewModel.bookmarkedServiceIds.collectAsState()
  val recentServices by viewModel.recentServices.collectAsState()
  val selectedCategory by viewModel.homeCategory.collectAsState()
  val checkedKeys by viewModel.checkedDocumentKeys.collectAsState()
  val searchQuery by viewModel.homeSearchQuery.collectAsState()
  val isGridView by viewModel.isGridView.collectAsState()
  val isWizardOpen by viewModel.isWizardOpen.collectAsState()

  var selectedBatch by remember { mutableStateOf(ServiceBatch.ALL) }
  val listState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()

  // Filter services based on category and optional batch (1-6 vs 7-12)
  val displayedServices = remember(allCategoryServices, selectedBatch, selectedCategory, searchQuery) {
    if (selectedCategory != ServiceCategory.ALL || searchQuery.isNotBlank()) {
      allCategoryServices
    } else {
      when (selectedBatch) {
        ServiceBatch.ALL -> allCategoryServices
        ServiceBatch.BATCH_ONE -> allCategoryServices.take(6)
        ServiceBatch.BATCH_TWO -> allCategoryServices.drop(6)
      }
    }
  }

  // Step-by-Step Wizard Dialog
  if (isWizardOpen) {
    StepByStepWizardDialog(
      viewModel = viewModel,
      onNavigateToService = { serviceId ->
        viewModel.onServiceOpened(serviceId)
        onNavigateToService(serviceId)
      },
      onDismiss = { viewModel.closeWizard() }
    )
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .testTag("home_screen_container")
  ) {
    LazyColumn(
      state = listState,
      modifier = Modifier
        .fillMaxSize()
        .webMouseWheelScroll(listState)
        .testTag("home_screen"),
      contentPadding = PaddingValues(bottom = 80.dp), // Extra space for floating on-screen scroll buttons
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Executive Royal Cobalt Gradient Hero Header
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              brush = Brush.verticalGradient(
                colors = listOf(
                  Color(0xFF0F172A), // Slate 900
                  Color(0xFF1E3A8A), // Royal Blue 900
                  Color(0xFF2563EB)  // Vibrant Cobalt 600
                )
              ),
              shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(horizontal = 18.dp, vertical = 18.dp)
        ) {
          Column(modifier = Modifier.fillMaxWidth()) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "Hello! 👋",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Medium,
                  color = Color.White.copy(alpha = 0.9f)
                )
                Text(
                  text = "Document Requirement Bot",
                  style = MaterialTheme.typography.headlineSmall,
                  fontWeight = FontWeight.ExtraBold,
                  color = Color.White
                )
              }

              Box(
                modifier = Modifier
                  .size(42.dp)
                  .background(Color.White.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Shield,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(22.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "Know required proofs, fees & eligibility before applying.",
              style = MaterialTheme.typography.bodySmall,
              color = Color.White.copy(alpha = 0.85f),
              fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Real-time Active Search Bar in Header
            OutlinedTextField(
              value = searchQuery,
              onValueChange = { viewModel.onHomeSearchQueryChange(it) },
              placeholder = {
                Text(
                  text = "Search all 12 services, proofs, forms...",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              },
              leadingIcon = {
                Icon(
                  imageVector = Icons.Default.Search,
                  contentDescription = "Search",
                  tint = MaterialTheme.colorScheme.primary
                )
              },
              trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                  IconButton(
                    onClick = { viewModel.onHomeSearchQueryChange("") },
                    modifier = Modifier.testTag("home_search_clear")
                  ) {
                    Icon(
                      imageVector = Icons.Default.Clear,
                      contentDescription = "Clear search",
                      tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                }
              },
              singleLine = true,
              shape = RoundedCornerShape(12.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Transparent
              ),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("home_search_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Step-by-Step Eligibility Wizard Banner Button
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable { viewModel.openWizard() }
                .testTag("open_step_by_step_wizard_banner"),
              color = Color.White.copy(alpha = 0.16f),
              shape = RoundedCornerShape(12.dp)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 14.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.weight(1f)
                ) {
                  Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFFFDE047), // Bright Gold Star
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(
                      text = "Step-by-Step Document Checker",
                      style = MaterialTheme.typography.labelLarge,
                      fontWeight = FontWeight.Bold,
                      color = Color.White
                    )
                    Text(
                      text = "Check what you can apply for with your current documents",
                      style = MaterialTheme.typography.bodySmall,
                      color = Color.White.copy(alpha = 0.9f),
                      fontSize = 11.sp
                    )
                  }
                }

                Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }
        }
      }

      // Recently Viewed Horizontal Strip
      if (recentServices.isNotEmpty()) {
        item {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(bottom = 6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Recently Viewed",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            }

            Row(
              modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              recentServices.forEach { service ->
                Card(
                  modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                      viewModel.onServiceOpened(service.id)
                      onNavigateToService(service.id)
                    }
                    .testTag("recent_service_chip_${service.id}"),
                  colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                  ),
                  border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                  shape = RoundedCornerShape(10.dp)
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      imageVector = getServiceIcon(service.id),
                      contentDescription = null,
                      tint = MaterialTheme.colorScheme.primary,
                      modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = service.name,
                      style = MaterialTheme.typography.labelMedium,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                  }
                }
              }
            }
          }
        }
      }

      // Quick Batch Switcher (Allows instant jump to 1-6 or 7-12 without needing to scroll!)
      if (selectedCategory == ServiceCategory.ALL && searchQuery.isBlank()) {
        item {
          Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Text(
              text = "Quick Page Switcher (Instant Jump)",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              ServiceBatch.values().forEach { batch ->
                val isSelected = selectedBatch == batch
                FilterChip(
                  selected = isSelected,
                  onClick = {
                    selectedBatch = batch
                    coroutineScope.launch {
                      listState.animateScrollToItem(3)
                    }
                  },
                  label = {
                    Text(
                      text = batch.title,
                      fontSize = 11.sp,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                  },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                  ),
                  modifier = Modifier.weight(1f)
                )
              }
            }
          }
        }
      }

      // Category Filter Chips with real counts
      item {
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState())
              .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            ServiceCategory.values().forEach { category ->
              val isSelected = selectedCategory == category
              val categoryCount = if (category == ServiceCategory.ALL) {
                ServiceRepository.services.size
              } else {
                ServiceRepository.services.count { it.category == category }
              }

              FilterChip(
                selected = isSelected,
                onClick = {
                  viewModel.onHomeCategorySelect(category)
                  selectedBatch = ServiceBatch.ALL
                  coroutineScope.launch {
                    listState.animateScrollToItem(3)
                  }
                },
                label = {
                  Text("${category.displayName} ($categoryCount)")
                },
                leadingIcon = if (isSelected) {
                  {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = null,
                      modifier = Modifier.size(14.dp)
                    )
                  }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                  selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.testTag("category_chip_${category.name.lowercase()}")
              )
            }
          }
        }
      }

      // Title Row & View Mode Toggle (Grid vs Detailed List)
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = if (selectedCategory == ServiceCategory.ALL) {
                when (selectedBatch) {
                  ServiceBatch.ALL -> "All 12 Public Services"
                  ServiceBatch.BATCH_ONE -> "Services 1 to 6 (ID & Travel)"
                  ServiceBatch.BATCH_TWO -> "Services 7 to 12 (Certificates & Schemes)"
                }
              } else {
                "${selectedCategory.displayName} Services"
              },
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Showing ${displayedServices.size} Services • Use ↓ buttons or mouse wheel to scroll",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.primary
            )
          }

          // View Mode Switch Button (Grid / List)
          IconButton(
            onClick = { viewModel.toggleViewMode() },
            modifier = Modifier.testTag("toggle_view_mode_button")
          ) {
            Icon(
              imageVector = if (isGridView) Icons.Default.ViewAgenda else Icons.Default.GridView,
              contentDescription = if (isGridView) "Switch to List View" else "Switch to Compact Grid",
              tint = MaterialTheme.colorScheme.primary
            )
          }
        }
      }

      // Empty Search State
      if (displayedServices.isEmpty()) {
        item {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(16.dp)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "No services found",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Try clearing your search query or selecting 'All' category.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.height(12.dp))
              Button(
                onClick = {
                  viewModel.onHomeSearchQueryChange("")
                  viewModel.onHomeCategorySelect(ServiceCategory.ALL)
                  selectedBatch = ServiceBatch.ALL
                },
                shape = RoundedCornerShape(8.dp)
              ) {
                Text("Show All 12 Services")
              }
            }
          }
        }
      }

      // Service Items: Either 2-Column Compact Grid OR Full Detailed Cards
      if (isGridView) {
        val chunkedServices = displayedServices.chunked(2)
        items(chunkedServices) { pair ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            val firstService = pair[0]
            val (ready1, total1) = viewModel.getCheckedCountForService(firstService, checkedKeys)
            Box(modifier = Modifier.weight(1f)) {
              CompactServiceCard(
                service = firstService,
                isBookmarked = bookmarkedIds.contains(firstService.id),
                readyCount = ready1,
                totalCount = total1,
                onClick = {
                  viewModel.onServiceOpened(firstService.id)
                  onNavigateToService(firstService.id)
                },
                onBookmarkClick = {
                  viewModel.toggleBookmark(firstService.id)
                }
              )
            }

            if (pair.size > 1) {
              val secondService = pair[1]
              val (ready2, total2) = viewModel.getCheckedCountForService(secondService, checkedKeys)
              Box(modifier = Modifier.weight(1f)) {
                CompactServiceCard(
                  service = secondService,
                  isBookmarked = bookmarkedIds.contains(secondService.id),
                  readyCount = ready2,
                  totalCount = total2,
                  onClick = {
                    viewModel.onServiceOpened(secondService.id)
                    onNavigateToService(secondService.id)
                  },
                  onBookmarkClick = {
                    viewModel.toggleBookmark(secondService.id)
                  }
                )
              }
            } else {
              Spacer(modifier = Modifier.weight(1f))
            }
          }
        }
      } else {
        items(displayedServices, key = { it.id }) { service ->
          val (readyCount, totalCount) = viewModel.getCheckedCountForService(service, checkedKeys)
          Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            ServiceCard(
              service = service,
              isBookmarked = bookmarkedIds.contains(service.id),
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
      }

      // Step-by-Step "How It Works" Section
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("how_it_works_card"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
          ),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Lightbulb,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.secondary,
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "Step-by-Step Procedure",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "How to use this app for hassle-free applications",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            val steps = listOf(
              "Step 1: Choose Service" to "Browse or search from government/public categories.",
              "Step 2: Review Documents" to "Check identity, address proofs, validity rules & sample formats.",
              "Step 3: Track Readiness" to "Tick off documents as you gather them for 100% readiness.",
              "Step 4: Apply on Portal" to "Submit error-free on the verified official government portal."
            )

            steps.forEach { (title, subtitle) ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp),
                verticalAlignment = Alignment.Top
              ) {
                Text(
                  text = title,
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.SemiBold,
                  color = MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier.width(160.dp)
                )
                Text(
                  text = subtitle,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }

      // "Need Help? Ask Document Requirement Bot" Card
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("need_help_bot_card"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
          ),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.Chat,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(24.dp)
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Have Questions?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
              Text(
                text = "Ask Document Requirement Bot about required proofs and steps.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                fontSize = 12.sp
              )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
              onClick = onNavigateToBot,
              colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
              ),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.testTag("home_ask_bot_button")
            ) {
              Text(text = "Ask the Bot →")
            }
          }
        }
      }
    }
  }

  // Floating On-Screen Scroll Controller (↓ Down and ↑ Up buttons)
  OnScreenScrollController(
    listState = listState,
    modifier = Modifier.align(Alignment.BottomEnd)
  )
}
}
