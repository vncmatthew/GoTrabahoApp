package com.example.gotrabahomobile.Helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.Model.Booking
import com.example.gotrabahomobile.Remote.BookingRemote.BookingInstance
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.databinding.ItemLayoutBinding

class ActivityRecycleViewAdapter(private val bookingList: List<Booking>, private val context: Context) : RecyclerView.Adapter<ActivityRecycleViewAdapter.ActivityViewHolder>() {


    inner class ActivityViewHolder(val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityRecycleViewAdapter.ActivityViewHolder {
        return ActivityViewHolder(
            ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val currentItem = bookingList[position]
        val call = BookingInstance.retrofitBuilder

        call.getBookings()
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }
}