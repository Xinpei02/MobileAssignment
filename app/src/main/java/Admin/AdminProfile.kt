package Admin

import User.Login
import User.UserEditProfile
import User.UserProfile
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_profile)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.admin_navigation)
        bottomNavigationView.selectedItemId = R.id.admin_profile

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.admin_home -> {
                    startActivity(Intent(applicationContext, AdminMainActivity::class.java))
                    finish()
                    true
                }
                R.id.admin_order -> {
                    startActivity(Intent(applicationContext, AdminOrder::class.java))
                    finish()
                    true
                }

                R.id.admin_payment -> {
                    startActivity(Intent(applicationContext, AdminPayment::class.java))
                    finish()
                    true
                }

                R.id.admin_donation -> {
                    startActivity(Intent(applicationContext, AdminDonation::class.java))
                    finish()
                    true
                }

                R.id.admin_profile -> true

                else -> false
            }
        }

        val db = Firebase.firestore

        val sharedPrefs = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        val email = sharedPrefs.getString("email", "")
        val password = sharedPrefs.getString("password", "")

        var showName = findViewById<TextView>(R.id.showName)
        var showEmail = findViewById<TextView>(R.id.showEmail)
        var showContact = findViewById<TextView>(R.id.showContact)
        val editBtn = findViewById<Button>(R.id.editBtn)
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

        editBtn.setOnClickListener{
            val intent = Intent(this, AdminEditProfile::class.java)
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