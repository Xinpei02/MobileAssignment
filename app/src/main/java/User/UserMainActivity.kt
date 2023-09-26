package User

import User.Donation.Donation
import User.Order.Order
import User.Payment.Payment
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobileassignment.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usermain)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.user_navigation)
        bottomNavigationView.selectedItemId = R.id.user_home

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.user_home -> true
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
                    startActivity(Intent(applicationContext, Profile::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}