package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView

class RewardsActivity : AppCompatActivity() {

    private var counter: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewards)

        val counter2 = intent.getIntExtra("counter", 1)

        counter = counter2

        val backButton: ImageButton = findViewById(R.id.back_buttonNavbar)
        val availButton: Button = findViewById(R.id.buttonAvail)

        availButton.setOnClickListener {

//            Handler(Looper.getMainLooper()).postDelayed({
                updateRewardCircles()

//            }, 1000)


            val intent = Intent(this, CustomerMainActivity::class.java)
            intent.putExtra("counter", counter)
            startActivity(intent)
        }

        backButton.setOnClickListener{
            finish()
        }


    }

    private fun updateRewardCircles() {
        counter++
        Log.d("RewardsAct", "counter = $counter")
        val rewardGrid: GridLayout = findViewById(R.id.rewardGrid)

        val circle1 = findViewById<ImageView>(R.id.circle1)
        val circle2 = findViewById<ImageView>(R.id.circle2)
        val circle3 = findViewById<ImageView>(R.id.circle3)
        val circle4 = findViewById<ImageView>(R.id.circle4)
        val circle5 = findViewById<ImageView>(R.id.circle5)
        val circle6 = findViewById<ImageView>(R.id.circle6)
        val circle7 = findViewById<ImageView>(R.id.circle7)
        val circle8 = findViewById<ImageView>(R.id.circle8)


        if (counter >= 1) {
            circle1.setImageResource(R.drawable.reward_checked)
        }
        if (counter >= 2) {
            circle1.setImageResource(R.drawable.reward_checked)
            circle2.setImageResource(R.drawable.reward_checked)
        }
        if (counter >= 3) {
            circle1.setImageResource(R.drawable.reward_checked)
        }
        if (counter >= 4) {
            circle1.setImageResource(R.drawable.reward_checked)
        }
        if (counter >= 5) {
            circle1.setImageResource(R.drawable.reward_checked)
        }
        if (counter >= 6) {
            circle1.setImageResource(R.drawable.reward_checked)
        }
        if (counter >= 7) {
            circle1.setImageResource(R.drawable.reward_checked)
        }
        if (counter >= 8) {
            circle1.setImageResource(R.drawable.reward_checked)
        }

//        for (i in circles.indices) {
//            if (i < counter) {
//                circles[i].setImageResource(R.drawable.reward_checked)
//            } else {
//                circles[i].setImageResource(R.drawable.reward_notchecked)
//            }
//        }
    }
}