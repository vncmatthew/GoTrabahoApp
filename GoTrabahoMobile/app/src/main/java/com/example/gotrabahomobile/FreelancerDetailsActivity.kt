package com.example.gotrabahomobile

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.gotrabahomobile.Model.Negotiation
import com.example.gotrabahomobile.Remote.NegotiationRemote.NegotiationInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

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
                                val chatTracker = "nego${sqlId}${negotiation?.serviceId}"
                                val lastName = intent.getStringExtra("lastName")
                                val firstName = intent.getStringExtra("firstName")
                                val name = intent.getStringExtra("serviceName")

                                createBookingChatEntry(chatTracker, userId!!) { success ->
                                    if (success) {
                                        println("BookingChat entry created successfully")
                                    } else {
                                        println("Failed to create BookingChat entry")
                                    }
                                }
                                val intent =
                                    Intent(this@FreelancerDetailsActivity, ChatActivityNegotiation::class.java)
                                intent.putExtra("negotiationId", negotiation?.negotiationId)
                                intent.putExtra("serviceName", name)
                                intent.putExtra("chatroomId", chatTracker)
                                intent.putExtra("userId", userId)
                                intent.putExtra("sqlId", sqlId)
                                intent.putExtra("lastName", lastName)
                                intent.putExtra("firstName", firstName)
                                intent.putExtra("serviceIdcustomer", negotiation?.serviceId)
                                startActivity(intent)

                        } else {

                        Log.e(ContentValues.TAG, "Negotiation object is null")
                    }
                }
                else {
                    Log.d("MainActivity", "Response code: ${response.message()}")

                }
            }

            override fun onFailure(call: Call<Negotiation>, t: Throwable) {
                Log.d("MainActivity", "Exception: ", t)
            }
        })
    }

    fun createBookingChatEntry(chatroomId: String, freelancerId: String, callback: (Boolean) -> Unit) {
        val bookingChatRef = FirebaseDatabase.getInstance().getReference("ChatRoom")
        val bookingChatId = bookingChatRef.push().key ?: return
        firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null) {
            val hashMap: HashMap<String, Any?> = HashMap()
            hashMap["customerId"] = firebaseUser!!.uid
            hashMap["freelancerId"] = freelancerId
            hashMap["chatroomId"] = chatroomId

            bookingChatRef.child(bookingChatId).setValue(hashMap)
                .addOnSuccessListener {
                    callback(true)
                }
                .addOnFailureListener { exception ->
                    Log.w("Firebase", "Error creating BookingChat entry: ", exception)
                    callback(false)
                }
        } else {
            Log.w("Firebase", "Error: No authenticated user found.")
            callback(false)
        }
    }
}