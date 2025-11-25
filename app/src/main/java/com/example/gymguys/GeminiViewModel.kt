package com.example.gymguys

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val isLoading: Boolean = false
)

data class GeminiUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class GeminiViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GeminiUiState())
    val uiState: StateFlow<GeminiUiState> = _uiState.asStateFlow()

    private val generativeModel: GenerativeModel by lazy {
        GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = GeminiConfig.API_KEY
        )
    }

    private var isFirstMessage = true

    private val systemInstructionText = """
        You are an intelligent assistant specialized in gym and sports. 
        Only answer questions related to gym, sports, workouts, sports nutrition, supplements, and fitness.
        If the user's question is not related to gym and sports, politely say that you can only help with gym and sports topics.
        Always provide helpful, accurate, and practical answers.
        Respond in the same language as the user's question (Persian/Farsi or English).
        
       
    """.trimIndent()

    fun sendMessage(userMessage: String) {
        if (userMessage.isBlank()) return
        
        val apiKey = GeminiConfig.API_KEY.trim()
        if (apiKey.isBlank() || apiKey == "YOUR_GEMINI_API_KEY_HERE") {
            _uiState.value = _uiState.value.copy(
                error = "Please set your API Key in GeminiConfig.kt file first.",
                isLoading = false
            )
            return
        }
        
        if (!apiKey.startsWith("AIza") || apiKey.length < 30) {
            _uiState.value = _uiState.value.copy(
                error = "Invalid API Key. Please get a valid API Key from Google AI Studio.",
                isLoading = false
            )
            return
        }
        val userChatMessage = ChatMessage(text = userMessage, isUser = true)
        val loadingMessage = ChatMessage(text = "", isUser = false, isLoading = true)
        
        val currentMessages = _uiState.value.messages.filter { !it.isLoading }
        _uiState.value = _uiState.value.copy(
            messages = currentMessages + userChatMessage + loadingMessage,
            isLoading = true,
            error = null
        )

        viewModelScope.launch {
            try {
                val chatHistory = currentMessages.joinToString("\n") { message ->
                    if (message.isUser) "User: ${message.text}" else "Assistant: ${message.text}"
                }
                
                val prompt = if (chatHistory.isNotEmpty()) {
                    "$systemInstructionText\n\n$chatHistory\n\nUser: $userMessage\n\nAssistant:"
                } else {
                    "$systemInstructionText\n\nUser: $userMessage\n\nAssistant:"
                }
                
                val response = generativeModel.generateContent(prompt)
                val responseText = response.text ?: "Sorry, I couldn't generate a response. Please try again."
                
                if (isFirstMessage) {
                    isFirstMessage = false
                }

                val updatedMessages = _uiState.value.messages.filter { !it.isLoading }
                val assistantMessage = ChatMessage(text = responseText, isUser = false)
                
                _uiState.value = _uiState.value.copy(
                    messages = updatedMessages + assistantMessage,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                val updatedMessages = _uiState.value.messages.filter { !it.isLoading }
                val errorString = (e.message ?: e.toString()).lowercase()
                
                val errorMessage = when {
                    errorString.contains("403") ||
                    errorString.contains("forbidden") ||
                    errorString.contains("json conversion failed") ||
                    errorString.contains("doctype html") ||
                    errorString.contains("html") && errorString.contains("403") ||
                    errorString.contains("network") ||
                    errorString.contains("unable to resolve host") ||
                    errorString.contains("failed to connect") ||
                    errorString.contains("connection") ||
                    errorString.contains("timeout") ||
                    errorString.contains("no internet") ||
                    errorString.contains("socket") ||
                    errorString.contains("unreachable") ->
                        "you are not connected to the internet"
                    e.message?.contains("API_KEY", ignoreCase = true) == true || 
                    e.message?.contains("API key", ignoreCase = true) == true ||
                    e.message?.contains("API_KEY_NOT_SET", ignoreCase = true) == true -> 
                        "Error: Please set your API Key in GeminiConfig.kt file."
                    e.message?.contains("429", ignoreCase = true) == true ||
                    e.message?.contains("quota", ignoreCase = true) == true ->
                        "Error: Too many requests. Please wait a moment and try again."
                    e.message?.contains("401", ignoreCase = true) == true ||
                    e.message?.contains("unauthorized", ignoreCase = true) == true ->
                        "Error: Invalid API Key. Please check your API Key."
                    e.message?.contains("PERMISSION_DENIED", ignoreCase = true) == true ->
                        "Error: API access denied. Please check your API Key."
                    e.message?.contains("404", ignoreCase = true) == true ||
                    e.message?.contains("not found", ignoreCase = true) == true ->
                        "Error: Model not found. Please check the model name."
                    e.message?.contains("400", ignoreCase = true) == true ->
                        "Error: Invalid request. Please try again."
                    else -> {
                        val detailedError = e.message ?: e.javaClass.simpleName
                        "Error: $detailedError\n\nIf the problem persists, please check your API Key and internet connection."
                    }
                }
                
                _uiState.value = _uiState.value.copy(
                    messages = updatedMessages,
                    isLoading = false,
                    error = errorMessage
                )
                
                e.printStackTrace()
            }
        }
    }

    fun clearChat() {
        isFirstMessage = true
        _uiState.value = GeminiUiState()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

