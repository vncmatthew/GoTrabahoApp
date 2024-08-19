package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class FreelancerProfileMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_profile_menu)

        val userId = intent.getIntExtra("userId", 0)
        val email = intent.getStringExtra("email")
        val freelancerId = intent.getIntExtra("freelancerId", 0)
        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")
        val fullName = intent.getStringExtra("fullName")

        Log.d("Identification@", "${userId}")
        Log.d("Identification@", "${firstName}")
        Log.d("Identification@", "${fullName}")
        Log.d("Identification@", "${lastName}")
        Log.d("FreelancerAccountFragment", freelancerId.toString())
        Log.d("FreelancerAccountFragment", "${email}")

        val cardChangePassword = findViewById<androidx.cardview.widget.CardView>(R.id.changePasswordCard)
        val cardEditAddress = findViewById<androidx.cardview.widget.CardView>(R.id.editAddressCard)

        cardChangePassword.setOnClickListener {
            val intent = Intent(this, FreelancerProfilePageActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("freelancerId", freelancerId)
            intent.putExtra("firstName", firstName)
            intent.putExtra("lastName", lastName)
            intent.putExtra("fullName", fullName)
            intent.putExtra("email", email)
            startActivity(intent)
        }

        cardEditAddress.setOnClickListener {
            val intent = Intent(this, CustomerEditAddressActivity::class.java)
            startActivity(intent)
        }
    }
}