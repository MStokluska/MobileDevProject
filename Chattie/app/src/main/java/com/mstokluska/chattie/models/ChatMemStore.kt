package com.mstokluska.chattie.models

class ChatMemStore : ChatsStore {
    var chatsArray = ArrayList<ChatModel>()

    override fun addChat(chat: ChatModel) {
        chatsArray.add(chat)
    }

    override fun removeChat(chatIndex: String?): Boolean {
        val indexOfDeleted = chatsArray.indexOfFirst {
            it.id == chatIndex
        }
        if (indexOfDeleted != -1) {
            chatsArray.removeAt(indexOfDeleted)
            return true
        } else {
            return false
        }
    }
}