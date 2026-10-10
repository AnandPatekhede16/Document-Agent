package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bot.DocumentBotEngine
import com.example.bot.GeminiDocumentService
import com.example.data.local.UserPreferencesRepository
import com.example.data.model.ChatMessage
import com.example.data.model.DocumentItem
import com.example.data.model.MessageSender
import com.example.data.model.ServiceCategory
import com.example.data.model.ServiceItem
import com.example.data.repository.ServiceRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DocBotViewModel(application: Application) : AndroidViewModel(application) {

  private val prefsRepo = UserPreferencesRepository(application.applicationContext)

  // -------------------------------------------------------------
  // Home Screen Specific State (Independent to prevent coupling)
  // -------------------------------------------------------------
  private val _homeCategory = MutableStateFlow(ServiceCategory.ALL)
  val homeCategory: StateFlow<ServiceCategory> = _homeCategory.asStateFlow()

  private val _homeSearchQuery = MutableStateFlow("")
  val homeSearchQuery: StateFlow<String> = _homeSearchQuery.asStateFlow()

  private val _isGridView = MutableStateFlow(false)
  val isGridView: StateFlow<Boolean> = _isGridView.asStateFlow()

  // Guaranteed full services for Home Screen (when category is ALL and query is empty -> 12 services)
  val homeServices: StateFlow<List<ServiceItem>> = combine(
    _homeSearchQuery,
    _homeCategory
  ) { query, category ->
    ServiceRepository.searchServices(query, category)
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ServiceRepository.services)

  // -------------------------------------------------------------
  // Search Screen Specific State
  // -------------------------------------------------------------
  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedCategory = MutableStateFlow(ServiceCategory.ALL)
  val selectedCategory: StateFlow<ServiceCategory> = _selectedCategory.asStateFlow()

  // Filtered Services for Search Screen
  val filteredServices: StateFlow<List<ServiceItem>> = combine(
    _searchQuery,
    _selectedCategory
  ) { query, category ->
    ServiceRepository.searchServices(query, category)
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ServiceRepository.services)

  // -------------------------------------------------------------
  // Bookmarks & Persistence
  // -------------------------------------------------------------
  val bookmarkedServiceIds: StateFlow<Set<String>> = prefsRepo.bookmarkedServiceIds
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

  val bookmarkedServices: StateFlow<List<ServiceItem>> = bookmarkedServiceIds
    .combine(MutableStateFlow(ServiceRepository.services)) { ids, allServices ->
      allServices.filter { ids.contains(it.id) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Recently Viewed Services (Max 5)
  val recentServiceIds: StateFlow<List<String>> = prefsRepo.recentServiceIds
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val recentServices: StateFlow<List<ServiceItem>> = recentServiceIds
    .combine(MutableStateFlow(ServiceRepository.services)) { ids, allServices ->
      ids.mapNotNull { id -> allServices.find { it.id == id } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Readiness Checklist Tracker: Set of "$serviceId:$docId"
  val checkedDocumentKeys: StateFlow<Set<String>> = prefsRepo.checkedDocumentKeys
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

  // Chatbot State
  private val _chatMessages = MutableStateFlow<List<ChatMessage>>(listOf(DocumentBotEngine.getInitialGreeting()))
  val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

  private val _isBotTyping = MutableStateFlow(false)
  val isBotTyping: StateFlow<Boolean> = _isBotTyping.asStateFlow()

  val customApiKey: StateFlow<String> = prefsRepo.customGeminiApiKey
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

  val isGeminiAvailable: StateFlow<Boolean> = customApiKey
    .map { key -> GeminiDocumentService.isApiKeyAvailable(key) }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GeminiDocumentService.isApiKeyAvailable())

  // Document details dialog
  private val _activeDocumentModal = MutableStateFlow<DocumentItem?>(null)
  val activeDocumentModal: StateFlow<DocumentItem?> = _activeDocumentModal.asStateFlow()

  // -------------------------------------------------------------
  // Step-by-Step Document Eligibility Wizard State
  // -------------------------------------------------------------
  private val _isWizardOpen = MutableStateFlow(false)
  val isWizardOpen: StateFlow<Boolean> = _isWizardOpen.asStateFlow()

  private val _wizardGoalCategory = MutableStateFlow(ServiceCategory.ALL)
  val wizardGoalCategory: StateFlow<ServiceCategory> = _wizardGoalCategory.asStateFlow()

  private val _wizardPossessedDocs = MutableStateFlow<Set<String>>(emptySet())
  val wizardPossessedDocs: StateFlow<Set<String>> = _wizardPossessedDocs.asStateFlow()

  // -------------------------------------------------------------
  // Home Screen Methods
  // -------------------------------------------------------------
  fun onHomeCategorySelect(category: ServiceCategory) {
    _homeCategory.value = category
  }

  fun onHomeSearchQueryChange(query: String) {
    _homeSearchQuery.value = query
  }

  fun toggleViewMode() {
    _isGridView.value = !_isGridView.value
  }

  // -------------------------------------------------------------
  // Search Screen Methods
  // -------------------------------------------------------------
  fun onSearchQueryChange(query: String) {
    _searchQuery.value = query
  }

  fun onCategorySelect(category: ServiceCategory) {
    _selectedCategory.value = category
  }

  // -------------------------------------------------------------
  // Bookmark & History Methods
  // -------------------------------------------------------------
  fun toggleBookmark(serviceId: String) {
    viewModelScope.launch {
      prefsRepo.toggleBookmark(serviceId)
    }
  }

  fun removeBookmark(serviceId: String) {
    viewModelScope.launch {
      prefsRepo.removeBookmark(serviceId)
    }
  }

  fun onServiceOpened(serviceId: String) {
    viewModelScope.launch {
      prefsRepo.addRecentService(serviceId)
    }
  }

  fun openDocumentDialog(documentItem: DocumentItem) {
    _activeDocumentModal.value = documentItem
  }

  fun closeDocumentDialog() {
    _activeDocumentModal.value = null
  }

  // -------------------------------------------------------------
  // Interactive Checklist Methods
  // -------------------------------------------------------------
  fun toggleDocumentChecked(serviceId: String, docId: String) {
    viewModelScope.launch {
      prefsRepo.toggleDocumentChecked(serviceId, docId)
    }
  }

  fun isDocumentChecked(serviceId: String, docId: String, checkedKeys: Set<String>): Boolean {
    return checkedKeys.contains("$serviceId:$docId")
  }

  fun getServiceProgress(service: ServiceItem, checkedKeys: Set<String>): Float {
    val total = service.requiredDocuments.size
    if (total == 0) return 0f
    val checkedCount = service.requiredDocuments.count { isDocumentChecked(service.id, it.id, checkedKeys) }
    return checkedCount.toFloat() / total.toFloat()
  }

  fun getCheckedCountForService(service: ServiceItem, checkedKeys: Set<String>): Pair<Int, Int> {
    val total = service.requiredDocuments.size
    val checkedCount = service.requiredDocuments.count { isDocumentChecked(service.id, it.id, checkedKeys) }
    return Pair(checkedCount, total)
  }

  fun resetChecklist(serviceId: String) {
    viewModelScope.launch {
      prefsRepo.resetServiceChecklist(serviceId)
    }
  }

  // -------------------------------------------------------------
  // Chatbot Methods
  // -------------------------------------------------------------
  fun sendChatMessage(text: String) {
    val cleanText = text.trim()
    if (cleanText.isEmpty()) return

    val userMsg = ChatMessage(
      sender = MessageSender.USER,
      text = cleanText
    )
    _chatMessages.value = _chatMessages.value + userMsg

    viewModelScope.launch {
      _isBotTyping.value = true

      // 1. Attempt live Gemini AI answer first if key is available and device is online
      val key = customApiKey.value
      val aiReply = GeminiDocumentService.askGemini(cleanText, key)

      if (aiReply != null) {
        _chatMessages.value = _chatMessages.value + aiReply
      } else {
        // 2. Seamless local/offline intelligent engine fallback
        delay(350)
        val botReply = DocumentBotEngine.processQuestion(cleanText)
        _chatMessages.value = _chatMessages.value + botReply
      }
      _isBotTyping.value = false
    }
  }

  fun setCustomApiKey(key: String) {
    viewModelScope.launch {
      prefsRepo.setCustomApiKey(key)
    }
  }

  fun askAboutService(service: ServiceItem) {
    sendChatMessage("What documents do I need for ${service.name}?")
  }

  fun clearChat() {
    _chatMessages.value = listOf(DocumentBotEngine.getInitialGreeting())
  }

  // -------------------------------------------------------------
  // Step-by-Step Wizard Methods
  // -------------------------------------------------------------
  fun openWizard() {
    _isWizardOpen.value = true
  }

  fun closeWizard() {
    _isWizardOpen.value = false
  }

  fun setWizardGoalCategory(category: ServiceCategory) {
    _wizardGoalCategory.value = category
  }

  fun toggleWizardPossessedDoc(docKey: String) {
    val current = _wizardPossessedDocs.value.toMutableSet()
    if (current.contains(docKey)) {
      current.remove(docKey)
    } else {
      current.add(docKey)
    }
    _wizardPossessedDocs.value = current
  }

  fun resetWizard() {
    _wizardPossessedDocs.value = emptySet()
    _wizardGoalCategory.value = ServiceCategory.ALL
  }
}
