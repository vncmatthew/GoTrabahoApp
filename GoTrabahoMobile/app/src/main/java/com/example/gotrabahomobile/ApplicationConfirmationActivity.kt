package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class ApplicationConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application_confirmation)

        val text = findViewById<TextView>(R.id.textViewAppReview)
        val userId = intent.getIntExtra("userId", 0)
        val freelancerId = intent.getIntExtra("freelancerId", 0)
        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")
        val fullName = intent.getStringExtra("fullName")
        val email = intent.getStringExtra("email")
        Log.d("ApplicationConfirmationActivity", freelancerId.toString())
        Log.d("ApplicationConfirmationActivity", "${email}")

        text.setOnClickListener{
            val intent = Intent(this, FreelancerMainActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("freelancerId", freelancerId)
            intent.putExtra("firstName", firstName)
            intent.putExtra("lastName", lastName)
            intent.putExtra("fullName", fullName)
            intent.putExtra("email", email)


            startActivity(intent)
        }


    }
}