package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class FreelancerDetailsActivity : AppCompatActivity() {

    private lateinit var btnSetSchedule: Button
    private lateinit var btnNegotiate: Button
    private lateinit var serviceName: TextView
    private lateinit var serviceDescription: TextView
    private lateinit var servicePrice: TextView
    private lateinit var serviceLocation: TextView
    private lateinit var serviceRating: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_details)

        btnSetSchedule = findViewById(R.id.buttonSetSchedule)
        btnNegotiate = findViewById(R.id.buttonNegotiate)
        serviceName = findViewById(R.id.textViewFreelancerName)
        serviceDescription = findViewById(R.id.textViewDescription)
        servicePrice = findViewById(R.id.textViewPrice)
        serviceLocation = findViewById(R.id.textViewLocation)
        serviceRating = findViewById(R.id.textViewRating)

        val Id = intent.getIntExtra("serviceId", 0)
        val name = intent.getStringExtra("name")
        val rating = intent.getFloatExtra("rating", 0.0f)
        val location = intent.getStringExtra("location")
        val price = intent.getDoubleExtra("price", 0.0)
        val description = intent.getStringExtra("description")

        serviceName.text = name
        serviceRating.text = rating.toString()
        serviceDescription.text = description
        serviceLocation.text = location
        servicePrice.text = price.toString()



    }
}