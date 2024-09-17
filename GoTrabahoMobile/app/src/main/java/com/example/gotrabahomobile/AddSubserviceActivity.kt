package com.example.gotrabahomobile

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import com.example.gotrabahomobile.DTO.SubService
import com.example.gotrabahomobile.DTO.SubServicesTypes
import com.example.gotrabahomobile.Helper.SubServiceAdapter
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddSubserviceActivity : AppCompatActivity() {

    private lateinit var subServiceSpinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_subservice)

        subServiceSpinner = findViewById(R.id.spinnerSubservice)
        val backButton = findViewById<ImageButton>(R.id.back_buttonNavbar)
        val confirmSubservice = findViewById<Button>(R.id.buttonConfirmSubservice)

        val serviceId = intent.getIntExtra("serviceId", 0)
        val serviceType = intent.getStringExtra("serviceType")

        fetchSubService()

        backButton.setOnClickListener {
            finish()
        }

        confirmSubservice.setOnClickListener {

            val selectedSubService = subServiceSpinner.selectedItem as SubServicesTypes
            val subServiceId = selectedSubService.subServiceTypeId
            addSubService(subServiceId!!)
            val intent = Intent(this@AddSubserviceActivity, FreelancerServicesListActivity::class.java)
            intent.putExtra("serviceId", serviceId)
            intent.putExtra("serviceType", serviceType)
            startActivity(intent)
        }
    }





    private fun fetchSubService(){
        val call = ServicesInstance.retrofitBuilder
        val serviceType = intent.getStringExtra("serviceTypeName")
        call.getSubServicesPerService(serviceType).enqueue(object: Callback<List<SubServicesTypes>>{
            override fun onResponse(
                call: Call<List<SubServicesTypes>>,
                response: Response<List<SubServicesTypes>>
            ) {
                if(response.isSuccessful){
                    val subserviceResponse = response.body()
                    if(subserviceResponse != null){
                        val adapter = SubServiceAdapter(this@AddSubserviceActivity, subserviceResponse)
                        subServiceSpinner.adapter = adapter
                    }
                } else{
                    Log.e(ContentValues.TAG, "Error fetching SubServices")
                }
            }

            override fun onFailure(call: Call<List<SubServicesTypes>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun addSubService(subServiceTypeId: Int){
        val call = ServicesInstance.retrofitBuilder
        val serviceId = intent.getIntExtra("serviceId", 0)
        val subServiceInput = SubService(
            serviceId = serviceId,
            subServiceTypeId = subServiceTypeId
        )
        call.insertSubService(subServiceInput).enqueue(object: Callback<SubService>{
            override fun onResponse(call: Call<SubService>, response: Response<SubService>) {
                if(response.isSuccessful){
                    Log.d("Success", "Successfully added a SubService")
                }
            }

            override fun onFailure(call: Call<SubService>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}