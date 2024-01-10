package com.example.pe41

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseAuthenticatedActivity() {

    private lateinit var groupsButton: Button
    private lateinit var disconnectButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        groupsButton = findViewById(R.id.button_groups)
        groupsButton.setOnClickListener {
            val intent =  Intent(this@MainActivity, GroupListActivity::class.java)
            startActivity(intent)
        }

        disconnectButton = findViewById(R.id.button_disconnect)
        disconnectButton.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            val intent =  Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}