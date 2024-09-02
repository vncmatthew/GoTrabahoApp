package com.example.gotrabahomobile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class AddPaymentMethodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_payment_method)

        val addMaya = findViewById<androidx.cardview.widget.CardView>(R.id.linkmaya)
        val backButton: ImageButton = findViewById(R.id.back_buttonNavbar)

        addMaya.setOnClickListener {
            val maya_url = "https://play.google.com/store/apps/details?id=com.paymaya&hl=en_US&pli=1"
            val url = Uri.parse(maya_url)
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }

        backButton.setOnClickListener{
            finish()
        }
    }
}