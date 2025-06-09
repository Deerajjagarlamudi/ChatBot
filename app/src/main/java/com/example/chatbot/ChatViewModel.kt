package com.example.chatbot

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateListOf
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel : ViewModel() {

    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    private val generativeModel = GenerativeModel(
        modelName = "models/gemini-1.5-flash",
        apiKey = Constants.apiKey
    )

    fun sendMessage(question: String) {
        Log.i("ChatViewModel", "User asked: $question")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val chat = generativeModel.startChat()

                withContext(Dispatchers.Main) {
                    messageList.add(MessageModel(question, "user"))
                    messageList.add(MessageModel("typing...", "model"))
                }

                val response = chat.sendMessage(question)

                Log.d("Gemini", response.text ?: "No response")

                withContext(Dispatchers.Main) {
                    if (messageList.isNotEmpty()) {
                        messageList.removeAt(messageList.size - 1)
                    }

                    messageList.add(
                        MessageModel(response.text ?: "No response", "model")
                    )
                }

            } catch (e: Exception) {
                Log.e("Gemini Error", e.toString())
                withContext(Dispatchers.Main) {
                    if (messageList.isNotEmpty()) {
                        messageList.removeAt(messageList.size - 1)
                    }

                    messageList.add(
                        MessageModel("Error: ${e.localizedMessage ?: "Unknown error"}", "model")
                    )
                }
            }
        }
    }
}