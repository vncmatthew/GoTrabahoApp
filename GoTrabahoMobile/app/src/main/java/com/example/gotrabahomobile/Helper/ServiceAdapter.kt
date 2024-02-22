package com.example.gotrabahomobile.Helper

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.LoginActivity
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.R
import com.example.gotrabahomobile.databinding.ItemLayoutBinding


class ServiceAdapter(private val serviceList: List<Services>, private val context: Context): RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {


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
        val currentItem = serviceList[position]
        holder.binding.apply{
            tvNameR.text = "Service Name: ${currentItem.name}"
            tvLocationR.text = "Location Name: ${currentItem.location}"
            tvRatingR.text = "Rating: ${currentItem.rating}"
            tvPriceR.text = "Price Estimate: ${currentItem.priceEstimate}"
        }
        holder.binding.btnChooseService.setOnClickListener{
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra("serviceId", currentItem.serviceId)
            context.startActivity(intent)
        }
    }
}