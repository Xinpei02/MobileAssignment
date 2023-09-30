package Admin

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.mobileassignment.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddAdmin : AppCompatActivity() {

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_admin)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.admin_navigation)

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

                R.id.admin_profile -> {
                    startActivity(Intent(applicationContext, AdminProfile::class.java))
                    finish()
                    true
                }

                else -> false
            }
        }

        val db = Firebase.firestore
        val userName = findViewById<TextView>(R.id.txtName)
        val userContact = findViewById<TextView>(R.id.txtContact)
        val userEmail = findViewById<TextView>(R.id.txtEmail)
        val userPassword = findViewById<TextView>(R.id.txtPassword)
        val txtPasswordLayout = findViewById<TextInputLayout>(R.id.txtPasswordLayout)
        val addBtn = findViewById<Button>(R.id.addBtn)

        addBtn.setOnClickListener {
            val name = userName.text.toString()
            val contact = userContact.text.toString()
            val email = userEmail.text.toString()
            val password = userPassword.text.toString()

            txtPasswordLayout.isPasswordVisibilityToggleEnabled = true

            if (name.isEmpty() || contact.isEmpty() || email.isEmpty() || password.isEmpty()) {
                if (name.isEmpty()) {
                    userName.error = "Name is required"
                }
                if (contact.isEmpty()) {
                    userContact.error = "Contact is required"
                }
                if (email.isEmpty()) {
                    userEmail.error = "Email is required"
                }
                if (password.isEmpty()) {
                    txtPasswordLayout.isPasswordVisibilityToggleEnabled = false
                    userPassword.error = "Password is required"
                }
                Toast.makeText(this, "Enter valid details", Toast.LENGTH_SHORT).show()
            } else if (!email.matches(emailPattern.toRegex())) {
                userEmail.error = "Enter valid email address"
                Toast.makeText(this, "Enter valid email address", Toast.LENGTH_SHORT).show()
            } else if (password.length < 8) {
                txtPasswordLayout.isPasswordVisibilityToggleEnabled = false
                userPassword.error = "Password must more than 8 characters"
                Toast.makeText(this, "Password must more than 8 characters", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val user = hashMapOf(
                    "name" to name,
                    "contact" to contact,
                    "email" to email,
                    "password" to password,
                    "role" to "Admin",
                )

                val usersCollection = db.collection("users")
                val query = usersCollection.whereEqualTo("email", email)

                query.get()
                    .addOnSuccessListener { querySnapshot ->
                        if (querySnapshot.isEmpty) {
                            // Email is not found, so it's not registered
                            usersCollection.add(user)
                                .addOnSuccessListener { documentReference ->
                                    Log.d(
                                        ContentValues.TAG,
                                        "DocumentSnapshot added with ID: ${documentReference.id}"
                                    )
                                    val intent = Intent(this, AdminMainActivity::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    Log.w(ContentValues.TAG, "Error adding document", e)
                                }
                        } else {
                            // Email is already registered
                            // You can show an error message or take appropriate action here
                            Toast.makeText(this, "Email is already registered.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error checking for existing email", e)
                    }
            }
        }
    }
}