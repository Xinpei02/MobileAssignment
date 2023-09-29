package User.Payment

import User.Donation.Donation
import User.Order.Order
import User.UserProfile
import User.UserMainActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobileassignment.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class Payment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.user_navigation)
        bottomNavigationView.selectedItemId = R.id.user_payment

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
                R.id.user_payment -> true
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
    }
}