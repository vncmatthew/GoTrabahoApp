package com.example.gotrabahomobile.Helper

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.FreelancerDetailsActivity
import com.example.gotrabahomobile.Model.Booking
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.Model.UserFirebase
import com.example.gotrabahomobile.Remote.BookingRemote.BookingInstance
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.databinding.ItemLayoutBinding
import java.util.ArrayList

class CustomerHomeServicesAdapter(private val serviceList: ArrayList<Services>, private val context: Context, private val userList: ArrayList<UserFirebase>, private val identification: Int) : RecyclerView.Adapter<CustomerHomeServicesAdapter.ServiceViewHolder>() {

    inner class ServiceViewHolder(val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerHomeServicesAdapter.ServiceViewHolder {
        return ServiceViewHolder(
            ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        if (serviceList.isNotEmpty()) {
            val currentItem = serviceList[position]
            val user = userList[position]

            holder.binding.apply {
                tvNameR.text = "Service Name: ${currentItem.name}"
                tvLocationR.text = "Location Name: ${currentItem.location}"
                tvRatingR.text = "Rating: ${currentItem.rating}"
                tvPriceR.text = "Price Estimate: ${currentItem.priceEstimate}"
            }
            holder.binding.btnChooseService.setOnClickListener {
                val intent = Intent(context, FreelancerDetailsActivity::class.java)
                intent.putExtra("userId", user.userId)
                intent.putExtra("firstName", user.firstName)
                intent.putExtra("lastName", user.lastName)
                intent.putExtra("sqlId", identification)
                intent.putExtra("serviceId", currentItem.serviceId)
                intent.putExtra("serviceName", currentItem.name)
                intent.putExtra("rating", currentItem.rating)
                intent.putExtra("location", currentItem.location)
                intent.putExtra("price", currentItem.priceEstimate)
                intent.putExtra("description", currentItem.description)

                context.startActivity(intent)
            }
        } else {

        }
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

}