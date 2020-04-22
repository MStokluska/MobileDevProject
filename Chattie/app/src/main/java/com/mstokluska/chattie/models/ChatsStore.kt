package com.mstokluska.chattie.models
 interface ChatsStore {
     fun addChat(chat: ChatModel)
     fun removeChat(chatIndex: String?): Boolean
}