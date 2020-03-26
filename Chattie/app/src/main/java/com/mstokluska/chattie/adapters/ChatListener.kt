package com.mstokluska.chattie.adapters

import com.mstokluska.chattie.models.ChatModel

interface ChatListener {
    fun onDeleteClick(chat: ChatModel)
    fun onChatClick(chat: ChatModel)
}