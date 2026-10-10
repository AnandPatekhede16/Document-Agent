package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ServiceCategory
import com.example.data.model.ServiceItem
import com.example.ui.theme.CategoryBenefitsBg
import com.example.ui.theme.CategoryBenefitsColor
import com.example.ui.theme.CategoryCertificatesBg
import com.example.ui.theme.CategoryCertificatesColor
import com.example.ui.theme.CategoryEducationBg
import com.example.ui.theme.CategoryEducationColor
import com.example.ui.theme.CategoryIdentityBg
import com.example.ui.theme.CategoryIdentityColor
import com.example.ui.theme.CategoryTransportBg
import com.example.ui.theme.CategoryTransportColor
import com.example.ui.theme.CategoryTravelBg
import com.example.ui.theme.CategoryTravelColor
import com.example.ui.theme.StatusSuccessBg
import com.example.ui.theme.StatusSuccessColor

fun getServiceIcon(serviceId: String): ImageVector {
  return when (serviceId.lowercase()) {
    "aadhaar" -> Icons.Default.VerifiedUser
    "pan" -> Icons.Default.CreditCard
    "passport" -> Icons.Default.Flight
    "driving_licence" -> Icons.Default.DirectionsCar
    "voter_id" -> Icons.Default.HowToVote
    "income_certificate" -> Icons.Default.Payments
    "caste_certificate" -> Icons.Default.Badge
    "domicile_certificate" -> Icons.Default.HistoryEdu
    "birth_certificate" -> Icons.Default.FamilyRestroom
    "death_certificate" -> Icons.AutoMirrored.Filled.Assignment
    "ration_card" -> Icons.Default.CreditCard
    "scholarship" -> Icons.Default.School
    else -> Icons.AutoMirrored.Filled.Assignment
  }
}

fun getCategoryTheme(category: ServiceCategory): Pair<Color, Color> {
  return when (category) {
    ServiceCategory.IDENTITY -> Pair(CategoryIdentityColor, CategoryIdentityBg)
    ServiceCategory.CERTIFICATES -> Pair(CategoryCertificatesColor, CategoryCertificatesBg)
    ServiceCategory.TRAVEL -> Pair(CategoryTravelColor, CategoryTravelBg)
    ServiceCategory.TRANSPORT -> Pair(CategoryTransportColor, CategoryTransportBg)
    ServiceCategory.EDUCATION -> Pair(CategoryEducationColor, CategoryEducationBg)
    ServiceCategory.BENEFITS -> Pair(CategoryBenefitsColor, CategoryBenefitsBg)
    ServiceCategory.ALL -> Pair(CategoryIdentityColor, CategoryIdentityBg)
  }
}

@Composable
fun ServiceCard(
  service: ServiceItem,
  isBookmarked: Boolean,
  onClick: () -> Unit,
  onBookmarkClick: () -> Unit,
  modifier: Modifier = Modifier,
  readyCount: Int = 0,
  totalCount: Int = service.requiredDocuments.size
) {
  val (categoryColor, categoryBg) = getCategoryTheme(service.category)
  val isCompleted = totalCount > 0 && readyCount == totalCount

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("service_card_${service.id}")
      .clip(RoundedCornerShape(16.dp))
      .clickable(onClick = onClick)
      .border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
        shape = RoundedCornerShape(16.dp)
      ),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Leading Icon Avatar with Category Accent
        Box(
          modifier = Modifier
            .size(44.dp)
            .background(
              color = categoryBg,
              shape = RoundedCornerShape(12.dp)
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = getServiceIcon(service.id),
            contentDescription = service.name,
            tint = categoryColor,
            modifier = Modifier.size(24.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Center Title & Category Badge
        Column(
          modifier = Modifier.weight(1f)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text(
              text = service.name,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
              modifier = Modifier.weight(1f, fill = false)
            )

            // Category Badge
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = categoryBg
            ) {
              Text(
                text = service.category.displayName,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = categoryColor,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(2.dp))

          Text(
            text = service.department,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 11.sp
          )
        }

        // Bookmark Action
        IconButton(
          onClick = onBookmarkClick,
          modifier = Modifier
            .size(36.dp)
            .testTag("bookmark_button_${service.id}")
        ) {
          Icon(
            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
            contentDescription = if (isBookmarked) "Remove bookmark" else "Bookmark service",
            tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = service.shortDescription,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        lineHeight = 18.sp
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Bottom Row: Readiness Checklist Progress or Document Count + Navigation Indicator
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (readyCount > 0) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = if (isCompleted) StatusSuccessBg else MaterialTheme.colorScheme.primaryContainer
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (isCompleted) StatusSuccessColor else MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(13.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (isCompleted) "Ready to Apply (100%)" else "$readyCount of $totalCount Collected (${(readyCount * 100) / totalCount}%)",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (isCompleted) StatusSuccessColor else MaterialTheme.colorScheme.primary
              )
            }
          }
        } else {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
          ) {
            Text(
              text = "${service.requiredDocuments.size} Required Proofs",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Medium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }

        // View checklist indicator
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(end = 4.dp)
        ) {
          Text(
            text = "View Checklist",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = categoryColor
          )
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = categoryColor,
            modifier = Modifier.size(11.dp)
          )
        }
      }

      // Linear progress bar if user is tracking documents
      if (readyCount > 0 && totalCount > 0) {
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
          progress = { readyCount.toFloat() / totalCount.toFloat() },
          modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp)),
          color = if (isCompleted) StatusSuccessColor else categoryColor,
          trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
      }
    }
  }
}

// Compact Grid Card for 2-column view (shows many services at a glance!)
@Composable
fun CompactServiceCard(
  service: ServiceItem,
  isBookmarked: Boolean,
  onClick: () -> Unit,
  onBookmarkClick: () -> Unit,
  modifier: Modifier = Modifier,
  readyCount: Int = 0,
  totalCount: Int = service.requiredDocuments.size
) {
  val (categoryColor, categoryBg) = getCategoryTheme(service.category)
  val isCompleted = totalCount > 0 && readyCount == totalCount

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("compact_service_card_${service.id}")
      .clip(RoundedCornerShape(14.dp))
      .clickable(onClick = onClick)
      .border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
        shape = RoundedCornerShape(14.dp)
      ),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .background(categoryBg, RoundedCornerShape(10.dp)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = getServiceIcon(service.id),
            contentDescription = service.name,
            tint = categoryColor,
            modifier = Modifier.size(20.dp)
          )
        }

        IconButton(
          onClick = onBookmarkClick,
          modifier = Modifier.size(28.dp)
        ) {
          Icon(
            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
            contentDescription = null,
            tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = service.name,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(2.dp))

      Text(
        text = service.department,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontSize = 11.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(4.dp),
          color = if (readyCount > 0 && isCompleted) StatusSuccessBg else MaterialTheme.colorScheme.surfaceVariant
        ) {
          Text(
            text = if (readyCount > 0) "$readyCount/$totalCount Ready" else "${service.requiredDocuments.size} Docs",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = if (readyCount > 0 && isCompleted) StatusSuccessColor else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            fontSize = 10.sp
          )
        }

        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
          contentDescription = null,
          tint = categoryColor,
          modifier = Modifier.size(10.dp)
        )
      }
    }
  }
}
