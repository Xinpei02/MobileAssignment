package Admin

import User.Profile
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobileassignment.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminPayment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_payment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.admin_navigation)
        bottomNavigationView.selectedItemId = R.id.admin_payment

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.admin_home -> {
                    startActivity(Intent(applicationContext, AdminOrder::class.java))
                    finish()
                    true
                }
                R.id.admin_order -> {
                    startActivity(Intent(applicationContext, AdminOrder::class.java))
                    finish()
                    true
                }

                R.id.admin_payment -> true

                R.id.admin_donation -> {
                    startActivity(Intent(applicationContext, AdminDonation::class.java))
                    finish()
                    true
                }

                R.id.user_profile -> {
                    startActivity(Intent(applicationContext, Profile::class.java))
                    finish()
                    true
                }

                else -> false
            }
        }
    }
}