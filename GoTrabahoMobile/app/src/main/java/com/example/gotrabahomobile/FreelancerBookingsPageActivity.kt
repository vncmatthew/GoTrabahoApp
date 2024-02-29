package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gotrabahomobile.Helper.BookingFreelancerAdapter
import com.example.gotrabahomobile.Helper.ServiceAdapter
import com.example.gotrabahomobile.Model.Booking
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.Model.UserFirebase
import com.example.gotrabahomobile.Remote.BookingRemote.BookingInstance
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.databinding.ActivityCustomerHomePageBinding
import com.example.gotrabahomobile.databinding.ActivityFreelancerBookingsPageBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FreelancerBookingsPageActivity : AppCompatActivity() {

    private lateinit var rvAdapter: BookingFreelancerAdapter
    private lateinit var bookingList: List<Booking>
    private lateinit var binding: ActivityFreelancerBookingsPageBinding
    var selectedService: String? = null
    var userList = ArrayList<UserFirebase>()
    private lateinit var rvsAdapter: ServiceAdapter
    private lateinit var serviceList: List<Services>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFreelancerBookingsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bookingList = listOf()
        serviceList = listOf()

        val serviceTypeName = findViewById<Spinner>(R.id.spinnerBookingsServiceName)
        val serviceTypesArray = resources.getStringArray(R.array.serviceTypes)
        val serviceTypesList = serviceTypesArray.toMutableList()

        val adapter = ArrayAdapter(this@FreelancerBookingsPageActivity,
            android.R.layout.simple_spinner_item, serviceTypesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        serviceTypeName.adapter = adapter
        serviceTypeName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Store the selected item in the variable
                selectedService = parent.getItemAtPosition(position) as? String
                getBookingList(selectedService)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(this@FreelancerBookingsPageActivity, "Please Select a Service", Toast.LENGTH_SHORT).show()
            }
        }

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        tabLayout.addTab(tabLayout.newTab().setText(R.string.status_pending))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.status_ongoing))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.status_completed))
        binding.btnFreelancerMessage.setOnClickListener{
            val intent = Intent(this@FreelancerBookingsPageActivity, FreelancerMessagesActivity::class.java)
            startActivity(intent)
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // Handle tab selection
                when (tab.position) {
                    0 -> {
                       getBookingList(1)
                    }
                    1 -> {
                        getBookingList(2)
                    }
                    2 -> {
                        getBookingList(3)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Handle tab unselection
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Handle tab reselection
            }
        })


    }



    private fun getBookingList(status: Int){
        //val userId = intent.getIntExtra("userID", 0)
        //val freelancerId = intent.getIntExtra("freelancerId", 0)
        val service = BookingInstance.retrofitBuilder

        service.getBookingStatus(50,status).enqueue(object : Callback<List<Booking>> {
            override fun onResponse(
                call: Call<List<Booking>>,
                response: Response<List<Booking>>
            ) {
                if (response.isSuccessful && response.body() != null){
                    bookingList = response.body()!!
                    binding.rvFreeelancerHome.apply {
                        rvAdapter = BookingFreelancerAdapter(bookingList, this@FreelancerBookingsPageActivity)
                        adapter = rvAdapter
                        layoutManager = LinearLayoutManager(this@FreelancerBookingsPageActivity)
                    }
                }
                else{
                    Log.d("TestActivity", "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<Booking>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}