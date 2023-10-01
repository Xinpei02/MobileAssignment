package User

import User.Donation.Donation
import User.Payment.Payment
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.mobileassignment.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class UserProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.user_navigation)
        bottomNavigationView.selectedItemId = R.id.user_profile

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.user_home -> {
                    startActivity(Intent(applicationContext, UserMainActivity::class.java))
                    finish()
                    true
                }

                R.id.user_menu -> {
                    startActivity(Intent(applicationContext, Menu::class.java))
                    finish()
                    true
                }

                R.id.user_cart -> {
                    startActivity(Intent(applicationContext, user_cart::class.java))
                    finish()
                    true
                }

                R.id.user_donation -> {
                    startActivity(Intent(applicationContext, Donation::class.java))
                    finish()
                    true
                }

                R.id.user_profile -> true

                else -> false
            }
        }

        val db = Firebase.firestore

        val sharedPrefs = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        val email = sharedPrefs.getString("email", "")
        val password = sharedPrefs.getString("password", "")

        var showName = findViewById<TextView>(R.id.showName)
        var showUserName = findViewById<TextView>(R.id.showUserName)
        var showEmail = findViewById<TextView>(R.id.showEmail)
        var showContact = findViewById<TextView>(R.id.showContact)
        var showPassword = findViewById<TextView>(R.id.showPassword)
        val userProfilePic = findViewById<ImageView>(R.id.userProfilePic)
        val editBtn = findViewById<Button>(R.id.editBtn)
        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        val paymentBtn = findViewById<Button>(R.id.paymentBtn)

        paymentBtn.setOnClickListener{
            val intent = Intent(this, Payment::class.java)
            startActivity(intent)
        }

        db.collection("users")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    // A document with the provided email and password was found
                    for (document in result) {
                        val userName = document.getString("name")
                        val profileUserName = document.getString("name")
                        val userContact = document.getString("contact")
                        val userEmail = document.getString("email")
                        val userPassword = document.getString("password")
                        val profileImageUrl = document.getString("profileImageUrl")

                        showName.text = userName
                        showUserName.text = profileUserName
                        showContact.text = userContact
                        showEmail.text = userEmail
                        showPassword.text = userPassword
                        if (!profileImageUrl.isNullOrEmpty()) {
                            Picasso.get().load(profileImageUrl).into(userProfilePic)
                        }

                    }
                } else {
                    // No matching record found, show an error or handle it as needed
                    Log.d(ContentValues.TAG, "No matching record found")
                    Toast.makeText(this, "No matching record found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

        editBtn.setOnClickListener{
            val intent = Intent(this, UserEditProfile::class.java)
            startActivity(intent)
        }

        logoutBtn.setOnClickListener{
            val editor = sharedPrefs.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}