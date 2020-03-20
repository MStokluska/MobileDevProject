package com.mstokluska.chattie.main

import android.app.Application

import com.apollographql.apollo.ApolloClient
import com.mstokluska.chattie.helpers.Client
import okhttp3.OkHttpClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class MainApp : Application(), AnkoLogger {

    val client = Client.init()

    override fun onCreate() {
        super.onCreate()
        info("App started")


    }
}
