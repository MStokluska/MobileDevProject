package com.mstokluska.chattie.adapters

import com.mstokluska.chattie.models.UserModel

interface UsersListener {
    fun onUserClick(user: UserModel)
}