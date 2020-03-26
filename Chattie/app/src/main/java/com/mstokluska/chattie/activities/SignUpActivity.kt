package com.mstokluska.chattie.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.mstokluska.chattie.R
import com.mstokluska.chattie.main.MainApp
import com.mstokluska.chattie.models.UserModel
import com.mstokluska.graphql.CreateUserMutation
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.*

class SignUpActivity : AppCompatActivity(), AnkoLogger {

    lateinit var app: MainApp
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        app = application as MainApp

        btnCreateUser.setOnClickListener() {

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

        }


        // menu bar
        toolbarCreateUser.title = title
        setSupportActionBar(toolbarCreateUser)
    }

    // Inflating menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
