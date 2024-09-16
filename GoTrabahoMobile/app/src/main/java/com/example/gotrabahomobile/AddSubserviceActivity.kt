package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class AddSubserviceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_subservice)

        val backButton = findViewById<ImageButton>(R.id.back_buttonNavbar)
        val confirmSubservice = findViewById<Button>(R.id.buttonConfirmSubservice)

        backButton.setOnClickListener {
            finish()
        }

        confirmSubservice.setOnClickListener {
            finish()
        }
    }
}