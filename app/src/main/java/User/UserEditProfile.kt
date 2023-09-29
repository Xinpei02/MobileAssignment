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
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.mobileassignment.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class UserEditProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_edit_profile)

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
                R.id.user_profile -> {
                    startActivity(Intent(applicationContext, UserProfile::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        val db = FirebaseFirestore.getInstance()
        val sharedPrefs = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        val email = sharedPrefs.getString("email", "")
        val password = sharedPrefs.getString("password", "")

        var editName = findViewById<EditText>(R.id.editName)
        var editContact = findViewById<EditText>(R.id.editContact)
        var editEmail = findViewById<EditText>(R.id.editEmail)
        val profilePic = findViewById<ImageView>(R.id.profilePic)
        var PICK_IMAGE_REQUEST_CODE = 123
        val updateBtn = findViewById<Button>(R.id.updateBtn)

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

                        editName.setText(userName)
                        editContact.setText(userContact)
                        editEmail.setText(userEmail)
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

        profilePic.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        }


        updateBtn.setOnClickListener {
            val newName = editName.text.toString()
            val newContact = editContact.text.toString()
            val newEmail = editEmail.text.toString()

            val userDetail: Map<String, Any> = hashMapOf(
                "name" to newName,
                "contact" to newContact,
                "email" to newEmail,
            )

            db.collection("users")
                .whereEqualTo("email", email) // Assuming 'email' is the unique identifier
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val documents = task.result?.documents

                        if (documents != null && documents.isNotEmpty()) {
                            val documentSnapshot = documents[0]
                            val documentID = documentSnapshot.id

                            db.collection("users")
                                .document(documentID)
                                .update(userDetail)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show()
                                    val sharedPrefs = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
                                    val editor = sharedPrefs.edit()
                                    editor.putString("email", newEmail)
                                    editor.apply()

                                    val intent = Intent(this, UserProfile::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Update failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this, "No user found with the provided email", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}