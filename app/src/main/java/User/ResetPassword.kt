package User

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mobileassignment.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ResetPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val db = Firebase.firestore

        val forgotEmail = findViewById<EditText>(R.id.forgotEmail)
        val forgotPass = findViewById<EditText>(R.id.forgotPass)
        val resetBtn = findViewById<Button>(R.id.resetBtn)

        resetBtn.setOnClickListener{

            val email = forgotEmail.text.toString()
            val newPassword = forgotPass.text.toString()

            val userDetail: Map<String, Any> = hashMapOf(
                "password" to newPassword
            )

            db.collection("users")
                .whereEqualTo("email", email)

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
                                    Toast.makeText(this, "Successfully Reset Password", Toast.LENGTH_SHORT).show()

                                    val intent = Intent(this, Login::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Reset failed: ${e.message}", Toast.LENGTH_SHORT).show()
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