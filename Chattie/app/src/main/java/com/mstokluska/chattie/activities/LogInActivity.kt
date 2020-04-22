package com.mstokluska.chattie.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.mstokluska.chattie.R
import com.mstokluska.chattie.main.MainApp
import com.mstokluska.chattie.models.UserModel
import com.mstokluska.graphql.CheckUserQuery
import com.mstokluska.graphql.GetIpQuery


import kotlinx.android.synthetic.main.log_in_activity.*
import org.jetbrains.anko.*

class LogInActivity : AppCompatActivity(), AnkoLogger {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.log_in_activity)
        app = application as MainApp

        val getIp = GetIpQuery.builder().build()
        app.client
            .query(getIp)
            .enqueue(object : ApolloCall.Callback<GetIpQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    info(e)
                }

                override fun onResponse(response: Response<GetIpQuery.Data>) {
                    runOnUiThread {
                        serverIp.text = getString(R.string.serverIp) + response.data()!!.ip
                    }
                }
            })

        btnSignIn.setOnClickListener() {
            if (userNickname.text.toString().isEmpty() or userPassword.text.toString().isEmpty()) {
                toast(getString(R.string.userPasswordRequired))
            } else {
                val checkUserQuery = CheckUserQuery.builder()
                    .username(userNickname.text.toString())
                    .password(userPassword.text.toString())
                    .build()

                app.client
                    .query(checkUserQuery)
                    .enqueue(object : ApolloCall.Callback<CheckUserQuery.Data>() {
                        override fun onFailure(e: ApolloException) {
                            info(e)
                        }

                        override fun onResponse(response: Response<CheckUserQuery.Data>) {
                            val resultUserName = response.data()?.checkUser()?.username()

                            runOnUiThread {
                                if (resultUserName != null) {
                                    app.users.currentUser.id = response.data()!!.checkUser().id()
                                    app.users.currentUser.name = response.data()!!.checkUser().name()
                                    app.users.currentUser.userName =
                                        response.data()!!.checkUser().username()
                                    app.users.currentUser.password =
                                        response.data()!!.checkUser().password()
                                    startActivityForResult<ChatsActivity>(0)
                                } else {
                                    toast(getString(R.string.incorrectDetails))
                                }
                            }
                        }
                    })
            }
        }
        btnSignUp.setOnClickListener() {
            startActivityForResult<SignUpActivity>(0)
        }
    }
}
