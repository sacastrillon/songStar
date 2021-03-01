package com.example.woo.songstar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.woo.songstar.R
import com.example.woo.songstar.activities.RemoveUserActivity
import com.example.woo.songstar.models.User

class RemoveUserAdapter(context: Context, users: List<User>, parent: RemoveUserActivity): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var context: Context? = null
    private var users: List<User> = emptyList()
    var parent: RemoveUserActivity? = null

    init {
        this.context = context
        this.users = users
        this.parent = parent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(this.context).inflate(R.layout.adapter_remove_user, parent, false)
        return RemoveUserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.users.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val removeUserViewHolder = holder as RemoveUserViewHolder
        removeUserViewHolder.bind(this.users[position])
    }

    inner class RemoveUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView? = itemView.findViewById(R.id.tvRemoveUserName)
        private var userName: TextView? = itemView.findViewById(R.id.tvRemoveUserUserName)
        private var remove: ImageView? = itemView.findViewById(R.id.ivRemove)

        fun bind(user: User) {
            this.name?.text = user.name
            this.userName?.text = user.username
            this.remove?.setOnClickListener {
               parent?.removeUser(user)
            }
        }
    }
}