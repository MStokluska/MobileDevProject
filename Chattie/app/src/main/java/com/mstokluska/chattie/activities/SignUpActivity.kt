package com.mstokluska.chattie.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.mstokluska.chattie.R
import com.mstokluska.chattie.main.MainApp
import com.mstokluska.chattie.models.UserMemStore
import com.mstokluska.chattie.models.UserModel
import com.mstokluska.graphql.CreateUserMutation
import com.mstokluska.graphql.EditUserMutation
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.*

class SignUpActivity : AppCompatActivity(), AnkoLogger {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        app = application as MainApp

        toolbarCreateUser.title = getString(R.string.sign_up)
        setSupportActionBar(toolbarCreateUser)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (app.users.isLoggedIn()) {
            btnCreateUser.setText(getString(R.string.update))
            toolbarCreateUser.title = getString(R.string.editDetails)
            user_create_Nickname.setText(app.users.currentUser.userName)
            user_create_name.setText(app.users.currentUser.name)
            user_create_Password.visibility = View.INVISIBLE
        }

        btnCreateUser.setOnClickListener() {
            if (!app.users.isLoggedIn()) {
                val newUser = UserModel(
                    name = user_create_name.text.toString(),
                    userName = user_create_Nickname.text.toString(),
                    password = user_create_Password.text.toString()
                )

                app.users.createUser(newUser)

                if (app.users.currentUser.name.isEmpty() or app.users.currentUser.userName.isEmpty() or app.users.currentUser.password.isEmpty()) {
                    toast(getString(R.string.allFields))
                } else {
                    val createUserMutation = CreateUserMutation.builder()
                        .name(app.users.currentUser.name)
                        .username(app.users.currentUser.userName)
                        .password(app.users.currentUser.password)
                        .build()

                    app.client
                        .mutate(createUserMutation)
                        .enqueue(object : ApolloCall.Callback<CreateUserMutation.Data>() {
                            override fun onFailure(e: ApolloException) {
                                info(e)
                            }

                            override fun onResponse(response: Response<CreateUserMutation.Data>) {
                                val resultUserName = response.data()?.createUser()?.username()
                                runOnUiThread {
                                    if (resultUserName != null) {
                                        toast(getString(R.string.userCreated))
                                        finish()
                                    } else {
                                        toast(getString(R.string.userNameTaken))
                                        user_create_Nickname.setText("")
                                    }
                                }
                            }
                        })
                }
            } else {
                val editUser = EditUserMutation.builder()
                    .id(app.users.currentUser.id)
                    .name(user_create_name.text.toString())
                    .username(user_create_Nickname.text.toString())
                    .password(app.users.currentUser.password)
                    .build()

                app.client
                    .mutate(editUser)
                    .enqueue(object : ApolloCall.Callback<EditUserMutation.Data>() {
                        override fun onFailure(e: ApolloException) {
                            info(e)
                        }

                        override fun onResponse(response: Response<EditUserMutation.Data>) {
                            app.users.currentUser.userName =
                                response.data()!!.editUser()!!.username()
                            app.users.currentUser.name = response.data()!!.editUser()!!.name()
                            runOnUiThread {
                                toast(getString(R.string.userUpdated))
                                finish()
                            }
                        }
                    })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
