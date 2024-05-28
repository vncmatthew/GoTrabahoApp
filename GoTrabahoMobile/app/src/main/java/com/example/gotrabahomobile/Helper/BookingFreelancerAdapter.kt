package com.example.gotrabahomobile.Helper

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.BookingsFragment
import com.example.gotrabahomobile.FreelancerDetailsActivity
import com.example.gotrabahomobile.FreelancerEditServiceActivity
import com.example.gotrabahomobile.Model.Booking
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.PaymentActivity
import com.example.gotrabahomobile.Remote.BookingRemote.BookingInstance
import com.example.gotrabahomobile.Remote.NegotiationRemote.NegotiationInstance
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance

import com.example.gotrabahomobile.databinding.BookingLayoutBinding
import okhttp3.ResponseBody
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
            BookingLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val currentItem = bookingList[position]
        val call = ServicesInstance.retrofitBuilder

        currentItem.serviceId?.let {
            call.getService(it).enqueue(object : Callback<Services> {
                override fun onResponse(call: Call<Services>, response: Response<Services>) {
                    if (response.isSuccessful) {
                        var service = response.body()
                        val call = UserInstance.retrofitBuilder

                        currentItem.customerId?.let { it1 ->
                            call.getUser(it1).enqueue(object : Callback<User> {
                                override fun onResponse(
                                    call: Call<User>,
                                    response: Response<User>
                                ) {
                                    if (response.isSuccessful) {
                                        var user = response.body()
                                        holder.binding.apply {
                                            if (user != null) {
                                                tvCustomerName.text =
                                                    "Customer Name: ${user.firstName} ${user.lastName}"
                                            }
                                            tvDateCustomer.text =
                                                "Date: ${currentItem.bookingDatetime}"
                                            if (service != null) {
                                                tvServiceCustomer.text =
                                                    "Service Name: ${service.name}"
                                            }
                                            tvSetPriceCustomer.text =
                                                "Amount: ${currentItem.amount}"
                                        }
                                        holder.binding.btnPayServiceFee.setOnClickListener() {
                                            val intent =
                                                Intent(context, PaymentActivity::class.java)
                                            intent.putExtra(
                                                "negotiationId",
                                                currentItem.negotiationId
                                            )
                                            intent.putExtra("bookingId", currentItem.bookingId)
                                            intent.putExtra("email", email)
                                            context.startActivity(intent)
                                        }

                                        holder.binding.btnSetToCompleted.setOnClickListener {
                                            val book = BookingInstance.retrofitBuilder
                                            val updatedBook = Booking(
                                                bookingId = currentItem.bookingId,
                                                customerId = currentItem.customerId,
                                                bookingDatetime = currentItem.bookingDatetime,
                                                amount = currentItem.amount,
                                                bookingStatus = 3,
                                                serviceId = currentItem.serviceId,
                                                serviceFee = currentItem.serviceFee,
                                                negotiationId = null
                                            )
                                            book.updateBooking(
                                                currentItem.bookingId.toString(),
                                                updatedBook
                                            ).enqueue(
                                                object : retrofit2.Callback<ResponseBody> {
                                                    override fun onResponse(
                                                        call: Call<ResponseBody>,
                                                        response: Response<ResponseBody>
                                                    ) {
                                                        if (response.isSuccessful) {
                                                            Log.d(
                                                                "Booking",
                                                                "Successfully Updated to 4"
                                                            )
                                                        }
                                                    }

                                                    override fun onFailure(
                                                        call: Call<ResponseBody>,
                                                        t: Throwable
                                                    ) {
                                                        TODO("Not yet implemented")
                                                    }

                                                })

                                            deleteNego(currentItem.bookingId!!)
                                        }

                                        if (status == 1) {
                                            holder.binding.btnSetToCompleted.visibility = View.GONE
                                        }
                                        if (status == 2) {
                                            holder.binding.btnPayServiceFee.visibility = View.GONE
                                        }
                                        if (status == 3) {
                                            holder.binding.btnPayServiceFee.visibility = View.GONE
                                            holder.binding.btnSetToCompleted.visibility = View.GONE
                                        }


                                    }
                                }

                                override fun onFailure(call: Call<User>, t: Throwable) {
                                    TODO("Not yet implemented")
                                }

                            })
                        }

                    }
                }

                override fun onFailure(call: Call<Services>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }

    }

    private fun deleteNego(bookingId: Int) {
        val call = BookingInstance.retrofitBuilder
        call.getBooking(bookingId!!).enqueue(object : retrofit2.Callback<Booking> {
            override fun onResponse(call: Call<Booking>, response: Response<Booking>) {
                if (response.isSuccessful) {
                    val nego = NegotiationInstance.retrofitBuilder
                    nego.deleteNegotiation(response.body()?.negotiationId)
                        .enqueue(object : retrofit2.Callback<ResponseBody> {
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                if (response.isSuccessful) {
                                    Log.d("Negotiation", "Successfully Deleted")
                                }
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                TODO("Not yet implemented")
                            }
                        })
                }
            }

            override fun onFailure(call: Call<Booking>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}