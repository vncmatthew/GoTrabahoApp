package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.databinding.ActivityFreelancerEditServiceBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FreelancerEditServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFreelancerEditServiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFreelancerEditServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)



        var name = binding.editTextEditServiceName.text.toString()
        var description = binding.editTextEditDescription.text.toString()
        var price: Double
        try {
            price = binding.editTextEditPrice.text.toString().toDouble()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show()
            return
        }
        var location = binding.editTextEditLocation.text.toString()

        binding.buttonFreelancerEditServiceCancel.setOnClickListener {
            finish()
            Toast.makeText(this, "Cancel button clicked", Toast.LENGTH_SHORT).show()
        }

        binding.buttonFreelancerEditServiceSave.setOnClickListener{
            editService(name, description, price,location)
            Log.d("Test", "CheckUpButtonClicked")
            val intent = Intent(this@FreelancerEditServiceActivity, FreelancerServicesListActivity::class.java)
            startActivity(intent)
        }


    }


    private fun editService(name: String, description: String, price:  Double, location: String){
        val service = ServicesInstance.retrofitBuilder
        val serviceId = intent.getIntExtra("serviceId", 0)
        val serviceTypName = intent.getStringExtra("serviceTypeName")
        val status = intent.getBooleanExtra("status", true)
        val rating = intent.getFloatExtra("rating", 0f)
        val freelancerId = intent.getIntExtra("freelancerId", 0)


        val updatedService = Services(
            serviceId = serviceId,
            freelancerId = freelancerId,
            name = name,
            description = description,
            priceEstimate = price,
            serviceTypeName = serviceTypName,
            status = status,
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
}