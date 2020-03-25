package com.mstokluska.chattie.activities

import ChatAdapter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.mstokluska.chattie.R
import com.mstokluska.chattie.main.MainApp
import com.mstokluska.chattie.models.ChatModel
import com.mstokluska.chattie.models.UserModel
import com.mstokluska.graphql.GetChatsForUserQuery
import kotlinx.android.synthetic.main.activity_chats.*

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import java.util.function.Consumer

class ChatsActivity : AppCompatActivity(), AnkoLogger {

    lateinit var app: MainApp
    var user = UserModel()
    val chats = ArrayList<ChatModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = ChatAdapter(chats)

        if (intent.hasExtra("user_logged_in")) {
            user = intent.extras.getParcelable<UserModel>("user_logged_in")
        }

        val getChatsQuery = GetChatsForUserQuery.builder()
            .userId(user.id)
            .build()

        app.client
            .query(getChatsQuery)
            .enqueue(object: ApolloCall.Callback<GetChatsForUserQuery.Data>(){
                override fun onFailure(e: ApolloException) {

                }

                override fun onResponse(response: Response<GetChatsForUserQuery.Data>) {
                    runOnUiThread{
                        for ( name in response.data()!!.chatForUser){
                            chats.add(ChatModel(name.recipent().name()))
                        }
                    }
                }
            })

        toolbarChats.title = title
        setSupportActionBar(toolbarChats)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chats_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add_chat -> {
                startActivityForResult<UsersActivity>(0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
