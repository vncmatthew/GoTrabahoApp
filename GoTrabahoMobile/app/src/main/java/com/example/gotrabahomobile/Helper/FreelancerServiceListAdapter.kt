package com.example.gotrabahomobile.Helper

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.DTO.ServicesDTO
import com.example.gotrabahomobile.DTO.UserDetails
import com.example.gotrabahomobile.FreelancerEditServiceActivity
import com.example.gotrabahomobile.FreelancerServicesListActivity
import com.example.gotrabahomobile.Model.Freelancer
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.R
import com.example.gotrabahomobile.Remote.FreelancerRemote.FreelancerInstance
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.databinding.ListItemServicesListEditDeleteBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FreelancerServiceListAdapter(private val servicesList: List<Services>, private val context: Context, private val UserDetails: UserDetails, private val freelancerId: Int) : RecyclerView.Adapter<FreelancerServiceListAdapter.ServiceListViewHolder>() {

    inner class ServiceListViewHolder(val binding: ListItemServicesListEditDeleteBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceListViewHolder {
        return ServiceListViewHolder(
            ListItemServicesListEditDeleteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return servicesList.size
    }
    fun changeStatusTrue(){
        val intent = Intent(context, FreelancerEditServiceActivity::class.java)
        val call = FreelancerInstance.retrofitBuilder
        val freelancer = Freelancer(
            freelancerId = freelancerId,
            verificationStatus = 1
        )
        call.patchFreelancer(freelancerId, freelancer).enqueue(object: Callback<Freelancer>{
            override fun onResponse(call: Call<Freelancer>, response: Response<Freelancer>) {
                if(response.isSuccessful){
                    Log.d("Changed Bookings", "True")
                }
            }

            override fun onFailure(call: Call<Freelancer>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun changeStatusFalse(){

        val intent = Intent(context, FreelancerEditServiceActivity::class.java)

        val call = FreelancerInstance.retrofitBuilder
        val freelancer = Freelancer(
            freelancerId = freelancerId,
            verificationStatus = 3
        )
        call.patchFreelancer(freelancerId, freelancer).enqueue(object: Callback<Freelancer>{
            override fun onResponse(call: Call<Freelancer>, response: Response<Freelancer>) {
                if(response.isSuccessful){
                    Log.d("Changed Bookings", "True")
                }
            }

            override fun onFailure(call: Call<Freelancer>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onBindViewHolder(holder: ServiceListViewHolder, position: Int) {
        val services = servicesList[position]

        holder.binding.apply {
            editInfoServiceName.text = "${services.name}"
            editInfoDescription.text = "${services.description}"
            editInfoPrice.text = "${services.priceEstimate}"

            holder.binding.buttonEditService.setOnClickListener {
                val intent = Intent(context, FreelancerEditServiceActivity::class.java)
                intent.putExtra("serviceId", services.serviceId)
                intent.putExtra("serviceTypeName", services.serviceTypeName)
                intent.putExtra("status", services.status)
                intent.putExtra("rating", services.rating)
                intent.putExtra("freelancerId", services.freelancerId)
                intent.putExtra("userId", UserDetails.userId)
                intent.putExtra("firstName", UserDetails.firstName)
                intent.putExtra("lastName", UserDetails.lastName)
                intent.putExtra("fullName", UserDetails.fullName)
                intent.putExtra("email", UserDetails.email)
                intent.putExtra("showService", services.showService)
                context.startActivity(intent)
            }

            holder.binding.buttonDeleteService.setOnClickListener {
                val service = ServicesInstance.retrofitBuilder
                service.deleteService(services.serviceId!!).enqueue(object : Callback<Services> {
                    override fun onResponse(call: Call<Services>, response: Response<Services>) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Successfully Deleted The Service",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Services>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })
            }

            holder.binding.ActiveStatusSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    changeStatusTrue()
                    switchLabel.text = "Active"

                } else {
                    changeStatusFalse()
                    switchLabel.text = "Inactive"
                }


            }

        }


    }
}