package Admin

import User.Profile
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobileassignment.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adminmain)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.admin_navigation)
        bottomNavigationView.selectedItemId = R.id.admin_home

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.admin_home -> true
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