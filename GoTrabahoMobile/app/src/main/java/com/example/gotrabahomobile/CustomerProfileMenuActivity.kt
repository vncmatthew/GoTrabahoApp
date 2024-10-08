package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class CustomerProfileMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_profile_menu)

        val userId = intent.getIntExtra("userId", 0)
        val email = intent.getStringExtra("email")

        val cardChangePassword = findViewById<androidx.cardview.widget.CardView>(R.id.changePasswordCard)
        val cardEditAddress = findViewById<androidx.cardview.widget.CardView>(R.id.editAddressCard)

        val backButton: ImageButton = findViewById(R.id.back_buttonNavbar)

        backButton.setOnClickListener{
            finish()
        }

        cardChangePassword.setOnClickListener {
            val intent = Intent(this, CustomerMainActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("email", email)
            startActivity(intent)
        }

        cardEditAddress.setOnClickListener {
            val intent = Intent(this, CustomerEditAddressActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("email", email)
            startActivity(intent)
        }
    }
}