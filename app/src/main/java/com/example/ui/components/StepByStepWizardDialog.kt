package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ServiceCategory
import com.example.data.model.ServiceItem
import com.example.data.repository.ServiceRepository
import com.example.ui.theme.StatusSuccessBg
import com.example.ui.theme.StatusSuccessColor
import com.example.ui.theme.StatusWarningBg
import com.example.ui.theme.StatusWarningColor
import com.example.viewmodel.DocBotViewModel

data class WizardCommonDoc(
  val id: String,
  val title: String,
  val keywordMatch: List<String>
)

val AvailableCommonDocs = listOf(
  WizardCommonDoc("aadhaar", "Aadhaar Card", listOf("aadhaar", "poi", "poa")),
  WizardCommonDoc("pan", "PAN Card", listOf("pan", "tax")),
  WizardCommonDoc("marksheet", "10th Marksheet / Passing Certificate", listOf("10th", "matric", "sslc", "age")),
  WizardCommonDoc("utility_bill", "Electricity / Utility Bill (<3 mos)", listOf("electricity", "utility", "poa", "address")),
  WizardCommonDoc("bank_passbook", "Bank Passbook / Statement", listOf("bank", "passbook", "statement")),
  WizardCommonDoc("photo", "Passport Size Photographs", listOf("photo", "photograph")),
  WizardCommonDoc("birth_cert", "Birth Certificate", listOf("birth", "dob", "age")),
  WizardCommonDoc("voter_id", "Voter ID Card", listOf("voter", "epic")),
  WizardCommonDoc("ration_card", "Ration Card", listOf("ration", "por")),
  WizardCommonDoc("income_slip", "Salary Slip / Income Proof", listOf("income", "salary"))
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StepByStepWizardDialog(
  viewModel: DocBotViewModel,
  onNavigateToService: (String) -> Unit,
  onDismiss: () -> Unit
) {
  val goalCategory by viewModel.wizardGoalCategory.collectAsState()
  val possessedDocs by viewModel.wizardPossessedDocs.collectAsState()
  var currentStep by remember { mutableIntStateOf(1) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .fillMaxHeight(0.88f)
        .clip(RoundedCornerShape(24.dp))
        .testTag("step_by_step_wizard_dialog"),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 8.dp,
      shape = RoundedCornerShape(24.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        // Dialog Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "Step-by-Step Guide",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "Document Eligibility Wizard",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("wizard_close_button")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close Wizard"
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Step Indicator Progress Bar
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("1. Goal", "2. Your Docs", "3. Results").forEachIndexed { index, label ->
            val stepNumber = index + 1
            val isActive = currentStep >= stepNumber
            val isCurrent = currentStep == stepNumber

            Column(modifier = Modifier.weight(1f)) {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(4.dp)
                  .background(
                    if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(2.dp)
                  )
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Wizard Step Body
        Box(modifier = Modifier.weight(1f)) {
          when (currentStep) {
            1 -> {
              // Step 1: Select Service Objective
              Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                  text = "Step 1: What is your objective?",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "Select what kind of public or government service you want to apply for:",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                  modifier = Modifier.weight(1f),
                  verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                  items(ServiceCategory.values()) { category ->
                    val isSelected = goalCategory == category
                    val sampleServices = when (category) {
                      ServiceCategory.ALL -> "All 12 Public & Government Services"
                      ServiceCategory.IDENTITY -> "Aadhaar, PAN Card, Voter ID"
                      ServiceCategory.TRAVEL -> "Passport (Normal & Tatkaal)"
                      ServiceCategory.TRANSPORT -> "Driving Licence, Learner's Permit"
                      ServiceCategory.CERTIFICATES -> "Income, Caste, Domicile, Birth, Death"
                      ServiceCategory.EDUCATION -> "Scholarship & Fee Reimbursement"
                      ServiceCategory.BENEFITS -> "Ration Card (NFSA / BPL / APL)"
                    }

                    Card(
                      modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.setWizardGoalCategory(category) }
                        .testTag("wizard_goal_${category.name.lowercase()}"),
                      colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                      ),
                      border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                      ),
                      shape = RoundedCornerShape(14.dp)
                    ) {
                      Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                      ) {
                        Box(
                          modifier = Modifier
                            .size(24.dp)
                            .background(
                              if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                              CircleShape
                            )
                            .then(
                              if (!isSelected) Modifier.background(
                                MaterialTheme.colorScheme.outlineVariant,
                                CircleShape
                              ) else Modifier
                            ),
                          contentAlignment = Alignment.Center
                        ) {
                          if (isSelected) {
                            Icon(
                              imageVector = Icons.Default.Check,
                              contentDescription = null,
                              tint = Color.White,
                              modifier = Modifier.size(16.dp)
                            )
                          }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                          Text(
                            text = category.displayName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                          )
                          Text(
                            text = sampleServices,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                          )
                        }
                      }
                    }
                  }
                }
              }
            }

            2 -> {
              // Step 2: Select Which Documents You Currently Have
              Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                  text = "Step 2: Which documents do you already have?",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "Select all proofs you currently possess in original or DigiLocker:",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                  modifier = Modifier.weight(1f),
                  verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  items(AvailableCommonDocs) { doc ->
                    val isChecked = possessedDocs.contains(doc.id)
                    Card(
                      modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.toggleWizardPossessedDoc(doc.id) }
                        .testTag("wizard_doc_${doc.id}"),
                      colors = CardDefaults.cardColors(
                        containerColor = if (isChecked) StatusSuccessBg else MaterialTheme.colorScheme.surface
                      ),
                      border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = if (isChecked) StatusSuccessColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                      ),
                      shape = RoundedCornerShape(12.dp)
                    ) {
                      Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                      ) {
                        Box(
                          modifier = Modifier
                            .size(24.dp)
                            .background(
                              if (isChecked) StatusSuccessColor else MaterialTheme.colorScheme.surfaceVariant,
                              RoundedCornerShape(6.dp)
                            ),
                          contentAlignment = Alignment.Center
                        ) {
                          if (isChecked) {
                            Icon(
                              imageVector = Icons.Default.Check,
                              contentDescription = null,
                              tint = Color.White,
                              modifier = Modifier.size(16.dp)
                            )
                          }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                          text = doc.title,
                          style = MaterialTheme.typography.bodyMedium,
                          fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal,
                          color = MaterialTheme.colorScheme.onSurface
                        )
                      }
                    }
                  }
                }
              }
            }

            3 -> {
              // Step 3: Instant Eligibility Match
              val targetServices = if (goalCategory == ServiceCategory.ALL) {
                ServiceRepository.services
              } else {
                ServiceRepository.services.filter { it.category == goalCategory }
              }

              Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                  text = "Step 3: Eligibility & Roadmap",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "Based on your ${possessedDocs.size} available documents:",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                  modifier = Modifier.weight(1f),
                  verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                  items(targetServices) { service ->
                    // Calculate matches based on possessed docs
                    val requiredDocs = service.requiredDocuments
                    val possessedKeywordList = AvailableCommonDocs
                      .filter { possessedDocs.contains(it.id) }
                      .flatMap { it.keywordMatch }

                    val matchedDocs = requiredDocs.filter { reqDoc ->
                      val docText = (reqDoc.name + " " + reqDoc.purpose + " " + reqDoc.examples.joinToString(" ")).lowercase()
                      possessedKeywordList.any { kw -> docText.contains(kw) }
                    }

                    val missingDocs = requiredDocs.filterNot { matchedDocs.contains(it) }
                    val is100Ready = missingDocs.isEmpty()
                    val isAlmostReady = missingDocs.size == 1

                    Card(
                      modifier = Modifier
                        .fillMaxWidth()
                        .testTag("wizard_result_${service.id}"),
                      colors = CardDefaults.cardColors(
                        containerColor = when {
                          is100Ready -> StatusSuccessBg.copy(alpha = 0.7f)
                          isAlmostReady -> StatusWarningBg.copy(alpha = 0.7f)
                          else -> MaterialTheme.colorScheme.surface
                        }
                      ),
                      border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = when {
                          is100Ready -> StatusSuccessColor
                          isAlmostReady -> StatusWarningColor
                          else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        }
                      ),
                      shape = RoundedCornerShape(14.dp)
                    ) {
                      Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                          modifier = Modifier.fillMaxWidth(),
                          horizontalArrangement = Arrangement.SpaceBetween,
                          verticalAlignment = Alignment.CenterVertically
                        ) {
                          Text(
                            text = service.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                          )

                          // Status Pill
                          Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = when {
                              is100Ready -> StatusSuccessColor
                              isAlmostReady -> StatusWarningColor
                              else -> MaterialTheme.colorScheme.outlineVariant
                            }
                          ) {
                            Text(
                              text = when {
                                is100Ready -> "100% Ready!"
                                isAlmostReady -> "1 Doc Missing"
                                else -> "${missingDocs.size} Docs Missing"
                              },
                              style = MaterialTheme.typography.labelSmall,
                              fontWeight = FontWeight.Bold,
                              color = Color.White,
                              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                          }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        if (is100Ready) {
                          Text(
                            text = "✓ You have all mandatory proofs! Ready to apply immediately on official portal.",
                            style = MaterialTheme.typography.bodySmall,
                            color = StatusSuccessColor,
                            fontWeight = FontWeight.Medium
                          )
                        } else {
                          Text(
                            text = "Next Step: You still need ${missingDocs.firstOrNull()?.name ?: "remaining proofs"}.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                          )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                          modifier = Modifier.fillMaxWidth(),
                          horizontalArrangement = Arrangement.End
                        ) {
                          OutlinedButton(
                            onClick = {
                              onDismiss()
                              onNavigateToService(service.id)
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(34.dp)
                          ) {
                            Text(
                              text = "View Checklist →",
                              fontSize = 12.sp
                            )
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dialog Bottom Navigation Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          if (currentStep > 1) {
            OutlinedButton(
              onClick = { currentStep -= 1 },
              shape = RoundedCornerShape(10.dp)
            ) {
              Text("← Back")
            }
          } else {
            OutlinedButton(
              onClick = { viewModel.resetWizard() },
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(
                imageVector = Icons.Default.RestartAlt,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text("Reset")
            }
          }

          if (currentStep < 3) {
            Button(
              onClick = { currentStep += 1 },
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.testTag("wizard_next_button")
            ) {
              Text(if (currentStep == 1) "Next: Pick Docs →" else "See My Results →")
            }
          } else {
            Button(
              onClick = onDismiss,
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.testTag("wizard_done_button")
            ) {
              Text("Done")
            }
          }
        }
      }
    }
  }
}
