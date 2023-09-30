package User

import User.Donation.Donation
import User.Order.Order
import User.Payment.Payment
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.mobileassignment.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class UserEditProfile : AppCompatActivity() {

    private var selectedImageBitmap: Bitmap? = null
    private var PICK_IMAGE_REQUEST_CODE = 123
    private lateinit var profilePic: ImageView
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var sharedPrefs: SharedPreferences
    private var email: String = ""
    private var password: String = ""


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

        FirebaseApp.initializeApp(this)
        sharedPrefs = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        email = sharedPrefs.getString("email", "") ?: ""
        password = sharedPrefs.getString("password", "") ?: ""

        var editName = findViewById<EditText>(R.id.editName)
        var editContact = findViewById<EditText>(R.id.editContact)
        var editEmail = findViewById<EditText>(R.id.editEmail)
        profilePic = findViewById(R.id.profilePic)
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

        profilePic.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        }


        updateBtn.setOnClickListener {
            val newName = editName.text.toString()
            val newContact = editContact.text.toString()
            val newEmail = editEmail.text.toString() // Define newEmail here

            val userDetail = hashMapOf<String, Any>(
                "name" to newName,
                "contact" to newContact,
                "email" to newEmail
            )

            if (selectedImageBitmap != null) {
                val bitmap = selectedImageBitmap!!
                // Define the storage path for the image
                val imagePath = "profile_images/$newEmail.jpg"
                Log.d("Firebase Storage", "Uploading image to path: $imagePath")

                // Upload the image to Firebase Storage
                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef = storageRef.child(imagePath)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val imageData = baos.toByteArray()

                val uploadTask = imageRef.putBytes(imageData)

                uploadTask.addOnSuccessListener { _ ->
                    // Get the download URL of the uploaded image
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Add the download URL to the userDetail map
                        userDetail["profileImageUrl"] = uri.toString()

                        // Update user data with the profile image URL in Firestore
                        updateUserProfile(
                            email,
                            userDetail,
                            newEmail
                        ) // Pass newEmail as an argument
                    }.addOnFailureListener { e ->
                        Toast.makeText(
                            this,
                            "Failed to get image URL: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                // No image selected, update user data without the profile image URL
                updateUserProfile(email, userDetail, newEmail) // Pass newEmail as an argument
            }
        }
    }

    private fun updateUserProfile(email: String, userDetail: Map<String, Any>, newEmail: String) {
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
                                editor.putString("email", newEmail) // Update SharedPreferences with newEmail
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Get the selected image URI
            val imageUri: Uri? = data.data

            // Check if the URI is not null
            if (imageUri != null) {
                // Load the selected image into a Bitmap
                selectedImageBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))

                // Display the selected image in the ImageView
                profilePic.setImageBitmap(selectedImageBitmap)
            }
        }
    }
}