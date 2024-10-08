package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.gotrabahomobile.DTO.UserDetails
import com.example.gotrabahomobile.Model.Freelancer
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.Remote.FreelancerRemote.FreelancerInstance
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.databinding.ActivityFreelancerEditServiceBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FreelancerEditServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFreelancerEditServiceBinding
    lateinit var etName: EditText
    lateinit var etDescription: EditText
    lateinit var etPrice: EditText
    lateinit var etLocation: EditText
    var Dprice = null
    var name = ""
    var description = ""
    var price = ""
    var location = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_freelancer_edit_service)
        binding = ActivityFreelancerEditServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        var name: EditText = findViewById(R.id.editTextEditServiceName)
//        var nameString = name.toString()

        etName = binding.editTextEditServiceName
        etDescription = binding.editTextEditDescription
        etPrice = binding.editTextEditPrice
        etLocation = binding.editTextEditLocation
//        var price: Double? = null
        val backButton = findViewById<ImageButton>(R.id.back_buttonNavbar)
        backButton.setOnClickListener {
            finish()
        }


        binding.buttonEditServiceCancel.setOnClickListener {
            Log.d("ButtonClick", "Cancel button clicked")
            finish()
        }

        binding.buttonEditServiceSave.setOnClickListener{
            name = etName.text.toString()
            description = etDescription.text.toString()
            price = etPrice.text.toString()
            location = etLocation.text.toString()
            Log.d("SaveButton", "$name, $description, $price, $location ")
            if (price.isEmpty()) {
                Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_LONG).show()
            } else if (price == "0") {
                Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show()
            } else {
                price = etPrice.text.toString()
                editService(name, description, price.toDouble(), location)


                val userId = intent.getIntExtra("userId", 0)
                val firstName = intent.getStringExtra("firstName") ?: ""
                val lastName = intent.getStringExtra("lastName") ?: ""
                val fullName = intent.getStringExtra("fullName") ?: ""
                val email = intent.getStringExtra("email") ?: ""
                val freelancerId = intent.getIntExtra("freelancerId", 0)

                val intent = Intent(this@FreelancerEditServiceActivity, FreelancerMainActivity::class.java)
                intent.putExtra("userId", userId)
                intent.putExtra("firstName", firstName)
                intent.putExtra("lastName", lastName)
                intent.putExtra("fullName", fullName)
                intent.putExtra("email", email)
                intent.putExtra("freelancerId", freelancerId)
                startActivity(intent)
            }
        }
    }


    private fun editService(name: String, description: String, price:  Double, location: String){
        val service = ServicesInstance.retrofitBuilder
        val serviceId = intent.getIntExtra("serviceId", 0)
        val serviceTypName = intent.getStringExtra("serviceTypeName")
        val status = intent.getIntExtra("status", 0)
        val rating = intent.getFloatExtra("rating", 0f)
        val freelancerId = intent.getIntExtra("freelancerId", 0)
        val showService = intent.getBooleanExtra("showService", false)


        val updatedService = Services(
            serviceId = serviceId,
            freelancerId = freelancerId,
            name = name,
            description = description,
            priceEstimate = price,
            serviceTypeName = serviceTypName,
            status = status,
            showService = showService,
            location = location,
            rating = rating

        )
        service.updateServices(serviceId.toString(), updatedService).enqueue(object: Callback<Services>{
            override fun onResponse(call: Call<Services>, response: Response<Services>) {
                Toast.makeText(
                    this@FreelancerEditServiceActivity,
                    "Successfully Edited The Service",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<Services>, t: Throwable) {
                Toast.makeText(
                    this@FreelancerEditServiceActivity,
                    "Failed editing The Service",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })


    }

    fun changeStatusTrue(){

        val serviceId = intent.getIntExtra("serviceId", 0)
        val call = ServicesInstance.retrofitBuilder
        val services = Services(
            serviceId = serviceId,
            showService = true
        )
        call.patchServices(services).enqueue(object: Callback<Services>{
            override fun onResponse(call: Call<Services>, response: Response<Services>) {
                if(response.isSuccessful){
                    Log.d("Changed Bookings", "True")
                }
            }

            override fun onFailure(call: Call<Services>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun changeStatusFalse(){

        val serviceId = intent.getIntExtra("serviceId", 0)
        val call = ServicesInstance.retrofitBuilder
        val services = Services(
            serviceId = serviceId,
            showService = false
        )
        call.patchServices(services).enqueue(object: Callback<Services>{
            override fun onResponse(call: Call<Services>, response: Response<Services>) {
                if(response.isSuccessful){
                    Log.d("Changed Bookings", "False")
                }
            }

            override fun onFailure(call: Call<Services>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}