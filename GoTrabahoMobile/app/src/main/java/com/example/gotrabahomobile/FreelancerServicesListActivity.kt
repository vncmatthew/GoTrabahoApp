package com.example.gotrabahomobile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.DTO.ServicesDTO
import com.example.gotrabahomobile.DTO.UserDetails
import com.example.gotrabahomobile.Helper.FreelancerServiceListAdapter
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.databinding.ActivityFreelancerBookingsPageBinding
import com.example.gotrabahomobile.databinding.ActivityFreelancerServicesListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FreelancerServicesListActivity : AppCompatActivity() {

    private lateinit var servicesList: List<Services>
    private lateinit var rvAdapter: FreelancerServiceListAdapter
    private lateinit var binding: ActivityFreelancerServicesListBinding

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityFreelancerServicesListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        servicesList = listOf()



        val backButton: ImageButton = findViewById(R.id.back_buttonNavbar)
        backButton.setOnClickListener{
            finish()
        }

        val servicesListRecyclerView = findViewById<RecyclerView>(R.id.servicesListRecycler)



        servicesListRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        getServiceList()


    }

    fun showConfirmDeleteDialog() {
        val deleteView = LayoutInflater.from(this).inflate(R.layout.dialog_delete_service, null)

        val buttonNo = deleteView.findViewById<Button>(R.id.buttonDeleteServiceNo)
        val buttonYes = deleteView.findViewById<Button>(R.id.buttonDeleteServiceYes)

        val alertDialog = AlertDialog.Builder(this)
            .setView(deleteView)
            .create()

        buttonNo.setOnClickListener{
            alertDialog.dismiss()
        }

        buttonYes.setOnClickListener {
            Toast.makeText(this, "Service deleted", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun getServiceList(){
         val service = ServicesInstance.retrofitBuilder
         val freelancerId = intent.getIntExtra("freelancerId", 0)

        val userDetails = UserDetails(
            userId = intent.getIntExtra("userId", 0),
            freelancerId = intent.getIntExtra("freelancerId", 0),
            firstName = intent.getStringExtra("firstName") ?: "",
            lastName = intent.getStringExtra("lastName") ?: "",
            fullName = intent.getStringExtra("fullName") ?: "",
            email = intent.getStringExtra("email") ?: ""
        )

        val user =
        service.getFreelancerServices(freelancerId).enqueue(object: Callback<List<Services>>{
            override fun onResponse(

                call: Call<List<Services>>,
                response: Response<List<Services>>
            ) {
                servicesList = response.body()!!

                binding.servicesListRecycler.apply{
                    val freelancerId = intent.getIntExtra("freelancerId", 0)
                    rvAdapter = FreelancerServiceListAdapter(servicesList, this@FreelancerServicesListActivity, userDetails, freelancerId)
                    adapter = rvAdapter
                    layoutManager = LinearLayoutManager(this@FreelancerServicesListActivity)
                }

            }

            override fun onFailure(call: Call<List<Services>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}