package com.mstokluska.chattie.activities


import MessageAdapter
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
import com.mstokluska.chattie.adapters.MessagesListener
import com.mstokluska.chattie.helpers.KeyboardClose
import com.mstokluska.chattie.main.MainApp
import com.mstokluska.chattie.models.ChatModel
import com.mstokluska.chattie.models.MessageModel
import com.mstokluska.chattie.models.UserModel
import com.mstokluska.graphql.*
import kotlinx.android.synthetic.main.activity_chat_room.*
import org.jetbrains.anko.*


class ChatRoom : AppCompatActivity(), AnkoLogger, MessagesListener {

    lateinit var app: MainApp
    var user = UserModel()
    var chat = ChatModel()
    var messages = ArrayList<MessageModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        app = application as MainApp

        setupRecyclerView()

        if (intent.hasExtra("user_logged_in")) {
            user = intent.extras.getParcelable<UserModel>("user_logged_in")
            chat = intent.extras.getParcelable<ChatModel>("chat_used")
        }

        val getMessagesPerChat = GetMessagesPerChatQuery.builder()
            .chatRoomId(chat.id)
            .build()

        app.client
            .query(getMessagesPerChat)
            .enqueue(object : ApolloCall.Callback<GetMessagesPerChatQuery.Data>() {
                override fun onFailure(e: ApolloException) {

                }

                override fun onResponse(response: Response<GetMessagesPerChatQuery.Data>) {
                    runOnUiThread {
                        for (message in response.data()!!.messagesForChatRoom) {
                            val messageId = message.id()
                            val messageCreator = message.mcreator() + " : "
                            val chatRoomId = "dummy"
                            val messageContent = message.content()

                            messages.add(
                                MessageModel(
                                    messageId,
                                    messageCreator,
                                    chatRoomId,
                                    messageContent
                                )
                            )
                            messageRecyclerView.adapter?.notifyDataSetChanged()
                        }
                    }
                }
            })


        val messageAddedSub = MessageAddedSubscription.builder().build()

        app.client
            .subscribe(messageAddedSub)
            .execute(object : ApolloSubscriptionCall.Callback<MessageAddedSubscription.Data> {
                override fun onConnected() {
                    info("SUB CONNECTED")
                }

                override fun onTerminated() {
                    info("SUB TERMINATED")
                }

                override fun onCompleted() {
                    info("SUB COMPLETED")
                }

                override fun onFailure(e: ApolloException) {
                    info("SUB FAILURE")
                }

                override fun onResponse(response: Response<MessageAddedSubscription.Data>) {
                    info("SUB SUCCESS")
                    val messageData = response.data()!!.messageAdded()
                    val messageId = messageData.id()
                    val messageContent = messageData.content()
                    val messageCreator = messageData.mcreator() + " : "
                    val messageChatId = messageData.mchat()


                    val messageToAdd =
                        MessageModel(messageId, messageCreator, messageChatId, messageContent)

                    runOnUiThread {

                        if (chat.id == messageChatId) {
                            if (user.userName != messageData.mcreator()) {
                                messages.add(messageToAdd)
                            }
                            messageRecyclerView.adapter?.notifyDataSetChanged()
                        }
                    }
                }

            })


        image_send.setOnClickListener() {

            val createMessage = AddMessageMutation.builder()
                .mcreator(user.userName)
                .mchat(chat.id)
                .content(sendMessage.text.toString())
                .build()

            app.client
                .mutate(createMessage)
                .enqueue(object : ApolloCall.Callback<AddMessageMutation.Data>() {
                    override fun onFailure(e: ApolloException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(response: Response<AddMessageMutation.Data>) {

                        val messageData = response.data()!!.createMessage()
                        val messageId = messageData.id()
                        val messageCreator = messageData.mcreator() + " : "
                        val messageChatId = messageData.mchat()
                        val messageContent = messageData.content()

                        val messageToAdd =
                            MessageModel(messageId, messageCreator, messageChatId, messageContent)

                        runOnUiThread {
                            messages.add(messageToAdd)

                            sendMessage.text!!.clear()
                            messageRecyclerView.adapter?.notifyDataSetChanged()

                            KeyboardClose.hideSoftKeyBoard(this@ChatRoom, it.rootView)
                        }
                    }
                })
        }

        toolbarChatRoom.title = title
        setSupportActionBar(toolbarChatRoom)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        messageRecyclerView.layoutManager = layoutManager
        messageRecyclerView.adapter = MessageAdapter(messages, this)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_room_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                startActivityForResult(
                    intentFor<ChatsActivity>().putExtra(
                        "user_logged_in",
                        user
                    ), 0
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMessageClick(message: MessageModel) {
        // edit message ?
    }

}
