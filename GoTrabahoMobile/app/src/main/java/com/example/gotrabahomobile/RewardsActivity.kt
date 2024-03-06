package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class RewardsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewards)

        val backButton: ImageButton = findViewById(R.id.back_buttonNavbar)
        backButton.setOnClickListener{
            finish()
        }
    }
}