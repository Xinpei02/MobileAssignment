package com.example.mobileassignment

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val db = Firebase.firestore

        val registerName = findViewById<EditText>(R.id.txtName)
        val registerContact = findViewById<EditText>(R.id.txtContact)
        val registerEmail = findViewById<EditText>(R.id.txtEmail)
        val registerPassword = findViewById<EditText>(R.id.txtPassword)
        val registerConPass = findViewById<EditText>(R.id.txtConPassword)
        val txtPasswordLayout = findViewById<TextInputLayout>(R.id.txtPasswordLayout)
        val txtConPasswordLayout = findViewById<TextInputLayout>(R.id.txtConPasswordLayout)
        val registerBtn = findViewById<Button>(R.id.registerBtn)

        val loginText = findViewById<TextView>(R.id.loginText)

        loginText.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        registerBtn.setOnClickListener{
            val name = registerName.text.toString()
            val contact = registerContact.text.toString()
            val email = registerEmail.text.toString()
            val password = registerPassword.text.toString()
            val conPass = registerConPass.text.toString()

            txtPasswordLayout.isPasswordVisibilityToggleEnabled = true
            txtConPasswordLayout.isPasswordVisibilityToggleEnabled = true

            if(name.isEmpty() || contact.isEmpty() ||email.isEmpty() || password.isEmpty() || conPass.isEmpty()){
                if(name.isEmpty()){
                    registerName.error = "Enter your name"
                }
                if(contact.isEmpty()){
                    registerContact.error = "Enter your contact"
                }
                if(email.isEmpty()){
                    registerEmail.error = "Enter your email address"
                }
                if(password.isEmpty()){
                    txtPasswordLayout.isPasswordVisibilityToggleEnabled = false
                    registerPassword.error = "Enter your password"
                }
                if(conPass.isEmpty()){
                    txtConPasswordLayout.isPasswordVisibilityToggleEnabled = false
                    registerConPass.error = "Re enter your password"
                }
                Toast.makeText(this, "Enter valid details", Toast.LENGTH_SHORT).show()

            }
            else if(!email.matches(emailPattern.toRegex())){
                registerEmail.error = "Enter valid email address"
                Toast.makeText(this, "Enter valid email address", Toast.LENGTH_SHORT).show()
            }
            else if(password.length < 8){
                txtPasswordLayout.isPasswordVisibilityToggleEnabled = false
                registerPassword.error = "Password must more than 8 characters"
                Toast.makeText(this, "Password must more than 8 characters", Toast.LENGTH_SHORT).show()
            }
            else if(password != conPass){
                txtConPasswordLayout.isPasswordVisibilityToggleEnabled = false
                registerConPass.error = "Password not matched, try again"
                Toast.makeText(this, "Password not matched, try again", Toast.LENGTH_SHORT).show()
            }
            else{
                val user = hashMapOf(
                    "name" to name,
                    "contact" to contact,
                    "email" to email,
                    "password" to password,
                    "role" to "admin",
                )

                val usersCollection = db.collection("users")
                val query = usersCollection.whereEqualTo("email", email)

                query.get()
                    .addOnSuccessListener { querySnapshot ->
                        if (querySnapshot.isEmpty) {
                            // Email is not found, so it's not registered
                            usersCollection.add(user)
                                .addOnSuccessListener { documentReference ->
                                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                                    val intent = Intent(this, Login::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error adding document", e)
                                }
                        } else {
                            // Email is already registered
                            // You can show an error message or take appropriate action here
                            Toast.makeText(this, "Email is already registered.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error checking for existing email", e)
                    }






            }
        }
    }
}