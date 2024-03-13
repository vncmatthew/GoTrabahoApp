package com.example.gotrabahomobile.Helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.DTO.BookingUserDTO
import com.example.gotrabahomobile.DTO.ServicesDTO
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.Model.UserFirebase
import com.example.gotrabahomobile.R
import com.example.gotrabahomobile.Remote.BookingRemote.BookingInstance
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.databinding.ItemLayoutBinding
import com.example.gotrabahomobile.databinding.LayoutActivityCardBinding
import com.example.gotrabahomobile.databinding.ListItemServicesListEditDeleteBinding

class ServicesListAdapter(private val servicesList: List<ServicesDTO>) : RecyclerView.Adapter<ServicesListAdapter.ServiceListViewHolder>() {

    inner class ServiceListViewHolder(val binding: ListItemServicesListEditDeleteBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceListViewHolder {
        return ServiceListViewHolder(
            ListItemServicesListEditDeleteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ServiceListViewHolder, position: Int) {
        val services = servicesList[position]

        holder.binding.apply{
            editInfoServiceName.text = "${services.name}"

        }
    }

    override fun getItemCount(): Int {
        return servicesList.size
    }

}