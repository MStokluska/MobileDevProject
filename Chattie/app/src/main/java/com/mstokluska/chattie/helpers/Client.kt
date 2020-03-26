package com.mstokluska.chattie.helpers

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport
import okhttp3.OkHttpClient


object Client {

    var SERVER_URL = "http://10.0.2.2:4000/graphql"

    var okHttpClient =
        OkHttpClient.Builder()
        .build()

    fun init ()= ApolloClient.builder()
    .serverUrl(SERVER_URL)
    .okHttpClient(okHttpClient)
        .subscriptionTransportFactory(WebSocketSubscriptionTransport.Factory(SERVER_URL,
            okHttpClient))
    .build()
}