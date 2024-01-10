package com.example.pe41

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var editId: EditText
    private lateinit var editName: EditText
    private lateinit var editLastName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var editConfirmPassword: EditText
    private lateinit var checkboxTeacher: CheckBox
    private lateinit var buttonSignUp: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        editId = findViewById(R.id.edit_id)
        editName = findViewById(R.id.edit_name)
        editLastName = findViewById(R.id.edit_last_name)
        editEmail = findViewById(R.id.edit_email)
        editPassword = findViewById(R.id.edit_password)
        editConfirmPassword = findViewById(R.id.edit_confirm_password)
        checkboxTeacher = findViewById(R.id.checkbox_teacher)
        buttonSignUp = findViewById(R.id.button_signup)

        buttonSignUp.setOnClickListener {
            var id = editId.text.toString()
            var name = editName.text.toString()
            var lastName = editLastName.text.toString()
            var email = editEmail.text.toString()
            var password = editPassword.text.toString()
            var confirmPassword = editConfirmPassword.text.toString()
            var teacher = checkboxTeacher.isChecked

            if (password == confirmPassword) {
                signup(id, name, lastName, email, password, teacher)
            }
        }
    }

    private fun signup(id:String, name:String, lastName: String, email:String, password:String, teacher:Boolean){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    addUserToData(id, name, lastName, email, auth.currentUser?.uid!!, teacher)
                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@SignUpActivity, "Problème", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToData(id: String, name:String, lastName:String, email: String, userId: String, teacher:Boolean){
        database = FirebaseDatabase.getInstance().getReference("User")
        database.child(userId).setValue(User(userId,id,name,lastName,email,teacher))
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Data inserted success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }
}