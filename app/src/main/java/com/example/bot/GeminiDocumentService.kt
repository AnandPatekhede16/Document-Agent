package com.example.bot

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.ChatMessage
import com.example.data.model.MessageSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiDocumentService {

  private const val TAG = "GeminiDocService"
  // Models list with fallback in case of temporary 503 load spikes on a specific model
  private val CANDIDATE_MODELS = listOf(
    "gemini-3.5-flash",
    "gemini-3.8-flash",
    "gemini-3.1-flash-lite-preview",
    "gemini-flash-latest"
  )

  private val client = OkHttpClient.Builder()
    .connectTimeout(15, TimeUnit.SECONDS)
    .readTimeout(25, TimeUnit.SECONDS)
    .writeTimeout(15, TimeUnit.SECONDS)
    .build()

  /**
   * Checks whether a valid Gemini API key is configured.
   */
  fun isApiKeyAvailable(customKey: String? = null): Boolean {
    val key = resolveApiKey(customKey)
    return key.isNotBlank() && key != "MY_GEMINI_API_KEY"
  }

  fun resolveApiKey(customKey: String? = null): String {
    if (!customKey.isNullOrBlank() && customKey != "MY_GEMINI_API_KEY") {
      return customKey.trim()
    }
    return try {
      val buildKey = BuildConfig.GEMINI_API_KEY
      if (buildKey.isNotBlank() && buildKey != "MY_GEMINI_API_KEY") {
        buildKey.trim()
      } else {
        ""
      }
    } catch (e: Exception) {
      Log.e(TAG, "Error reading BuildConfig.GEMINI_API_KEY", e)
      ""
    }
  }

  /**
   * Calls the Gemini REST API dynamically according to the user's exact question.
   * Returns a dynamic ChatMessage directly tailored to the user's prompt.
   */
  suspend fun askGemini(question: String, customKey: String? = null): ChatMessage? = withContext(Dispatchers.IO) {
    val apiKey = resolveApiKey(customKey)
    if (apiKey.isBlank()) {
      Log.d(TAG, "No Gemini API key available.")
      return@withContext null
    }

    val systemInstruction = "You are Document Requirement Bot, an expert AI assistant dedicated to answering any and all user questions about Indian education, academic, and government documents and public services.\n\n" +
      "You MUST answer dynamically, thoroughly, and specifically based on the user's exact prompt. Do not use generic placeholders.\n\n" +
      "Format your answer with clear markdown:\n" +
      "• 📄 **Required Documents** (specific to the user's query with purpose and accepted proofs)\n" +
      "• 🎯 **Eligibility Criteria**\n" +
      "• 📝 **Step-by-Step Procedure / Application Steps**\n" +
      "• ⚠️ **Important Rules & Pitfalls to Avoid**\n\n" +
      "Be professional, accurate, and helpful."

    // Construct request payload
    val rootJson = JSONObject().apply {
      val contentsArray = JSONArray().apply {
        val contentObj = JSONObject().apply {
          val partsArray = JSONArray().apply {
            put(JSONObject().put("text", question))
          }
          put("parts", partsArray)
        }
        put(contentObj)
      }
      put("contents", contentsArray)

      val systemInstructionObj = JSONObject().apply {
        val partsArray = JSONArray().apply {
          put(JSONObject().put("text", systemInstruction))
        }
        put("parts", partsArray)
      }
      put("systemInstruction", systemInstructionObj)
    }

    val requestBodyString = rootJson.toString()

    // Try each model until one succeeds
    for (model in CANDIDATE_MODELS) {
      try {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"
        val requestBody = requestBodyString.toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
          .url(url)
          .post(requestBody)
          .build()

        val response = client.newCall(request).execute()
        val responseBodyString = response.body?.string() ?: ""

        if (response.isSuccessful) {
          val jsonResponse = JSONObject(responseBodyString)
          val candidates = jsonResponse.optJSONArray("candidates")
          if (candidates != null && candidates.length() > 0) {
            val firstCandidate = candidates.getJSONObject(0)
            val content = firstCandidate.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            if (parts != null && parts.length() > 0) {
              val answerText = parts.getJSONObject(0).optString("text")
              if (answerText.isNotBlank()) {
                return@withContext ChatMessage(
                  sender = MessageSender.BOT,
                  text = answerText,
                  isAiPowered = true
                )
              }
            }
          }
        } else {
          Log.w(TAG, "Model $model returned code ${response.code}: $responseBodyString")
        }
      } catch (e: Exception) {
        Log.e(TAG, "Error invoking model $model", e)
      }
    }

    return@withContext null
  }
}
