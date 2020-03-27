package com.mstokluska.chattie.adapters

import com.mstokluska.chattie.models.MessageModel

interface MessagesListener {
    fun onMessageClick(message: MessageModel)
}