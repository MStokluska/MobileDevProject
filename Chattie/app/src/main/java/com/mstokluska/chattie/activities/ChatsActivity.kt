package com.mstokluska.chattie.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.mstokluska.chattie.R
import com.mstokluska.chattie.main.MainApp
import com.mstokluska.chattie.models.user.UserModel
import kotlinx.android.synthetic.main.activity_chats.*

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

class ChatsActivity : AppCompatActivity(), AnkoLogger {

    lateinit var app: MainApp
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)
        app = application as MainApp

        if (intent.hasExtra("user_logged_in")) {
            user = intent.extras.getParcelable<UserModel>("user_logged_in")
            toast("username is ${user.name}")
        }




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
