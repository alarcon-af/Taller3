package com.example.taller3

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val userName: TextView = itemView.findViewById(R.id.user_name)
    private val userLastName: TextView = itemView.findViewById(R.id.user_lastname)
    private val userImage: ImageView = itemView.findViewById(R.id.user_image)

    fun bind(user: User) {
        userName.text = user.nombre
        userLastName.text = user.apellido
        Glide.with(itemView.context).load(user.foto).placeholder(R.drawable.ic_launcher_foreground).into(userImage)
    }
}
