package com.example.gotrabahomobile.Helper

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.FreelancerDetailsActivity
import com.example.gotrabahomobile.FreelancerEditServiceActivity
import com.example.gotrabahomobile.Model.Booking
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.PaymentActivity
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance

import com.example.gotrabahomobile.databinding.BookingLayoutBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.format.DateTimeFormatter

class BookingFreelancerAdapter(private val bookingList: List<Booking>, private val context: Context, private val email: String?, private val status: Int): RecyclerView.Adapter<BookingFreelancerAdapter.BookingViewHolder>() {


    inner class BookingViewHolder(val binding: BookingLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        return BookingViewHolder(
            BookingLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val currentItem = bookingList[position]
        val call = ServicesInstance.retrofitBuilder

        currentItem.serviceId?.let { call.getService(it).enqueue(object : Callback<Services> {
            override fun onResponse(call: Call<Services>, response: Response<Services>) {
                if(response.isSuccessful){
                    var service = response.body()
                    val call = UserInstance.retrofitBuilder

                    currentItem.customerId?.let { it1 -> call.getUser(it1).enqueue(object: Callback<User>{
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            if(response.isSuccessful){
                                var user = response.body()
                                holder.binding.apply{
                                    if (user != null) {
                                        tvCustomerName.text = "Customer Name: ${user.firstName} ${user.lastName}"
                                    }
                                    tvDateCustomer.text = "Date: ${currentItem.bookingDatetime}"
                                    if (service != null) {
                                        tvServiceCustomer.text = "Service Name: ${service.name}"
                                    }
                                    tvSetPriceCustomer.text = "Amount: ${currentItem.amount}"
                                }
                                holder.binding.btnPayServiceFee.setOnClickListener() {
                                    val intent = Intent(context, PaymentActivity::class.java)
                                    intent.putExtra("negotiationId", currentItem.negotiationId)
                                    intent.putExtra("email", email)
                                    context.startActivity(intent)
                                }

                                if(status == 2 || status == 3){
                                    holder.binding.btnPayServiceFee.visibility = View.GONE
                                }


                            }
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    }) }

                }
            }

            override fun onFailure(call: Call<Services>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
        }

    }
}