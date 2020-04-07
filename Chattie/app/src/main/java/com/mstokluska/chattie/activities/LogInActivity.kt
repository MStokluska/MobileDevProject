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
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.log_in_activity)
        app = application as MainApp

        val getIp = GetIpQuery.builder().build()

        app.client
            .query(getIp)
            .enqueue(object: ApolloCall.Callback<GetIpQuery.Data>(){

                override fun onFailure(e: ApolloException) {

                }

                override fun onResponse(response: Response<GetIpQuery.Data>) {
                    runOnUiThread{
                        serverIp.text = "Server IP is " + response.data()!!.ip
                    }

                }
            })


        btnSignIn.setOnClickListener() {
            user.userName = userNickname.text.toString()
            user.password = userPassword.text.toString()


            if (user.userName.isEmpty() or user.password.isEmpty()) {
                toast("Username and Password are required")
            } else {

                val checkUserQuery = CheckUserQuery.builder()
                    .username(user.userName)
                    .password(user.password)
                    .build()

                app.client
                    .query(checkUserQuery)
                    .enqueue(object : ApolloCall.Callback<CheckUserQuery.Data>() {

                        override fun onFailure(e: ApolloException) {

                        }

                        override fun onResponse(response: Response<CheckUserQuery.Data>) {

                            val resultUserName = response.data()?.checkUser()?.username()

                            runOnUiThread {
                                if (resultUserName != null) {
                                    user.id = response.data()!!.checkUser().id()
                                    user.name = response.data()!!.checkUser().name()
                                    user.userName = response.data()!!.checkUser().username()
                                    user.password = response.data()!!.checkUser().password()

                                    startActivityForResult(
                                        intentFor<ChatsActivity>().putExtra(
                                            "user_logged_in",
                                            user
                                        ), 0
                                    )

                                } else {
                                    toast("Incorrect username or password")
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
