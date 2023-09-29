package Admin

import User.UserProfile
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.mobileassignment.R
import com.google.android.material.card.MaterialCardView

class SelectRole : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_role)

        val selectUser = findViewById<MaterialCardView>(R.id.selectUser)
        val selectAdmin = findViewById<MaterialCardView>(R.id.selectAdmin)
        val cancelBtn = findViewById<Button>(R.id.cancelBtn)

        selectUser.setOnClickListener{
            val intent = Intent(this, AddUser::class.java)
            startActivity(intent)
        }

        selectAdmin.setOnClickListener{
            val intent = Intent(this, AddAdmin::class.java)
            startActivity(intent)
        }


        cancelBtn.setOnClickListener{
            val intent = Intent(this, AdminMainActivity::class.java)
            startActivity(intent)
        }
    }
}