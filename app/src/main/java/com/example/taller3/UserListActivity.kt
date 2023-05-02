package com.example.taller3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class UserListActivity : AppCompatActivity() {

    private lateinit var userList: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var users: MutableList<User>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        userList = findViewById(R.id.user_list_recyclerview)
        userList.layoutManager = LinearLayoutManager(this)

        users = mutableListOf()
        userAdapter = UserAdapter(users)
        userList.adapter = userAdapter

        database = FirebaseDatabase.getInstance().getReference("users")

        getUsers()
    }

    private fun getUsers() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    user?.let { users.add(it) }
                }
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })
    }
}
