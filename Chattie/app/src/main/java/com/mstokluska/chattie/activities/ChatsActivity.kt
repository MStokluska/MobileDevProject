package com.mstokluska.chattie.activities

import ChatAdapter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloSubscriptionCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.mstokluska.chattie.R
import com.mstokluska.chattie.adapters.ChatListener
import com.mstokluska.chattie.main.MainApp
import com.mstokluska.chattie.models.ChatModel
import com.mstokluska.chattie.models.UserModel
import com.mstokluska.graphql.ChatAddedSubscription
import com.mstokluska.graphql.ChatDeletedSubscription
import com.mstokluska.graphql.DeleteChatMutation
import com.mstokluska.graphql.GetChatsForUserQuery
import kotlinx.android.synthetic.main.activity_chats.*
import kotlinx.android.synthetic.main.card_chat.*
import org.jetbrains.anko.*


class ChatsActivity : AppCompatActivity(), AnkoLogger, ChatListener {


    lateinit var app: MainApp
    var user = UserModel()
    var chats = ArrayList<ChatModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)
        app = application as MainApp

        setupRecyclerView()


        if (intent.hasExtra("user_logged_in")) {
            user = intent.extras.getParcelable<UserModel>("user_logged_in")
        }

        val getChatsQuery = GetChatsForUserQuery.builder()
            .userId(user.id)
            .build()


        app.client
            .query(getChatsQuery)
            .enqueue(object : ApolloCall.Callback<GetChatsForUserQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    runOnUiThread {

                    }
                }

                override fun onResponse(response: Response<GetChatsForUserQuery.Data>) {

                    val chatsArray = response.data()!!.chatForUser

                    runOnUiThread {
                        for (chat in chatsArray) {
                            if(chat.recipent()!!.username() == user.userName) {
                                chats.add(
                                    ChatModel(
                                        chat.id(),
                                        chat.recipent()!!.username(),
                                        chat.creator()!!.username()
                                    )
                                )
                            } else {
                                chats.add(
                                    ChatModel(
                                        chat.id(),
                                        chat.creator()!!.username(),
                                        chat.recipent()!!.username()
                                    )
                                )
                            }


                        }
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                }
            })

        val chatAddedSub = ChatAddedSubscription.builder().build()


//        app.client
//            .subscribe(chatAddedSub)
//            .execute(object : ApolloSubscriptionCall.Callback<ChatAddedSubscription.Data> {
//                override fun onConnected() {
//                    info("SUB CONNECTED")
//                }
//
//                override fun onTerminated() {
//                    info("SUB TERMINATED")
//                }
//
//                override fun onCompleted() {
//                    info("SUB COMPLETED")
//                }
//
//                override fun onFailure(e: ApolloException) {
//                    info("SUB FAILURE")
//                }
//
//                override fun onResponse(response: Response<ChatAddedSubscription.Data>) {
//                    info("SUB SUCCESS")
//                    runOnUiThread {
//                        val chatId = response.data()!!.chatAdded().id()
//                        val chatCreatorUsername =
//                            response.data()!!.chatAdded().creator()!!.username()
//                        val chatRecipentUsername =
//                            response.data()!!.chatAdded().recipent()!!.username()
//
//
//                        if (user.userName == chatCreatorUsername || user.userName == chatRecipentUsername) {
//
//                            if(user.userName == chatRecipentUsername){
//                                chats.add(
//                                    ChatModel(
//                                        chatId,
//                                        chatRecipentUsername,
//                                        chatCreatorUsername
//                                    )
//                                )
//
//                            } else {
//                                chats.add(
//                                    ChatModel(
//                                        chatId,
//                                        chatCreatorUsername,
//                                        chatRecipentUsername
//                                    )
//                                )
//                            }
//                            recyclerView.adapter?.notifyDataSetChanged()
//                        }
//                    }
//                }
//
//            })
//
//
//        val chatDeletedSub = ChatDeletedSubscription.builder().build()
//
//        app.client
//            .subscribe(chatDeletedSub)
//            .execute(object : ApolloSubscriptionCall.Callback<ChatDeletedSubscription.Data> {
//                override fun onConnected() {
//                    info("SUB CONNECTED")
//                }
//
//                override fun onTerminated() {
//                    info("SUB TERMINATED")
//                }
//
//                override fun onCompleted() {
//                    info("SUB COMPLETED")
//                }
//
//                override fun onFailure(e: ApolloException) {
//                    info("SUB FAILURE")
//                }
//
//                override fun onResponse(response: Response<ChatDeletedSubscription.Data>) {
//                    info("SUB SUCCESS")
//                    runOnUiThread {
//                        val chatId = response.data()?.chatDeleted()
//                        val indexOfDeleted = chats.indexOfFirst {
//                            it.id == chatId
//                        }
//                        if( indexOfDeleted != -1) {
//                            chats.removeAt(indexOfDeleted)
//                            recyclerView.adapter?.notifyDataSetChanged()
//                        }
//                    }
//                }
//
//            })

        toolbarChats.title = title
        setSupportActionBar(toolbarChats)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = ChatAdapter(chats, this)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chats_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add_chat -> {

                startActivityForResult(
                    intentFor<UsersActivity>().putExtra(
                        "user_logged_in",
                        user
                    ), 0
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDeleteClick(chat: ChatModel) {
        val deleteChatMutation = DeleteChatMutation.builder()
            .chatId(chat.id)
            .build()

        app.client
            .mutate(deleteChatMutation)
            .enqueue(object : ApolloCall.Callback<DeleteChatMutation.Data>() {
                override fun onFailure(e: ApolloException) {
                    e.printStackTrace()
                }

                override fun onResponse(response: Response<DeleteChatMutation.Data>) {
                    runOnUiThread {
                        chats.remove(chat)
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                }
            })
    }

    override fun onChatClick(chat: ChatModel) {
        startActivityForResult(
            intentFor<ChatRoom>().putExtra(
                "user_logged_in",
                 user
            ).putExtra(
                "chat_used",
                chat
            ), 0
        )
    }

}
