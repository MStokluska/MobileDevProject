package com.mstokluska.chattie.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.mstokluska.chattie.R
import kotlinx.android.synthetic.main.activity_chats.*

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivityForResult

class ChatsActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

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
