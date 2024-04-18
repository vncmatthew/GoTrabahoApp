package com.example.gotrabahomobile.Helper

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.BookingDetailsActivity
import com.example.gotrabahomobile.DTO.BookingUserDTO
import com.example.gotrabahomobile.R
import com.example.gotrabahomobile.databinding.LayoutActivityCardBinding


class BookingUserAdapter(private val context: Context, private val bookingList: List<BookingUserDTO>, private val email: String?) :
    RecyclerView.Adapter<BookingUserAdapter.BookingViewHolder>() {

    inner class BookingViewHolder(val binding: LayoutActivityCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        return BookingViewHolder(
            LayoutActivityCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }
    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookingList[position]

        holder.binding.apply{
            customerNameActivity.text = "${booking.freelancerFirst}" + " " + "${booking.freelancerLast}"
            priceActivity.text = "₱${booking.amount}"
            serviceActivity.text = " ${booking.serviceType}"
            dateActivity.text = " ${booking.bookingDatetime}"

        }
        holder.binding.customerProfile.setOnClickListener {
            val intent = Intent(context, BookingDetailsActivity::class.java)
            intent.putExtra("bookingId", booking.bookingId)
            intent.putExtra("serviceType", booking.serviceType)
            intent.putExtra("email", email)
            intent.putExtra("amount", booking.amount)
            intent.putExtra("amount", booking.serviceId)
            intent.putExtra("firstName",booking.freelancerFirst)
            intent.putExtra("lastName", booking.freelancerLast)
            intent.putExtra("sqlId", booking.customerId)
            context.startActivity(intent)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtUserName: TextView = view.findViewById(R.id.customerName)
        val layoutUser: LinearLayout = view.findViewById(R.id.layoutFreelancerMessage)
    }
}