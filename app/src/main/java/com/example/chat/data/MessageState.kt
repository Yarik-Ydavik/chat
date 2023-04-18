package com.example.chat.data

sealed class MessageState{
    class Success (val data: MutableList<Message>): MessageState()
    class Failure (val message: String): MessageState()
    object Loading : MessageState()
    object Empty : MessageState()
}
