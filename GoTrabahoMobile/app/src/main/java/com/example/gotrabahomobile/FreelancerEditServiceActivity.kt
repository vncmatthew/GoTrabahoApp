package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.gotrabahomobile.Model.Services
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
                val intent = Intent(this@FreelancerEditServiceActivity, FreelancerMainActivity::class.java)
                startActivity(intent)
            }
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