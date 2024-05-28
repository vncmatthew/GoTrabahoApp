package com.example.gotrabahomobile

import android.app.AlertDialog
import android.content.Intent
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.gotrabahomobile.DTO.ServiceDetails
import com.example.gotrabahomobile.Model.Booking
import com.example.gotrabahomobile.Model.Rating
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.Remote.BookingRemote.BookingInstance
import com.example.gotrabahomobile.Remote.NegotiationRemote.NegotiationInstance
import com.example.gotrabahomobile.Remote.RatingRemote.RatingInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.sql.Date
import java.text.DateFormat
import java.time.LocalDate
import javax.security.auth.callback.Callback


class BookingDetailsActivity : AppCompatActivity() {
    private var ratingNumber = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)

        //buttons 1
        val rateButton = findViewById<Button>(R.id.buttonRate)
        val messageButton = findViewById<Button>(R.id.buttonMessage)
        //buttons 2
        val cancelBookingButton = findViewById<Button>(R.id.buttonCancelBooking)
        val paymentButton = findViewById<Button>(R.id.buttonPayment)
        val reportBookingButton = findViewById<Button>(R.id.buttonReport)


        getDetails()
        bookingStatus()

        val backButton: ImageButton = findViewById(R.id.back_buttonNavbar)
        backButton.setOnClickListener{
            finish()
        }
        rateButton.setOnClickListener {
            showRatingDialog()
        }

        messageButton.setOnClickListener {

        }

        cancelBookingButton.setOnClickListener {
            showCancelServiceDialog()
        }

        paymentButton.setOnClickListener {
            getPayment()
        }

        reportBookingButton.setOnClickListener {
            val bookingId = intent.getIntExtra("bookingId", 0)
            val intent = Intent(this, ReportProblemActivity::class.java)
            intent.putExtra("bookingId", bookingId)
            startActivity(intent)
        }
    }

    private fun bookingStatus() {
        //bookingStatus 0 - Pending, 1 - Ongoing, 2 - Service Done awaiting payment, 3 - Payment Done

        //buttons 1
        val rateButton = findViewById<Button>(R.id.buttonRate)
        val messageButton = findViewById<Button>(R.id.buttonMessage)
        //buttons 2
        val cancelBookingButton = findViewById<Button>(R.id.buttonCancelBooking)
        val paymentButton = findViewById<Button>(R.id.buttonPayment)
        val reportBookingButton = findViewById<Button>(R.id.buttonReport)

        val bookingId = intent.getIntExtra("bookingId", 0)
        val call = BookingInstance.retrofitBuilder
        call.getBooking(bookingId).enqueue(object: retrofit2.Callback<Booking>{
            override fun onResponse(call: Call<Booking>, response: Response<Booking>) {
                if(response.isSuccessful)
                {
                    when (response.body()?.bookingStatus) {
                        0 -> { //Pending
                            rateButton.visibility = View.GONE
                            messageButton.visibility = View.VISIBLE
                            cancelBookingButton.visibility = View.VISIBLE
                            paymentButton.visibility = View.GONE
                            reportBookingButton.visibility = View.GONE
                        }
                        1 -> { //Ongoing
                            rateButton.visibility = View.GONE
                            messageButton.visibility = View.GONE
                            cancelBookingButton.visibility = View.GONE
                            paymentButton.visibility = View.GONE
                            reportBookingButton.visibility = View.GONE
                        }
                        2 -> { //Service Done awaiting payment
                            rateButton.visibility = View.GONE
                            messageButton.visibility = View.GONE
                            cancelBookingButton.visibility = View.GONE
                            paymentButton.visibility = View.VISIBLE
                            reportBookingButton.visibility = View.GONE
                        }
                        3 -> { //Payment Done
                            rateButton.visibility = View.VISIBLE
                            messageButton.visibility = View.GONE
                            cancelBookingButton.visibility = View.GONE
                            paymentButton.visibility = View.GONE
                            reportBookingButton.visibility = View.VISIBLE
                        }
                        else -> {
                            rateButton.visibility = View.GONE
                            messageButton.visibility = View.GONE
                            cancelBookingButton.visibility = View.GONE
                            paymentButton.visibility = View.GONE
                            reportBookingButton.visibility = View.GONE
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Booking>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun showCancelServiceDialog() {
        val cancelServiceView = LayoutInflater.from(this).inflate(R.layout.dialog_cancel_service, null)

        val closeButton = cancelServiceView.findViewById<ImageView>(R.id.close_button)
        val proceedButton = cancelServiceView.findViewById<Button>(R.id.buttonCancelServiceProceed)

        val alertDialog = AlertDialog.Builder(this)
            .setView(cancelServiceView)
            .create()

        closeButton.setOnClickListener {
            alertDialog.dismiss()
        }

        proceedButton.setOnClickListener {
            alertDialog.dismiss()
            showConfirmCancelDialog()
        }

        alertDialog.show()
    }

    private fun showConfirmCancelDialog() {
        val confirmCancelServiceView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_cancelservice, null)

        val noButton = confirmCancelServiceView.findViewById<Button>(R.id.buttonNoCancelBooking)
        val yesButton = confirmCancelServiceView.findViewById<Button>(R.id.buttonYesCancelBooking)

        val alertDialog = AlertDialog.Builder(this)
            .setView(confirmCancelServiceView)
            .create()

        noButton.setOnClickListener {
            alertDialog.dismiss()
        }

        yesButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showRatingDialog() {
        val ratingView = LayoutInflater.from(this).inflate(R.layout.dialog_rating, null)

        val closeButton = ratingView.findViewById<ImageButton>(R.id.close_button)
        val submitButton = ratingView.findViewById<Button>(R.id.buttonRateSubmit)



        val alertDialog = AlertDialog.Builder(this)
            .setView(ratingView)
            .create()

        closeButton.setOnClickListener {
            alertDialog.dismiss()
        }

        submitButton.setOnClickListener {
            submitRatingToDatabase(ratingNumber)
            //deleteNego()
            alertDialog.dismiss()
            finish()
        }

        alertDialog.show()

        //stars
        val star1 = ratingView.findViewById<ImageView>(R.id.star1)
        val star2 = ratingView.findViewById<ImageView>(R.id.star2)
        val star3 = ratingView.findViewById<ImageView>(R.id.star3)
        val star4 = ratingView.findViewById<ImageView>(R.id.star4)
        val star5 = ratingView.findViewById<ImageView>(R.id.star5)

        star1.setOnClickListener{ handleStarClick(ratingView, 1)}
        star2.setOnClickListener{ handleStarClick(ratingView, 2)}
        star3.setOnClickListener{ handleStarClick(ratingView, 3)}
        star4.setOnClickListener{ handleStarClick(ratingView, 4)}
        star5.setOnClickListener{ handleStarClick(ratingView, 5)}
    }

    private fun handleStarClick(ratingView: View, starNumber: Int) {
        ratingNumber = starNumber
        updateStars(ratingView, starNumber)
    }

    private fun updateStars(ratingView: View, selectedStarNumber: Int) {

        //stars
        val star1 = ratingView.findViewById<ImageView>(R.id.star1)
        val star2 = ratingView.findViewById<ImageView>(R.id.star2)
        val star3 = ratingView.findViewById<ImageView>(R.id.star3)
        val star4 = ratingView.findViewById<ImageView>(R.id.star4)
        val star5 = ratingView.findViewById<ImageView>(R.id.star5)

        when (selectedStarNumber) {
            1 -> {
                star1.setImageResource(R.drawable.star_filled)
                star2.setImageResource(R.drawable.star_notfilled)
                star3.setImageResource(R.drawable.star_notfilled)
                star4.setImageResource(R.drawable.star_notfilled)
                star5.setImageResource(R.drawable.star_notfilled)
            }
            2 -> {
                star1.setImageResource(R.drawable.star_filled)
                star2.setImageResource(R.drawable.star_filled)
                star3.setImageResource(R.drawable.star_notfilled)
                star4.setImageResource(R.drawable.star_notfilled)
                star5.setImageResource(R.drawable.star_notfilled)
            }
            3 -> {
                star1.setImageResource(R.drawable.star_filled)
                star2.setImageResource(R.drawable.star_filled)
                star3.setImageResource(R.drawable.star_filled)
                star4.setImageResource(R.drawable.star_notfilled)
                star5.setImageResource(R.drawable.star_notfilled)
            }
            4 -> {
                star1.setImageResource(R.drawable.star_filled)
                star2.setImageResource(R.drawable.star_filled)
                star3.setImageResource(R.drawable.star_filled)
                star4.setImageResource(R.drawable.star_filled)
                star5.setImageResource(R.drawable.star_notfilled)
            }
            5 -> {
                star1.setImageResource(R.drawable.star_filled)
                star2.setImageResource(R.drawable.star_filled)
                star3.setImageResource(R.drawable.star_filled)
                star4.setImageResource(R.drawable.star_filled)
                star5.setImageResource(R.drawable.star_filled)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun submitRatingToDatabase(ratingNumber: Int) {

        val call = BookingInstance.retrofitBuilder
        val bookingId = intent.getIntExtra("bookingId", 0)
        call.getBooking(bookingId).enqueue(object: retrofit2.Callback<Booking>{
            override fun onResponse(call: Call<Booking>, response: Response<Booking>) {
                if(response.isSuccessful){
                    val newRating = Rating(
                        star = ratingNumber.toBigDecimal(),
                        bookingId = response.body()?.bookingId?.toInt(),

                        dateRecorded = LocalDate.now().toString(),
                        customerId = response.body()?.customerId
                    )
                    val call = RatingInstance.retrofitBuilder
                    call.insertRating(newRating).enqueue(object: retrofit2.Callback<ResponseBody>{
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                             if(response.isSuccessful){
                                Log.d("Success", "${response.body()}")
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })

                    val book = BookingInstance.retrofitBuilder
                    val updatedBook = Booking(
                        bookingId = response.body()?.bookingId,
                        customerId = response.body()?.customerId,
                        bookingDatetime = response.body()?.bookingDatetime,
                        amount = response.body()?.amount,
                        bookingStatus = 4,
                        serviceId = response.body()!!.serviceId,
                        serviceFee = response.body()?.serviceFee,
                        negotiationId = null
                    )
                    book.updateBooking(response.body()?.bookingId.toString(), updatedBook).enqueue(object: retrofit2.Callback<ResponseBody>{
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            if(response.isSuccessful){
                                Log.d("Booking", "Successfully Updated to 4")
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

    /*private fun deleteNego(){

        val call = BookingInstance.retrofitBuilder
        val bookingId = intent.getIntExtra("bookingId", 0)
        call.getBooking(bookingId).enqueue(object: retrofit2.Callback<Booking>{
            override fun onResponse(call: Call<Booking>, response: Response<Booking>) {
                if(response.isSuccessful){
                    val nego = NegotiationInstance.retrofitBuilder
                    nego.deleteNegotiation(response.body()?.negotiationId).enqueue(object: retrofit2.Callback<ResponseBody>{
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if(response.isSuccessful){
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
    }*/

    private fun getPayment(){

        val bookingId = intent.getIntExtra("bookingId", 0)
        val email = intent.getStringExtra("email")
        val book = BookingInstance.retrofitBuilder
        book.getBooking(bookingId).enqueue(object: retrofit2.Callback<Booking>{
            override fun onResponse(call: Call<Booking>, response: Response<Booking>) {
                if(response.isSuccessful){
                    val intent = Intent(this@BookingDetailsActivity, PaymentActivity::class.java)
                    intent.putExtra("bookingId", bookingId)
                    intent.putExtra("email", email)

                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<Booking>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getDetails(){

        val tvName = findViewById<TextView>(R.id.textViewBookingDetailsFreelancerName)
        val tvService = findViewById<TextView>(R.id.textViewService)
        val tvDesc = findViewById<TextView>(R.id.textViewDescription)
        val tvPrice = findViewById<TextView>(R.id.textViewPriceDesc)
        val tvLoc = findViewById<TextView>(R.id.textViewLocationDesc)
        val tvRating = findViewById<TextView>(R.id.textViewRatingDesc)
        val tvDate = findViewById<TextView>(R.id.textViewDateBookedDesc)
        val tvStatus = findViewById<TextView>(R.id.textViewStatusDesc)

        val book = BookingInstance.retrofitBuilder
        val bookingId = intent.getIntExtra("bookingId", 0)
        book.getBooking(bookingId).enqueue(object: retrofit2.Callback<Booking>{
            override fun onResponse(call: Call<Booking>, response: Response<Booking>) {
                if(response.isSuccessful){
                    tvPrice.text = response.body()?.amount.toString()
                    tvDate.text = response.body()?.bookingDatetime
                    if(response.body()?.bookingStatus == 1){
                        tvStatus.text = "Pending"
                    }
                    else if(response.body()?.bookingStatus == 2){
                        tvStatus.text = "Ongoing"
                    }
                    else if(response.body()?.bookingStatus == 3 || response.body()?.bookingStatus == 4){
                        tvStatus.text = "Completed"
                    }
                }
            }

            override fun onFailure(call: Call<Booking>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

        book.getServiceDetails(bookingId).enqueue(object: retrofit2.Callback<ServiceDetails>{
            override fun onResponse(call: Call<ServiceDetails>, response: Response<ServiceDetails>) {
                if(response.isSuccessful){
                    tvName.text = response.body()?.freelancerName
                    tvService.text = response.body()?.name
                    tvDesc.text = response.body()?.description
                    tvLoc.text = response.body()?.location
                    tvRating.text = response.body()?.rating.toString()

                }
            }

            override fun onFailure(call: Call<ServiceDetails>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

}