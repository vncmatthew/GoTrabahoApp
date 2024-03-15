package com.example.gotrabahomobile.Helper

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.DTO.BookingUserDTO
import com.example.gotrabahomobile.Model.Booking
import com.example.gotrabahomobile.Remote.BookingRemote.BookingInstance
import com.example.gotrabahomobile.databinding.ItemLayoutBinding
import com.example.gotrabahomobile.databinding.LayoutActivityCardBinding
import java.util.ArrayList

class ActivityRecycleViewAdapter(private val bookingList: ArrayList<BookingUserDTO>, private val context: Context) : RecyclerView.Adapter<ActivityRecycleViewAdapter.ActivityViewHolder>() {


    inner class ActivityViewHolder(val binding: LayoutActivityCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActivityRecycleViewAdapter.ActivityViewHolder {
        return ActivityViewHolder(
            LayoutActivityCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val currentItem = bookingList[position]


        holder.binding.apply {
            customerNameActivity.text =
                "${currentItem.freelancerFirst + " " + currentItem.freelancerLast}"
            serviceActivity.text = "${currentItem.serviceType}"
            dateActivity.text = "${currentItem.bookingDatetime}"
            priceActivity.text = "${currentItem.amount}"
        }


    }
}