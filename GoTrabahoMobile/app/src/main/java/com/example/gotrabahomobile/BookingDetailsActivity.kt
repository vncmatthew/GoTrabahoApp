package com.example.gotrabahomobile

import android.app.AlertDialog
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.example.gotrabahomobile.Model.Rating
import com.example.gotrabahomobile.Remote.RatingRemote.RatingInstance
import retrofit2.Call
import retrofit2.Response
import java.sql.Date
import java.text.DateFormat
import java.time.LocalDate
import javax.security.auth.callback.Callback


class BookingDetailsActivity : AppCompatActivity() {
    private var ratingNumber = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)

        val rateButton = findViewById<Button>(R.id.buttonRate)




        rateButton.setOnClickListener {
            showRatingDialog()
        }
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
            alertDialog.dismiss()
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
        val newRating = Rating(
            star = ratingNumber.toBigDecimal(),
            dateRecorded = LocalDate.now()
            //customerId =

        )
        val call = RatingInstance.retrofitBuilder

        call.insertRating(newRating).enqueue(object: retrofit2.Callback<Rating>{
            override fun onResponse(call: Call<Rating>, response: Response<Rating>) {
                TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<Rating>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}