package User

import Admin.AdminMainActivity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.mobileassignment.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val db = Firebase.firestore

        val loginEmail = findViewById<EditText>(R.id.txtEmail)
        val loginPassword = findViewById<EditText>(R.id.txtPassword)
        val loginPasswordLayout = findViewById<TextInputLayout>(R.id.txtPasswordLayout)
        loginPasswordLayout.isPasswordVisibilityToggleEnabled = true
        val loginBtn = findViewById<Button>(R.id.loginBtn)

        val forgotPassText = findViewById<TextView>(R.id.forgotPassText)
        val registerText = findViewById<TextView>(R.id.registerText)
        forgotPassText.paintFlags = forgotPassText.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        registerText.paintFlags = registerText.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        forgotPassText.setOnClickListener{
            val intent = Intent(this, ResetPassword::class.java)
            startActivity(intent)
        }

        registerText.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener{
            loginPasswordLayout.isPasswordVisibilityToggleEnabled = true

            val email = loginEmail.text.toString()
            val password = loginPassword.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                if(email.isEmpty()){
                    loginEmail.error = "Enter your email address"
                }
                if(password.isEmpty()){
                    loginPassword.error = "Enter your password"
                    loginPasswordLayout.isPasswordVisibilityToggleEnabled = true
                }
                Toast.makeText(this, "Enter valid details", Toast.LENGTH_SHORT).show()
            }
            else if(!email.matches(emailPattern.toRegex())){
                loginEmail.error = "Enter valid email address"
                Toast.makeText(this, "Enter valid email address", Toast.LENGTH_SHORT).show()
            }
            else if(password.length < 8){
                loginPasswordLayout.isPasswordVisibilityToggleEnabled = true
                loginPassword.error = "Password must more than 8 characters"
                Toast.makeText(this, "Password must more than 8 characters", Toast.LENGTH_SHORT).show()
            }
            else{
                val providedEmail = email
                val providedPassword = password
                val sharedPrefs = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
                val editor = sharedPrefs.edit()
                editor.putString("email", providedEmail)
                editor.putString("password", providedPassword)
                editor.apply()

                db.collection("users")
                    .whereEqualTo("email", providedEmail)
                    .whereEqualTo("password", providedPassword)
                    .get()
                    .addOnSuccessListener { result ->
                        if (!result.isEmpty) {
                            // A document with the provided email and password was found
                            for (document in result) {
                                val userRole = document.getString("role")
                                if (userRole == "Admin") {
                                    // User is an admin, direct to admin activity
                                    val adminMainActivityIntent = Intent(this, AdminMainActivity::class.java)
                                    startActivity(adminMainActivityIntent)
                                } else if (userRole == "User") {
                                    // User is a customer, direct to main activity
                                    val mainIntent = Intent(this, UserMainActivity::class.java)
                                    startActivity(mainIntent)
                                } else {
                                    // Handle other roles as needed
                                }
                            }
                        } else {
                            // No matching record found, show an error or handle it as needed
                            Log.d(TAG, "No matching record found")
                            Toast.makeText(this, "No matching record found", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents.", exception)
                    }

            }
        }
    }
}