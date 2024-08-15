package com.example.gotrabahomobile

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btn_freelancer = findViewById<Button>(R.id.btnFreelancer)
        val btn_customer = findViewById<Button>(R.id.btnCustomer)

        btn_freelancer.setOnClickListener{
            val intent = Intent(this@RegisterActivity, FreelancerRegisterDetailsActivity::class.java)
            startActivity(intent)
        }

        btn_customer.setOnClickListener{
            val intent = Intent(this@RegisterActivity, CustomerRegisterDetailsActivity::class.java)
            startActivity(intent)
        }



    }

}
