package com.example.ui.screens.details

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.rememberCoroutineScope
import com.example.ui.components.OnScreenScrollController
import com.example.ui.components.webMouseWheelScroll
import kotlinx.coroutines.launch
import com.example.data.model.DocumentItem
import com.example.data.model.ServiceItem
import com.example.data.model.StatePortalInfo
import com.example.data.repository.ServiceRepository
import com.example.ui.components.DocumentDetailDialog
import com.example.ui.components.getCategoryTheme
import com.example.ui.components.getServiceIcon
import com.example.ui.theme.StatusSuccessBg
import com.example.ui.theme.StatusSuccessColor
import com.example.ui.theme.StatusWarningBg
import com.example.ui.theme.StatusWarningColor
import com.example.viewmodel.DocBotViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailScreen(
  serviceId: String,
  viewModel: DocBotViewModel,
  onNavigateBack: () -> Unit,
  onNavigateToBot: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val service = remember(serviceId) { ServiceRepository.getServiceById(serviceId) }
  val bookmarkedIds by viewModel.bookmarkedServiceIds.collectAsState()
  val isBookmarked = bookmarkedIds.contains(serviceId)
  val checkedKeys by viewModel.checkedDocumentKeys.collectAsState()
  val activeModalDoc by viewModel.activeDocumentModal.collectAsState()

  var selectedStatePortal by remember { mutableStateOf<StatePortalInfo?>(service?.statePortals?.firstOrNull()) }

  if (service == null) {
    Scaffold(
      topBar = {
        TopAppBar(
          title = { Text("Service Not Found") },
          navigationIcon = {
            IconButton(onClick = onNavigateBack) {
              Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
          }
        )
      }
    ) { padding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(padding),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text("Service could not be found.")
          Spacer(modifier = Modifier.height(16.dp))
          Button(onClick = onNavigateBack) {
            Text("Back to Home")
          }
        }
      }
    }
    return
  }

  val (readyCount, totalCount) = viewModel.getCheckedCountForService(service, checkedKeys)
  val progress = viewModel.getServiceProgress(service, checkedKeys)
  val isAllReady = totalCount > 0 && readyCount == totalCount
  val (categoryColor, categoryBg) = getCategoryTheme(service.category)

  // Active Document Modal Dialog
  activeModalDoc?.let { doc ->
    DocumentDetailDialog(
      document = doc,
      isChecked = viewModel.isDocumentChecked(service.id, doc.id, checkedKeys),
      onToggleChecked = { viewModel.toggleDocumentChecked(service.id, doc.id) },
      onDismiss = { viewModel.closeDocumentDialog() }
    )
  }

  // Share checklist text formatter
  fun shareChecklist() {
    val shareText = buildString {
      append("📋 Document Checklist for ${service.name}\n")
      append("Department: ${service.department}\n")
      append("Official Fee: ${service.estimatedFee}\n")
      append("Processing Time: ${service.processingTime}\n\n")
      append("Required Documents:\n")
      service.requiredDocuments.forEachIndexed { idx, doc ->
        val isChecked = viewModel.isDocumentChecked(service.id, doc.id, checkedKeys)
        val mark = if (isChecked) "☑ [Ready]" else "☐ [Pending]"
        append("${idx + 1}. $mark ${doc.name}\n   Purpose: ${doc.purpose}\n")
      }
      append("\nOfficial Portal: ${service.officialPortalUrl}\n")
      append("\nGenerated via Document Requirement Bot")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(Intent.EXTRA_SUBJECT, "${service.name} Document Checklist")
      putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(Intent.createChooser(intent, "Share Checklist via"))
  }

  Scaffold(
    modifier = modifier.testTag("service_detail_screen"),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = service.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        },
        navigationIcon = {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.testTag("detail_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back"
            )
          }
        },
        actions = {
          IconButton(
            onClick = { shareChecklist() },
            modifier = Modifier.testTag("detail_share_button")
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "Share Checklist",
              tint = MaterialTheme.colorScheme.onSurface
            )
          }

          IconButton(
            onClick = { viewModel.toggleBookmark(service.id) },
            modifier = Modifier.testTag("detail_bookmark_toggle")
          ) {
            Icon(
              imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
              contentDescription = if (isBookmarked) "Remove Bookmark" else "Save Service",
              tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    },
    bottomBar = {
      Surface(
        tonalElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Button(
            onClick = {
              viewModel.askAboutService(service)
              onNavigateToBot()
            },
            modifier = Modifier
              .weight(1f)
              .testTag("detail_ask_bot_button"),
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ask Bot About This")
          }

          OutlinedButton(
            onClick = {
              try {
                val url = selectedStatePortal?.portalUrl ?: service.officialPortalUrl
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
              } catch (e: Exception) {
                // Browser intent fallback
              }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.testTag("detail_official_portal_button")
          ) {
            Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Portal")
          }
        }
      }
    }
  ) { innerPadding ->
    val detailListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      LazyColumn(
        state = detailListState,
        modifier = Modifier
          .fillMaxSize()
          .webMouseWheelScroll(detailListState),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // Section Quick Jump Chips
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            FilterChip(
              selected = false,
              onClick = { coroutineScope.launch { detailListState.animateScrollToItem(1) } },
              label = { Text("Overview") }
            )
            FilterChip(
              selected = false,
              onClick = { coroutineScope.launch { detailListState.animateScrollToItem(4) } },
              label = { Text("📋 Documents (${service.requiredDocuments.size})") }
            )
            FilterChip(
              selected = false,
              onClick = { coroutineScope.launch { detailListState.animateScrollToItem(6) } },
              label = { Text("📝 How to Apply") }
            )
            FilterChip(
              selected = false,
              onClick = { coroutineScope.launch { detailListState.animateScrollToItem(5) } },
              label = { Text("✓ Eligibility") }
            )
            FilterChip(
              selected = false,
              onClick = { coroutineScope.launch { detailListState.animateScrollToItem(7) } },
              label = { Text("⚠️ Important Notes") }
            )
            FilterChip(
              selected = false,
              onClick = { coroutineScope.launch { detailListState.animateScrollToItem(8) } },
              label = { Text("❓ FAQs") }
            )
          }
        }
      // 1. Hero Overview Card with Category Branding
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
          ),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(52.dp)
                  .background(categoryBg, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = getServiceIcon(service.id),
                  contentDescription = null,
                  tint = categoryColor,
                  modifier = Modifier.size(28.dp)
                )
              }
              Spacer(modifier = Modifier.width(14.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = service.name,
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = service.department,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  fontSize = 12.sp
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = service.description,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Badges Grid: Estimated Fee, Processing Time, Mode
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                  .fillMaxWidth()
                  .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                  .padding(horizontal = 10.dp, vertical = 6.dp)
              ) {
                Icon(Icons.Default.Payments, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Fee: ", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                Text(text = service.estimatedFee, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }

              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                  .fillMaxWidth()
                  .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                  .padding(horizontal = 10.dp, vertical = 6.dp)
              ) {
                Icon(Icons.Default.AccessTime, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Timeline: ", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                Text(text = service.processingTime, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
          }
        }
      }

      // 2. Interactive Document Readiness Tracker Card
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (isAllReady) StatusSuccessBg else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
          ),
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isAllReady) StatusSuccessColor.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = if (isAllReady) Icons.Default.CheckCircle else Icons.Outlined.CheckCircleOutline,
                  contentDescription = null,
                  tint = if (isAllReady) StatusSuccessColor else MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Application Readiness",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = if (isAllReady) StatusSuccessColor else MaterialTheme.colorScheme.onSurface
                )
              }

              if (readyCount > 0) {
                IconButton(
                  onClick = { viewModel.resetChecklist(service.id) },
                  modifier = Modifier.size(28.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset checklist",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
              progress = { progress },
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
              color = if (isAllReady) StatusSuccessColor else MaterialTheme.colorScheme.primary,
              trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "$readyCount of $totalCount Required Documents Ready",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = if (isAllReady) StatusSuccessColor else MaterialTheme.colorScheme.primary
              )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = when {
                isAllReady -> "🎉 All documents collected! You meet the primary checklist to submit your application."
                readyCount > 0 -> "In progress. Tap the checkboxes below as you gather each document."
                else -> "Tap the checkboxes beside each document as you gather your paperwork."
              },
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      // 3. State Portals Selector (if service has state variations)
      if (service.statePortals.isNotEmpty()) {
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Language, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "State & Regional Portals",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }

              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "Requirements and portals differ by state. Select your state:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )

              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                service.statePortals.forEach { portal ->
                  val isSelected = selectedStatePortal?.stateName == portal.stateName
                  FilterChip(
                    selected = isSelected,
                    onClick = { selectedStatePortal = portal },
                    label = { Text(portal.stateName) },
                    colors = FilterChipDefaults.filterChipColors(
                      selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                      selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                  )
                }
              }

              selectedStatePortal?.let { portal ->
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                  shape = RoundedCornerShape(10.dp),
                  color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column(modifier = Modifier.weight(1f)) {
                      Text(
                        text = "${portal.stateName} Portal: ${portal.portalName}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                      )
                      if (portal.stateNote.isNotBlank()) {
                        Text(
                          text = portal.stateNote,
                          style = MaterialTheme.typography.labelSmall,
                          color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                      }
                    }
                    IconButton(
                      onClick = {
                        try {
                          context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(portal.portalUrl)))
                        } catch (e: Exception) {}
                      },
                      modifier = Modifier.size(32.dp)
                    ) {
                      Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = "Open State Portal", modifier = Modifier.size(16.dp))
                    }
                  }
                }
              }
            }
          }
        }
      }

      // 4. Required Documents List with Checkboxes
      item {
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Required Documents",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Check as collected",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.primary
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            service.requiredDocuments.forEach { doc ->
              val isChecked = viewModel.isDocumentChecked(service.id, doc.id, checkedKeys)
              InteractiveDocumentCard(
                document = doc,
                isChecked = isChecked,
                onCheckboxToggle = { viewModel.toggleDocumentChecked(service.id, doc.id) },
                onClick = { viewModel.openDocumentDialog(doc) }
              )
            }

            if (service.optionalDocuments.isNotEmpty()) {
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "Optional / Conditional Documents",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.height(4.dp))
              service.optionalDocuments.forEach { doc ->
                val isChecked = viewModel.isDocumentChecked(service.id, doc.id, checkedKeys)
                InteractiveDocumentCard(
                  document = doc,
                  isChecked = isChecked,
                  onCheckboxToggle = { viewModel.toggleDocumentChecked(service.id, doc.id) },
                  onClick = { viewModel.openDocumentDialog(doc) }
                )
              }
            }
          }
        }
      }

      // 5. Avoid Common Rejections Card
      if (service.commonRejections.isNotEmpty()) {
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surface
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, StatusWarningColor.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(18.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Cancel,
                  contentDescription = null,
                  tint = StatusWarningColor,
                  modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = "Avoid Common Rejection Reasons",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              service.commonRejections.forEach { rejection ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                  verticalAlignment = Alignment.Top
                ) {
                  Text(
                    text = "✗",
                    color = StatusWarningColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 8.dp)
                  )
                  Text(
                    text = rejection,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                }
              }
            }
          }
        }
      }

      // 6. Eligibility Criteria Section
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
          ),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(22.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = "Eligibility Criteria",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            service.eligibilityCriteria.forEach { crit ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp),
                verticalAlignment = Alignment.Top
              ) {
                Text(
                  text = "•",
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                  text = crit,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.Top
              ) {
                Icon(
                  imageVector = Icons.Default.Info,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier
                    .size(16.dp)
                    .padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Requirements may vary by service, applicant category, and current government rules. Verify the latest requirements with the official service portal before applying.",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }

      // 7. Step-by-Step Application Process
      item {
        Column(modifier = Modifier.fillMaxWidth()) {
          Text(
            text = "How to Apply",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(12.dp))

          service.applicationSteps.forEach { step ->
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp),
                verticalAlignment = Alignment.Top
              ) {
                Box(
                  modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = "${step.stepNumber}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                  )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = step.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = step.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }
          }
        }
      }

      // 8. Important Notes & Precautions
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.35f)
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(22.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = "Important Notes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            service.importantNotes.forEach { note ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp),
                verticalAlignment = Alignment.Top
              ) {
                Text(
                  text = "⚠",
                  color = MaterialTheme.colorScheme.tertiary,
                  modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                  text = note,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }
      }

      // 9. Frequently Asked Questions
      if (service.faqs.isNotEmpty()) {
        item {
          Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Frequently Asked Questions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            service.faqs.forEach { faq ->
              FaqItemView(faq = faq)
            }
          }
        }
      }
    }

    OnScreenScrollController(
      listState = detailListState,
      modifier = Modifier.align(Alignment.BottomEnd)
    )
  }
}
}

