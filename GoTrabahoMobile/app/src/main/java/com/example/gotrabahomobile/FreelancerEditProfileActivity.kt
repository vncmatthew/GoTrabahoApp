package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class FreelancerEditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_edit_profile)

        val cancelButton: Button = findViewById(R.id.buttonEditFreelancerProfileCancel)

        cancelButton.setOnClickListener {
            finish()
        }
    }
}