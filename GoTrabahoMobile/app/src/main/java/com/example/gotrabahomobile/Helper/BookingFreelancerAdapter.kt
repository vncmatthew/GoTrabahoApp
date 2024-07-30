package com.example.gotrabahomobile.Helper

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.Model.Booking
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.PaymentActivity
import com.example.gotrabahomobile.R
import com.example.gotrabahomobile.Remote.BookingRemote.BookingInstance
import com.example.gotrabahomobile.Remote.NegotiationRemote.NegotiationInstance
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance

import com.example.gotrabahomobile.databinding.BookingLayoutBinding
import com.google.firebase.database.FirebaseDatabase
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.format.DateTimeFormatter

class BookingFreelancerAdapter(private val bookingList: List<Booking>, private val context: Context, private val email: String?, private val status: Int): RecyclerView.Adapter<BookingFreelancerAdapter.BookingViewHolder>() {

    val CHANNEL_ID ="channelID"
    val CHANNEL_NAME ="channelName"
    val NOTIF_ID = 0
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
        createNotifChannel()
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
                                                            notifMessage("The booking has been completed", "Thank you for your service")
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
                val booking = response.body()
                if (response.isSuccessful) {
                    val nego = NegotiationInstance.retrofitBuilder
                    nego.deleteNegotiation(response.body()?.negotiationId)
                        .enqueue(object : retrofit2.Callback<ResponseBody> {
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                if (response.isSuccessful) {
                                    val tracker = "nego" + booking?.customerId + booking?.serviceId
                                    deleteChatroom(tracker) { success ->
                                        if (success) {
                                            Log.d("Success","Chatroom deleted successfully")
                                        } else {
                                            Log.d("Failed","Failed to delete chatroom")
                                        }
                                    }
                                    Log.d("Negotiation", "Successfully Deleted")
                                }
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    Log.d("Negotiation Error", "{$t}")
                            }
                        })
                }
            }

            override fun onFailure(call: Call<Booking>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun notifMessage(Title:String, Text:String){
        val intentT =Intent(context, context::class.java)
        val pendingIntent = TaskStackBuilder.create(context).run{
            addNextIntentWithParentStack(intentT)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(Title)
            .setContentText(Text)
            .setSmallIcon(R.drawable.logo_blue_noname)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        val notifManger = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notifManger.notify(NOTIF_ID,notif)
    }


    private fun createNotifChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
                lightColor = Color.BLUE
                enableLights(true)
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }


    fun deleteChatroom(chatroomId: String, callback: (Boolean) -> Unit) {
        val chatroomRef = FirebaseDatabase.getInstance().getReference("Chat").child(chatroomId)

        chatroomRef.removeValue()
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener { exception ->
                Log.w("Firebase", "Error deleting chatroom: ", exception)
                callback(false)
            }
    }


}