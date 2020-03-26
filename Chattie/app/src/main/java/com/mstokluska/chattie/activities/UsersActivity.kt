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
import com.mstokluska.chattie.models.UserModel
import com.mstokluska.graphql.GetUsersQuery
import kotlinx.android.synthetic.main.activity_chats.*
import kotlinx.android.synthetic.main.activity_users.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class UsersActivity : AppCompatActivity(), UsersListener, AnkoLogger {

    lateinit var app: MainApp
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        app = application as MainApp

       setupRecyclerView()

        if (intent.hasExtra("user_logged_in")) {
            user = intent.extras.getParcelable<UserModel>("user_logged_in")
        }
        
        val getUsersQuery = GetUsersQuery.builder().build()

        app.client
            .query(getUsersQuery)
            .enqueue(object : ApolloCall.Callback<GetUsersQuery.Data>() {
                override fun onFailure(e: ApolloException) {

                }

                override fun onResponse(response: Response<GetUsersQuery.Data>) {
                    runOnUiThread {

                            val initialUserArray = response.data()!!.allUsers

                            initialUserArray.forEach {
                                if(it.username() != user.userName) {
                                    app.users.add(UserModel(it.id(), it.username(), it.name()))
                                }
                            }
                        usersRecyclerView.adapter?.notifyDataSetChanged()
                        }
                    }
            })

        toolbarChatWithUser.title = title
        setSupportActionBar(toolbarChatWithUser)
    }

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

    override fun onUserClick(user: UserModel) {
       toast("User clicked " + user.name)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        usersRecyclerView.layoutManager = layoutManager
        usersRecyclerView.adapter = UsersAdapter(app.users, this)
    }

}
