package com.mstokluska.chattie.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.mstokluska.chattie.R
import com.mstokluska.chattie.main.MainApp
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.toast

class SignUpActivity : AppCompatActivity() {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        app = application as MainApp


        btnCreateUser.setOnClickListener() {
            toast("user created!")
        }

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
