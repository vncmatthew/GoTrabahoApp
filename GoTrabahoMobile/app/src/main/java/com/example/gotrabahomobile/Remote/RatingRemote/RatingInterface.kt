package com.example.gotrabahomobile.Remote.RatingRemote

import com.example.gotrabahomobile.Model.Notifications
import com.example.gotrabahomobile.Model.Rating
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RatingInterface {
    @GET("api/Rating")
    fun getRatings(): Call<List<Rating>>

    @POST("api/Rating")
    fun insertRating(@Body request: Rating): Call<ResponseBody>

    @DELETE("api/Rating/{ratingId}")
    fun deleteRating(@Path("ratingId") resourceId: String): Call<Rating>
}