package com.example.data.model

enum class ServiceCategory(val displayName: String) {
  ALL("All"),
  IDENTITY("Identity"),
  CERTIFICATES("Certificates"),
  TRAVEL("Travel"),
  TRANSPORT("Transport"),
  EDUCATION("Education"),
  BENEFITS("Benefits")
}

data class DocumentItem(
  val id: String,
  val name: String,
  val purpose: String,
  val examples: List<String>,
  val required: Boolean = true,
  val notes: String = ""
)

data class ApplicationStep(
  val stepNumber: Int,
  val title: String,
  val description: String
)

data class FaqItem(
  val question: String,
  val answer: String
)

data class StatePortalInfo(
  val stateName: String,
  val portalName: String,
  val portalUrl: String,
  val stateNote: String = ""
)

data class ServiceItem(
  val id: String,
  val name: String,
  val shortDescription: String,
  val description: String,
  val category: ServiceCategory,
  val department: String,
  val officialPortalUrl: String,
  val estimatedFee: String,
  val processingTime: String,
  val applicationMode: String = "Online & Offline",
  val requiredDocuments: List<DocumentItem>,
  val optionalDocuments: List<DocumentItem> = emptyList(),
  val eligibilityCriteria: List<String>,
  val applicationSteps: List<ApplicationStep>,
  val importantNotes: List<String>,
  val commonRejections: List<String> = emptyList(),
  val statePortals: List<StatePortalInfo> = emptyList(),
  val faqs: List<FaqItem> = emptyList(),
  val keywords: List<String> = emptyList()
)
