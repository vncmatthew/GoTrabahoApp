package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class CustomerProfilePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_profile_page)

        val backButton: ImageButton = findViewById(R.id.back_buttonNavbar)
        backButton.setOnClickListener{
            finish()
        }
    }
}