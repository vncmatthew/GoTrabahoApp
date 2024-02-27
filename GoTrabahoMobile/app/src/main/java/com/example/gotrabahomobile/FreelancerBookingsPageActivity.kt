package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gotrabahomobile.Helper.BookingFreelancerAdapter
import com.example.gotrabahomobile.Helper.ServiceAdapter
import com.example.gotrabahomobile.Model.Booking
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.Remote.BookingRemote.BookingInstance
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.databinding.ActivityCustomerHomePageBinding
import com.example.gotrabahomobile.databinding.ActivityFreelancerBookingsPageBinding
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FreelancerBookingsPageActivity : AppCompatActivity() {

    private lateinit var rvAdapter: BookingFreelancerAdapter
    private lateinit var bookingList: List<Booking>
    private lateinit var binding: ActivityFreelancerBookingsPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFreelancerBookingsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bookingList = listOf()

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