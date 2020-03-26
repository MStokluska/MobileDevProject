package com.mstokluska.chattie.main

import android.app.Application

import com.apollographql.apollo.ApolloClient
import com.mstokluska.chattie.helpers.Client
import com.mstokluska.chattie.models.ChatModel
import com.mstokluska.chattie.models.UserModel
import okhttp3.OkHttpClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class MainApp : Application(), AnkoLogger {

    val client = Client.init()
    val chats = ArrayList<ChatModel>()


    override fun onCreate() {
        super.onCreate()
        info("App started")


    }
}
