package com.example.gotrabahomobile.Helper

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.DTO.ServicesWUserId
import com.example.gotrabahomobile.FreelancerDetailsActivity
import com.example.gotrabahomobile.LoginActivity
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.Model.UserFirebase
import com.example.gotrabahomobile.R
import com.example.gotrabahomobile.databinding.ItemLayoutBinding


class ServiceAdapter(private val serviceList: List<ServicesWUserId>, private val context: Context, private val userList: ArrayList<UserFirebase>, private val identification: Int): RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {


    inner class ServiceViewHolder(val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        return ServiceViewHolder(
            ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        if (serviceList.isNotEmpty()) {
        val currentItem = serviceList[position]
        val user = userList.find{it.sqlId == currentItem.userId.toString()}

        holder.binding.apply{
            tvNameR.text = "Service Name: ${currentItem.name}"
            tvLocationR.text = "Location: ${currentItem.location}"
            tvRatingR.text = "Rating: ${currentItem.rating}"
            tvPriceR.text = "Price Estimate: ₱ ${currentItem.priceEstimate}"
        }
        holder.binding.btnChooseService.setOnClickListener{
            val intent = Intent(context, FreelancerDetailsActivity::class.java)
            intent.putExtra("userId",user!!.userId)
            intent.putExtra("firstName",user.firstName)
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
}