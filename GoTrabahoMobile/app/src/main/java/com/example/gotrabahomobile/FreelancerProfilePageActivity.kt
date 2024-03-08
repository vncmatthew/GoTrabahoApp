package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.example.gotrabahomobile.Model.Freelancer

class FreelancerProfilePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_profile_page)

        val cancelButton: Button = findViewById(R.id.buttonFreelancerEditServiceCancel)
        cancelButton.setOnClickListener{
            finish()
        }

        val editProfileButton: Button = findViewById(R.id.buttonEditProfile)
        editProfileButton.setOnClickListener{
            val intent = Intent(this, FreelancerEditProfileActivity::class.java)
            startActivity(intent)
        }
    }
}