package com.example.opsc7312poe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class UserAdapter(
    private val users: List<User>,
    private val onUserClicked: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageview_new_message)
        val usernameTextView: TextView = view.findViewById(R.id.username_textview_newmessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_row_new_message, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.usernameTextView.text = user.username
        Picasso.get().load(user.profilePicUrl).into(holder.imageView)

        holder.itemView.setOnClickListener {
            onUserClicked(user)
        }
    }

    override fun getItemCount() = users.size
}