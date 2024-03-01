package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.gotrabahomobile.DTO.NegotiationResponse
import com.example.gotrabahomobile.Model.Negotiation
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.Remote.NegotiationRemote.NegotiationInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        val userId = intent.getStringExtra("userId")
        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")
        val sqlId = intent.getIntArrayExtra("sqlId")
        serviceName.text = name
        serviceRating.text = rating.toString()
        serviceDescription.text = description
        serviceLocation.text = location
        servicePrice.text = price.toString()

        btnNegotiate.setOnClickListener{
            setNegotiation()

            val intent = Intent(this@FreelancerDetailsActivity, ChatActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("firstName", firstName)
            intent.putExtra("lastName", lastName)
            intent.putExtra("serviceId", Id)
            intent.putExtra("sqlId", sqlId)
            startActivity(intent)
        }

    }

    fun setNegotiation(){

        val sqlId = intent.getIntExtra("sqlId", 0)
        val Id = intent.getIntExtra("serviceId", 0)
        val userId = intent.getStringExtra("userId")
        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")
        val negotiationInput = Negotiation(
            customerId = sqlId?.toInt(),
            serviceId = Id.toInt(),
            customerPrice = 0.0,
            freelancerPrice = 1.0,
            negotiationStatus = true)

        val call = NegotiationInstance.retrofitBuilder
        call.insertNegotiation(negotiationInput).enqueue(object: Callback <NegotiationResponse>{
            override fun onResponse(call: Call<NegotiationResponse>, response: Response<NegotiationResponse>) {

                if (response.isSuccessful) {
                    val responseString = response.body()?.response
                    if (responseString == "success") {
                        // Proceed to ChatActivity

                    } else {
                        // Handle other responses
                        Log.d("MainActivity", "Negotiation response: $responseString")
                    }
                } else {
                    // Handle the error response
                    Log.d("MainActivity", "Response code: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<NegotiationResponse>, t: Throwable) {
                Log.d("MainActivity", "Response code: ${t}")
            }

        })
    }
}