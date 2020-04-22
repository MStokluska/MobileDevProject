package com.mstokluska.chattie.models

interface UserStore {
    fun isLoggedIn(): Boolean
    fun createUser(user: UserModel)
}