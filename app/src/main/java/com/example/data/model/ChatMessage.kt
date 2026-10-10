package com.example.data.model

enum class MessageSender {
  USER,
  BOT
}

data class ChatMessage(
  val id: String = java.util.UUID.randomUUID().toString(),
  val sender: MessageSender,
  val text: String,
  val timestamp: Long = System.currentTimeMillis(),
  val matchedServiceId: String? = null,
  val matchedServiceName: String? = null,
  val quickActions: List<String> = emptyList(),
  val isAiPowered: Boolean = false
)
