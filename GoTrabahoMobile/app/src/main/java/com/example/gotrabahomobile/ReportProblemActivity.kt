package com.example.gotrabahomobile

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import com.example.gotrabahomobile.Model.Booking
import com.example.gotrabahomobile.Model.Rating
import com.example.gotrabahomobile.Model.ReportBooking
import com.example.gotrabahomobile.Remote.BookingRemote.BookingInstance
import com.example.gotrabahomobile.Remote.RatingRemote.RatingInstance
import com.example.gotrabahomobile.Remote.ReportBookingRemote.ReportBookingInstance
import com.example.gotrabahomobile.databinding.ActivityFreelancerMainBinding
import com.example.gotrabahomobile.databinding.ActivityReportProblemBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime

class ReportProblemActivity : AppCompatActivity() {


    private lateinit var binding: ActivityReportProblemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportProblemBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonReportProblemCancel.setOnClickListener{
            finish()
        }

        binding.buttonReportProblemSubmit.setOnClickListener{
            sendReport()
        }


    }


    private fun sendReport(){
        val edit_Title = binding.editTextReportProblemTitle
        val edit_Desc = binding.editTextReportProblemDesc

        val title = edit_Title.text.toString()
        val desc = edit_Desc.text.toString()

        val call = BookingInstance.retrofitBuilder
        val bookingId = intent.getIntExtra("bookingId", 0)
        call.getBooking(bookingId).enqueue(object: retrofit2.Callback<Booking>{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<Booking>, response: Response<Booking>) {
                if(response.isSuccessful){
                    val newReport = ReportBooking(
                        userId = response.body()?.customerId,
                        title = title,
                        description = desc,
                        timeStamp = LocalDateTime.now().toString(),
                        status = true,
                        bookingId = response.body()?.bookingId
                    )
                    val call = ReportBookingInstance.retrofitBuilder
                    call.insertReportBooking(newReport).enqueue(object: retrofit2.Callback<ResponseBody>{
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if(response.isSuccessful){
                                Log.d("Success", "${response.body()}")
                                val intent = Intent(this@ReportProblemActivity, CustomerMainActivity::class.java)
                                startActivity(intent)
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