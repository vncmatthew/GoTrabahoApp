package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView

class RewardsActivity : AppCompatActivity() {

    private var counter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewards)

        val backButton: ImageButton = findViewById(R.id.back_buttonNavbar)
        val availButton: Button = findViewById(R.id.buttonAvail)

        availButton.setOnClickListener {
            counter++
            updateRewardCircles()


        }

        backButton.setOnClickListener{
            finish()
        }


    }

    private fun updateRewardCircles() {
        val rewardGrid: GridLayout = findViewById(R.id.rewardGrid)
        val circles = arrayOf(
            findViewById<ImageView>(R.id.circle1),
            findViewById<ImageView>(R.id.circle2),
            findViewById<ImageView>(R.id.circle3),
            findViewById<ImageView>(R.id.circle4),
            findViewById<ImageView>(R.id.circle5),
            findViewById<ImageView>(R.id.circle6),
            findViewById<ImageView>(R.id.circle7),
            findViewById<ImageView>(R.id.circle8)
        )

        for (i in circles.indices) {
            if (i < counter) {
                circles[i].setImageResource(R.drawable.reward_checked)
            } else {
                circles[i].setImageResource(R.drawable.reward_notchecked)
            }
        }
    }
}