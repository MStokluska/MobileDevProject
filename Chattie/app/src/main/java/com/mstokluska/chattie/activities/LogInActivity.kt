package com.mstokluska.chattie.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mstokluska.chattie.R

import kotlinx.android.synthetic.main.log_in_activity.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

class LogInActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.log_in_activity)



        btnSignIn.setOnClickListener() {
            startActivityForResult<ChatsActivity>(0)
        }

        btnSignUp.setOnClickListener(){
            startActivityForResult<SignUpActivity>(0)
        }

    }
}
