package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gotrabahomobile.DTO.ServiceTypeListDTO
import com.example.gotrabahomobile.Helper.ServiceAdapter
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.Wrapper.ServiceTypeList
import com.example.gotrabahomobile.databinding.ActivityCustomerHomePageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerHomePageActivity : AppCompatActivity() {

    private lateinit var customerToolbar: androidx.appcompat.widget.Toolbar
    private lateinit var binding: ActivityCustomerHomePageBinding
    var selectedService: String? = null
    private lateinit var rvAdapter: ServiceAdapter
    private lateinit var serviceList: List<Services>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        serviceList = listOf()

        val serviceTypeName = findViewById<Spinner>(R.id.spinnerServiceName)
        val serviceTypesArray = resources.getStringArray(R.array.serviceTypes)
        val serviceTypesList = serviceTypesArray.toMutableList()

        val adapter = ArrayAdapter(this@CustomerHomePageActivity,
            android.R.layout.simple_spinner_item, serviceTypesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        serviceTypeName.adapter = adapter
        serviceTypeName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Store the selected item in the variable
                selectedService = parent.getItemAtPosition(position) as? String
                getServiceList(selectedService)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(this@CustomerHomePageActivity, "Please Select a Service", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getServiceList(select: String?){
        val userId = intent.getIntExtra("userID", 0)
        val service = ServicesInstance.retrofitBuilder

        service.getServicesType(select).enqueue(object : Callback<List<Services>> {
            override fun onResponse(
                call: Call<List<Services>>,
                response: Response<List<Services>>
            ) {
                if (response.isSuccessful && response.body() != null){
                        serviceList = response.body()!!
                        binding.rvMain.apply {
                            rvAdapter = ServiceAdapter(serviceList, this@CustomerHomePageActivity)
                            adapter = rvAdapter
                            layoutManager = LinearLayoutManager(this@CustomerHomePageActivity)
                        }
                    }
                else{
                    Log.d("TestActivity", "Error: ${response.code()}")
                }
                }
            override fun onFailure(call: Call<List<Services>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

}