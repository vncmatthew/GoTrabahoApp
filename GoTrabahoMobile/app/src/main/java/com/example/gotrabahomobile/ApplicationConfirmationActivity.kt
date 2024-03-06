package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ApplicationConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application_confirmation)

        val text = findViewById<TextView>(R.id.textViewAppReview)

        text.setOnClickListener{
            val intent = Intent(this, FreelancerMainActivity::class.java)
            startActivity(intent)
        }
    }
}