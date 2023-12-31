package Admin

import User.Login
import User.UserProfile
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileassignment.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class AdminMainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var btnAdd: FloatingActionButton
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<User>
    private lateinit var txtAllUser: TextView
    private lateinit var txtTotalAdmin: TextView
    private lateinit var txtTotalUser: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.admin_navigation)
        bottomNavigationView.selectedItemId = R.id.admin_home

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.admin_home -> true
                R.id.admin_order -> {
                    startActivity(Intent(applicationContext, AdminDashboard::class.java))
                    finish()
                    true
                }

                R.id.admin_payment -> {
                    startActivity(Intent(applicationContext, AdminPayment::class.java))
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

        btnAdd =  findViewById(R.id.addBtn)
        userRecyclerView = findViewById(R.id.userList)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)

        txtAllUser = findViewById(R.id.txtAllUser)
        txtTotalAdmin = findViewById(R.id.txtTotalAdmin)
        txtTotalUser = findViewById(R.id.txtTotalUser)
        calculateAndDisplayAllUser()
        calculateAndDisplayAdmin()
        calculateAndDisplayUser()

        userArrayList = arrayListOf<User>()
        getUserData()

        btnAdd.setOnClickListener{
            val intent = Intent(this, SelectRole::class.java)
            startActivity(intent)
        }

    }

    private fun calculateAndDisplayAllUser() {
        db.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                var total = 0 // Initialize a counter

                for (document in documents) {
                    val email = document.getString("email") // Get the "email" field as a string

                    // Check if the "email" field is not null or empty
                    if (!email.isNullOrEmpty()) {
                        total++
                    }
                }

                // Display the total in a TextView
                txtAllUser.text = "$total"
            }
            .addOnFailureListener { e ->
                // Handle errors here
                txtAllUser.text = "Error: ${e.message}"
            }
    }

    private fun calculateAndDisplayAdmin() {
        db.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                var total = 0

                for (document in documents) {
                    val role = document.getString("role")

                    if (role == "Admin") {
                        total++
                    }
                }

                // Display the total in a TextView
                txtTotalAdmin.text = "$total"
            }
            .addOnFailureListener { e ->
                // Handle errors here
                txtTotalAdmin.text = "Error: ${e.message}"
            }
    }

    private fun calculateAndDisplayUser() {
        db.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                var total = 0

                for (document in documents) {
                    val role = document.getString("role")

                    // Check if the "email" field is not null or empty
                    if (role == "User") {
                        total++
                    }
                }

                // Display the total in a TextView
                txtTotalUser.text = "$total"
            }
            .addOnFailureListener { e ->
                // Handle errors here
                txtTotalUser.text = "Error: ${e.message}"
            }
    }

    private fun getUserData() {
        val db = FirebaseFirestore.getInstance()

        // Reference to a Firestore collection
        val userCollection = db.collection("users")

        // Create an ArrayList to store user data
        val userArrayList = ArrayList<User>()


        // Fetch data using the get() method
        userCollection.get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    userArrayList.clear()

                    for (document in result.documents) {
                        val user = document.toObject(User::class.java)
                        if (user != null) {
                            userArrayList.add(user)
                        }
                    }

                    // Update your RecyclerView adapter with the updated data
                    userRecyclerView.adapter = MyAdapter(userArrayList)
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors here
            }
    }

}