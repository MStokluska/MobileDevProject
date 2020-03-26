package com.mstokluska.chattie.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mstokluska.chattie.R
import com.mstokluska.chattie.models.UserModel
import kotlinx.android.synthetic.main.card_user.view.*

class UsersAdapter constructor(private var users: List<UserModel>, private val listener: UsersListener) : RecyclerView.Adapter<UsersAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_user, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {

        val user = users[holder.adapterPosition]

        holder.bind(user, listener)
    }

    override fun getItemCount(): Int = users.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: UserModel, listener: UsersListener) {

            itemView.username.text = user.userName
            itemView.setOnClickListener { listener.onUserClick(user) }


        }
    }
}