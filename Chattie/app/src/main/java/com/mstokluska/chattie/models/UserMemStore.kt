package com.mstokluska.chattie.models

class UserMemStore: UserStore {
    val currentUser = UserModel()

    override fun isLoggedIn(): Boolean {
        return !currentUser.name.isEmpty()
    }

    override fun createUser(user: UserModel) {
        currentUser.name = user.name
        currentUser.userName = user.userName
        currentUser.password = user.password
    }
}