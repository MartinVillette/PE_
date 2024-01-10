package com.example.pe41

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GroupActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var group: Group
    private lateinit var groupNameText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        database = FirebaseDatabase.getInstance()
        groupNameText = findViewById(R.id.text_group_name)

        val groupId = intent.getStringExtra("groupId")
        if (groupId != null) {
            val groupRef = database.getReference("Group/$groupId")
            groupRef.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue(Group::class.java)?.let{
                        group = it
                        run()
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        } else {
            val intent = Intent(this, GroupListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun run(){
        groupNameText.text = group.groupName
    }
}