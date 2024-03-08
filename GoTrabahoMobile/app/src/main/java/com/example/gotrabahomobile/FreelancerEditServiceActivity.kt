package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class FreelancerEditServiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_edit_service)

        val cancelEditButton = findViewById<Button>(R.id.buttonFreelancerEditServiceCancel)

        cancelEditButton.setOnClickListener {
            finish()
        }
    }
}