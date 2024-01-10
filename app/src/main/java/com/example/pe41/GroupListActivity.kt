package com.example.pe41

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class GroupListActivity : AppCompatActivity() {

    private lateinit var newGroupButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var groups: ArrayList<Group> = ArrayList()
    private lateinit var groupRecyclerView: RecyclerView
    private lateinit var adapter: GroupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_list)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        adapter = GroupAdapter(this@GroupListActivity, groups)
        groupRecyclerView = findViewById(R.id.group_recycler_view)
        groupRecyclerView.layoutManager = LinearLayoutManager(this@GroupListActivity)
        groupRecyclerView.adapter = adapter

        newGroupButton = findViewById(R.id.button_new_group)

        val userRef = database.getReference("User/${auth.currentUser?.uid}")
        userRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    snapshot.getValue(User::class.java)?.let{user ->
                        if (user.teacher){
                            newGroupButton.setOnClickListener {
                                val intent =  Intent(this@GroupListActivity, NewGroupActivity::class.java)
                                startActivity(intent)
                            }
                        } else {
                            newGroupButton.visibility = View.GONE
                        }
                        for (groupId in user.groupIds){
                            val groupRef = database.getReference("Group/$groupId")
                            groupRef.addListenerForSingleValueEvent(object: ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    snapshot.getValue(Group::class.java)?.let{group ->
                                        groups.add(group)
                                        if (groups.size == user.groupIds.size){
                                            adapter.notifyDataSetChanged()
                                        }
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {}
                            })
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}