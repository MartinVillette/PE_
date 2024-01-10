package com.example.pe41

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NewGroupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var groupName: EditText
    private lateinit var searchBar: EditText
    private lateinit var createGroupButton: Button
    private var allUserList: ArrayList<User> = ArrayList()
    private var userList: ArrayList<User> = ArrayList()
    private var userToAddList: ArrayList<User> = ArrayList()
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var adapter: GroupUserAdapter
    private lateinit var newGroup: Group
    private lateinit var membersNumberText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_group)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        searchBar = findViewById(R.id.edit_research)
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateUserList(searchBar.text.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
        groupName = findViewById(R.id.edit_group_name)

        newGroup = Group()

        adapter = GroupUserAdapter(this@NewGroupActivity, newGroup, userList, userToAddList) { number ->
            //membersNumberText.text = number.toString()
        }
        userRecyclerView = findViewById(R.id.group_recycler_view)
        userRecyclerView.layoutManager = LinearLayoutManager(this@NewGroupActivity)
        userRecyclerView.adapter = adapter

        createGroupButton = findViewById(R.id.button_create)
        createGroupButton.setOnClickListener {
            saveNewGroup(newGroup)
        }

        initiateUserList()
    }

    private fun initiateUserList(){
        val userRef = database.getReference("User")
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allUserList.clear()
                userList.clear()
                for (postSnapshot in snapshot.children) {
                    val userId = postSnapshot.key
                    if (userId != auth.currentUser?.uid) {
                        val currentUserRef = userRef.child(userId!!)
                        currentUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(userSnapshot: DataSnapshot) {
                                userSnapshot.getValue(User::class.java)?.let {
                                    allUserList.add(it)
                                    userList.add(it)
                                }
                                if (allUserList.size == snapshot.children.count()){
                                    adapter.notifyDataSetChanged()
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun updateUserList(research: String = "") {
        userList.clear()
        for (user in allUserList){
            val username = user.username!!.lowercase()
            val name = user.name!!.lowercase()
            val lastName = user.lastName!!.lowercase()

            if (username.startsWith(research.lowercase()) ||
                name.startsWith(research.lowercase()) ||
                lastName.startsWith(research.lowercase())
            ) {
                userList.add(user)
            }
        }
        adapter.notifyDataSetChanged()
    }


    private fun saveNewGroup(group: Group) {
        group.groupName = groupName.text.toString()
        group.teacherId = auth.currentUser?.uid

        for (user in userToAddList) {
            group.addUserToGroup(user)
        }

        val newGroupRef = database.getReference("Group").push()
        group.groupId = newGroupRef.key ?: ""
        newGroupRef.setValue(group)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val l = arrayListOf(group.teacherId)
                    for (userId in group.usersIds + l){
                        val userRef = database.getReference("User/$userId")
                        userRef.addListenerForSingleValueEvent(object: ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()){
                                    val currentUser = snapshot.getValue(User::class.java)!!
                                    currentUser.addGroup(group.groupId!!)
                                    val updateData = HashMap<String, Any>()
                                    updateData["groupIds"] = currentUser.groupIds
                                    userRef.updateChildren(updateData)
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }
                    val intent =  Intent(this@NewGroupActivity, GroupActivity::class.java)
                    intent.putExtra("groupId", group.groupId)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }
}