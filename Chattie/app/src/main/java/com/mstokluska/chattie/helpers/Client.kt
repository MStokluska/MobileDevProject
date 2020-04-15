package com.mstokluska.chattie.helpers

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport
import okhttp3.OkHttpClient


object Client {


    // Local development SERVER URL:
    //var SERVER_URL = "http://54.194.196.137:4000/graphql"


    // AMAZON URL :
    var SERVER_URL = "http://chattieloadbalancer-1516544678.eu-west-1.elb.amazonaws.com:4000/graphql"

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