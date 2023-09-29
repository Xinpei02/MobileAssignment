package Admin

import User.UserProfile
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobileassignment.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminOrder : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_order)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.admin_navigation)
        bottomNavigationView.selectedItemId = R.id.admin_order

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.admin_home -> {
                    startActivity(Intent(applicationContext, AdminMainActivity::class.java))
                    finish()
                    true
                }
                R.id.admin_order -> true

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
    }
}