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
import com.mstokluska.chattie.models.UserModel
import com.mstokluska.graphql.CreateUserMutation
import com.mstokluska.graphql.EditUserMutation
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.*

class SignUpActivity : AppCompatActivity(), AnkoLogger {

    lateinit var app: MainApp
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        app = application as MainApp

        // menu bar
        toolbarCreateUser.title = "Sign Up"
        setSupportActionBar(toolbarCreateUser)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(app.currentUser.userName.isNotEmpty()){
            btnCreateUser.setText("Update!")
            toolbarCreateUser.title = "Edit details"
            user_create_Nickname.setText(app.currentUser.userName)
            user_create_name.setText(app.currentUser.name)
            user_create_Password.visibility = View.INVISIBLE
        }

        btnCreateUser.setOnClickListener() {

            if (app.currentUser.userName.isEmpty()) {
                user.name = user_create_name.text.toString()
                user.userName = user_create_Nickname.text.toString()
                user.password = user_create_Password.text.toString()

                if (user.name.isEmpty() or user.userName.isEmpty() or user.password.isEmpty()) {
                    toast("Make sure all fields are filled")
                } else {

                    val createUserMutation = CreateUserMutation.builder()
                        .name(user.name)
                        .username(user.userName)
                        .password(user.password)
                        .build()


                    app.client
                        .mutate(createUserMutation)
                        .enqueue(object : ApolloCall.Callback<CreateUserMutation.Data>() {
                            override fun onFailure(e: ApolloException) {
                                e.printStackTrace()
                            }

                            override fun onResponse(response: Response<CreateUserMutation.Data>) {

                                val resultUserName = response.data()?.createUser()?.username()

                                runOnUiThread {

                                    if (resultUserName != null) {
                                        toast("User Created!")
                                        finish()
                                    } else {
                                        toast("Sorry, username already taken")
                                        user_create_Nickname.setText("")
                                    }
                                }

                            }
                        })


                }

            } else {

                val editUser = EditUserMutation.builder()
                    .id(app.currentUser.id)
                    .name(user_create_name.text.toString())
                    .username(user_create_Nickname.text.toString())
                    .password(app.currentUser.password)
                    .build()


                app.client
                    .mutate(editUser)
                    .enqueue(object : ApolloCall.Callback<EditUserMutation.Data>() {
                        override fun onFailure(e: ApolloException) {
                            e.printStackTrace()
                        }

                        override fun onResponse(response: Response<EditUserMutation.Data>) {
                            app.currentUser.userName = response.data()!!.editUser()!!.username()
                            app.currentUser.name = response.data()!!.editUser()!!.name()

                            runOnUiThread {
                                    toast("User Updated!")
                                    finish()
                            }

                        }
                    })

            }
        }



    }

    // Inflating menu
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
