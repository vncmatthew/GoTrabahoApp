package com.example.gotrabahomobile

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.gotrabahomobile.Model.Negotiation
import com.example.gotrabahomobile.Remote.NegotiationRemote.NegotiationInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FreelancerDetailsActivity : AppCompatActivity() {

    private lateinit var btnNegotiate: Button
    private lateinit var serviceName: TextView
    private lateinit var serviceDescription: TextView
    private lateinit var servicePrice: TextView
    private lateinit var serviceLocation: TextView
    private lateinit var serviceRating: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_details)

        btnNegotiate = findViewById(R.id.buttonNegotiate)
        serviceName = findViewById(R.id.textViewFreelancerName)
        serviceDescription = findViewById(R.id.textViewDescription)
        servicePrice = findViewById(R.id.textViewPrice)
        serviceLocation = findViewById(R.id.textViewLocation)
        serviceRating = findViewById(R.id.textViewRating)

        val Id = intent.getIntExtra("serviceId", 0)
        val name = intent.getStringExtra("serviceName")
        val rating = intent.getFloatExtra("rating", 0.0f)
        val location = intent.getStringExtra("location")
        val price = intent.getDoubleExtra("price", 0.0)
        val description = intent.getStringExtra("description")
        val negotiation = intent.getIntExtra("negotiationId", 0)

        val userId = intent.getStringExtra("userId")
        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")

        serviceName.text = name
        serviceRating.text = rating.toString()
        serviceDescription.text = description
        serviceLocation.text = location
        servicePrice.text = price.toString()

        btnNegotiate.setOnClickListener{
            setNegotiation()
        }

        val backButton: ImageButton = findViewById(R.id.back_buttonNavbar)
        backButton.setOnClickListener{
            finish()
        }

    }

    fun setNegotiation(){

        val sqlId = intent.getIntExtra("sqlId", 0)
        val Id = intent.getIntExtra("serviceId", 0)
        val name = intent.getStringExtra("serviceName")
        val negotiationInput = Negotiation(
            freelancerPrice = 1.0,
            customerPrice = 22.0,
            customerId = sqlId,
            serviceId = Id,
            tracker = sqlId.toString() + Id.toString() + name)

        val userService = NegotiationInstance.retrofitBuilder
        userService.insertNegotiation(negotiationInput).enqueue(object : Callback<Negotiation> {
            override fun onResponse(call: Call<Negotiation>, response: Response<Negotiation>) {
                Log.i(ContentValues.TAG, "The response is " + response.message());
                Log.i(ContentValues.TAG, "The response is " + response.body());
                val userId = intent.getStringExtra("userId")
                if (response.isSuccessful) {
                    val negotiation = response.body()
                    if (negotiation != null) {

                                val lastName = intent.getStringExtra("lastName")
                                val firstName = intent.getStringExtra("firstName")
                                val name = intent.getStringExtra("serviceName")
                                val intent =
                                    Intent(this@FreelancerDetailsActivity, ChatActivity::class.java)
                                intent.putExtra("negotiationId", negotiation?.negotiationId)
                                intent.putExtra("serviceName", name)
                                intent.putExtra("userId", userId)
                                intent.putExtra("sqlId", sqlId)
                                intent.putExtra("lastName", lastName)
                                intent.putExtra("firstName", firstName)
                                intent.putExtra("serviceIdcustomer", negotiation?.serviceId)
                                startActivity(intent)

                        } else {
                        // Handle the case where the negotiation object is null
                        Log.e(ContentValues.TAG, "Negotiation object is null")
                    }
                }
                else {
                    // Handle the error response
                    Log.d("MainActivity", "Response code: ${response.message()}")

                }
            }

            override fun onFailure(call: Call<Negotiation>, t: Throwable) {
                // Handle network or other exceptions
                Log.d("MainActivity", "Exception: ", t)
            }
        })
    }
}