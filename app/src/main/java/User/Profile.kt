package User

import User.Donation.Donation
import User.Order.Order
import User.Payment.Payment
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.mobileassignment.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.user_navigation)
        bottomNavigationView.selectedItemId = R.id.user_profile

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.user_home -> {
                    startActivity(Intent(applicationContext, UserMainActivity::class.java))
                    finish()
                    true
                }
                R.id.user_order -> {
                    startActivity(Intent(applicationContext, Order::class.java))
                    finish()
                    true
                }
                R.id.user_payment -> {
                    startActivity(Intent(applicationContext, Payment::class.java))
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
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        val sharedPrefs = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        val email = sharedPrefs.getString("email", "")
        val password = sharedPrefs.getString("password", "")

        var showName = findViewById<TextView>(R.id.showName)
        var showEmail = findViewById<TextView>(R.id.showEmail)
        var showContact = findViewById<TextView>(R.id.showContact)
        val logoutBtn = findViewById<Button>(R.id.logoutBtn)

        db.collection("users")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    // A document with the provided email and password was found
                    for (document in result) {
                        val userName = document.getString("name")
                        val userContact = document.getString("contact")
                        val userEmail = document.getString("email")

                        showName.text = userName
                        showContact.text = userContact
                        showEmail.text = userEmail
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

        logoutBtn.setOnClickListener{
            val editor = sharedPrefs.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}