@Composable
fun InteractiveDocumentCard(
  document: DocumentItem,
  isChecked: Boolean,
  onCheckboxToggle: () -> Unit,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .clickable(onClick = onClick)
      .border(
        width = 1.dp,
        color = if (isChecked) StatusSuccessColor.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
        shape = RoundedCornerShape(14.dp)
      )
      .testTag("document_card_${document.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isChecked) StatusSuccessBg.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Direct Interactive Checkbox
      Checkbox(
        checked = isChecked,
        onCheckedChange = { onCheckboxToggle() },
        colors = CheckboxDefaults.colors(
          checkedColor = StatusSuccessColor,
          checkmarkColor = androidx.compose.ui.graphics.Color.White
        ),
        modifier = Modifier.size(24.dp)
      )

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = document.name,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = if (isChecked) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurface,
            textDecoration = if (isChecked) TextDecoration.None else TextDecoration.None
          )
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = if (isChecked) StatusSuccessBg else if (document.required) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant
          ) {
            Text(
              text = if (isChecked) "Ready" else if (document.required) "Required" else "Optional",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = if (isChecked) StatusSuccessColor else if (document.required) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = document.purpose,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      IconButton(
        onClick = onClick,
        modifier = Modifier.size(32.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Info,
          contentDescription = "View detail",
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}

@Composable
fun FaqItemView(faq: com.example.data.model.FaqItem) {
  var expanded by remember { mutableStateOf(false) }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp)
      .clip(RoundedCornerShape(12.dp))
      .clickable { expanded = !expanded }
      .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f), RoundedCornerShape(12.dp)),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = faq.question,
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.weight(1f)
        )
        Icon(
          imageVector = Icons.Default.ExpandMore,
          contentDescription = if (expanded) "Collapse" else "Expand",
          tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      if (expanded) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = faq.answer,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 20.sp
        )
      }
    }
  }
}
