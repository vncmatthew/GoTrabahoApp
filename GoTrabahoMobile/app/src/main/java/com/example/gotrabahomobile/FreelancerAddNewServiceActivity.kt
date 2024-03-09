package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class FreelancerAddNewServiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_add_new_service)

        val submitButton: Button = findViewById(R.id.buttonFreelancerEditServiceSubmit)

        submitButton.setOnClickListener{
            val intent = Intent(this, FreelancerMainActivity::class.java)
            Toast.makeText(this, "Your new service is being reviewed", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }
}