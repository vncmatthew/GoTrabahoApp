package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class FreelancerViewRatingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_view_ratings)

        val backButton = findViewById<ImageButton>(R.id.back_buttonNavbar)

        backButton.setOnClickListener {
            finish()
        }

        //show list_item_ratings.xml
    }
}