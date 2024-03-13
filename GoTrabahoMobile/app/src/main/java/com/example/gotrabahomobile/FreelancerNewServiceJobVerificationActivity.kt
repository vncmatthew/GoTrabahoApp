package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class FreelancerNewServiceJobVerificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_new_service_job_verification)

        val nextButton: Button = findViewById(R.id.buttonNewServiceNext)
        val back: ImageView = findViewById(R.id.imageViewBackButton)

        nextButton.setOnClickListener{
            val freelancerId = intent.getIntExtra("freelancerId", 0)
            val intent = Intent(this, FreelancerAddNewServiceActivity::class.java)
            intent.putExtra("freelancerId", freelancerId)
            startActivity(intent)
        }

        back.setOnClickListener{
            finish()
        }
    }
}