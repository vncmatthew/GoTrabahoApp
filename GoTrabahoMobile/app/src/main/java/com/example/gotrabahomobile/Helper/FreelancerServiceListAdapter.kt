package com.example.gotrabahomobile.Helper

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.DTO.ServicesDTO
import com.example.gotrabahomobile.FreelancerEditServiceActivity
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.databinding.ListItemServicesListEditDeleteBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FreelancerServiceListAdapter(private val servicesList: List<Services>, private val context: Context) : RecyclerView.Adapter<FreelancerServiceListAdapter.ServiceListViewHolder>() {

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
    override fun onBindViewHolder(holder: ServiceListViewHolder, position: Int) {
        val services = servicesList[position]

        holder.binding.apply{
            editInfoServiceName.text = "${services.name}"
            editInfoDescription.text = "${services.description}"
            editInfoPrice.text = "${services.priceEstimate}"

            holder.binding.buttonEditService.setOnClickListener{
                val intent = Intent(context, FreelancerEditServiceActivity::class.java)
                intent.putExtra("serviceId", services.serviceId)
                intent.putExtra("serviceTypeName", services.serviceTypeName)
                intent.putExtra("status", services.status)
                intent.putExtra("rating", services.rating)
                intent.putExtra("freelancerId", services.freelancerId)
                context.startActivity(intent)
            }

            holder.binding.buttonDeleteService.setOnClickListener {
                val service = ServicesInstance.retrofitBuilder
                service.deleteService(services.serviceId.toString()).enqueue(object: Callback<Services>{
                    override fun onResponse(call: Call<Services>, response: Response<Services>) {
                        Toast.makeText(
                            context,
                            "Successfully Deleted The Service",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onFailure(call: Call<Services>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })
            }

        }
    }



}