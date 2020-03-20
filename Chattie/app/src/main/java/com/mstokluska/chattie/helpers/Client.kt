package com.mstokluska.chattie.helpers

import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

object Client {

    var SERVER_URL = "http://10.0.2.2:4000/graphql"

    fun init ()= ApolloClient.builder()
    .serverUrl(SERVER_URL)
    .okHttpClient(
    OkHttpClient.Builder()
    .build()
    )
    .build()
}