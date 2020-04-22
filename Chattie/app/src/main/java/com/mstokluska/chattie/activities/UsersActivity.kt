package com.mstokluska.chattie.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.mstokluska.chattie.R
import com.mstokluska.chattie.adapters.UsersAdapter
import com.mstokluska.chattie.adapters.UsersListener
import com.mstokluska.chattie.main.MainApp
import com.mstokluska.chattie.models.UserMemStore
import com.mstokluska.chattie.models.UserModel
import com.mstokluska.graphql.CreateChatMutation
import com.mstokluska.graphql.GetUsersQuery
import kotlinx.android.synthetic.main.activity_chats.*
import kotlinx.android.synthetic.main.activity_users.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class UsersActivity : AppCompatActivity(), UsersListener, AnkoLogger {

    lateinit var app: MainApp
    var usersArray = ArrayList<UserModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        app = application as MainApp

        setupRecyclerView()

        val getUsersQuery = GetUsersQuery.builder().build()

        app.client
            .query(getUsersQuery)
            .enqueue(object : ApolloCall.Callback<GetUsersQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    info(e)
                }

                override fun onResponse(response: Response<GetUsersQuery.Data>) {
                    runOnUiThread {
                        val initialUserArray = response.data()!!.allUsers
                        initialUserArray.forEach {
                            if (it.username() != app.users.currentUser.userName) {
                                usersArray.add(UserModel(it.id(), it.username(), it.name()))
                            }
                        }
                        usersRecyclerView.adapter?.notifyDataSetChanged()
                    }
                }
            })

        toolbarChatWithUser.title = getString(R.string.users)
        setSupportActionBar(toolbarChatWithUser)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

    override fun onUserClick(user: UserModel) {

        val createChatMutation = CreateChatMutation.builder()
            .creator(app.users.currentUser.userName)
            .recipent(user.userName)
            .build()

        app.client
            .mutate(createChatMutation)
            .enqueue(object : ApolloCall.Callback<CreateChatMutation.Data>() {
                override fun onFailure(e: ApolloException) {
                    e.printStackTrace()
                }

                override fun onResponse(response: Response<CreateChatMutation.Data>) {

                    runOnUiThread {
                        finish()
                    }

                }
            })
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        usersRecyclerView.layoutManager = layoutManager
        usersRecyclerView.adapter = UsersAdapter(usersArray, this)
    }
}
