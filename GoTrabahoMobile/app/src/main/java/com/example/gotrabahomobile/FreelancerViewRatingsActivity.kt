package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gotrabahomobile.DTO.RatingListDTO
import com.example.gotrabahomobile.Helper.BookingUserAdapter
import com.example.gotrabahomobile.Helper.RatingListFreelancerAdapter
import com.example.gotrabahomobile.Remote.RatingRemote.RatingInstance
import com.example.gotrabahomobile.databinding.ActivityFreelancerViewRatingsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FreelancerViewRatingsActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityFreelancerViewRatingsBinding
    private lateinit var rvAdapter: RatingListFreelancerAdapter
    private lateinit var ratingList: List<RatingListDTO>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFreelancerViewRatingsBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        ratingList = listOf(


        )
        val backButton = findViewById<ImageButton>(R.id.back_buttonNavbar)

        backButton.setOnClickListener {
            finish()
        }

        getRatings()
    }

    private fun getRatings(){
        val freelancerId = intent.getIntExtra("freelancerId",0)
        val rating = RatingInstance.retrofitBuilder

        rating.getFreelancerRatings(freelancerId).enqueue(object: Callback<List<RatingListDTO>>{
            override fun onResponse(
                call: Call<List<RatingListDTO>>,
                response: Response<List<RatingListDTO>>
            ) {
                if(response.isSuccessful){
                    ratingList = response.body()!!

                    _binding.rvRating.apply {
                        rvAdapter = RatingListFreelancerAdapter(ratingList, this@FreelancerViewRatingsActivity)
                        adapter = rvAdapter
                        layoutManager = LinearLayoutManager(this@FreelancerViewRatingsActivity)
                    }
                }
            }

            override fun onFailure(call: Call<List<RatingListDTO>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}