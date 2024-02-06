package com.example.gotrabahomobile.Remote.UserRemote


import com.example.gotrabahomobile.Model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserInterface {

    @GET("api/User")
    fun getUsers(): Call<List<User>>
    @GET("api/User/{userId}")
    fun getUserId(@Path("userId") userId: Int): Call<User>
    @GET("api/User/bookings/{userId}")
    fun getUserBookings(@Path("userId") userId: Int): Call<User>
    @POST("api/User")
    fun registerUser(@Body request: User): Call<User>

    @PUT("api/User{userId}")
    fun updateUser(@Path("userId") resourceId: String, @Body updatedResource: User): Call<User>

    @DELETE("api/User/{userId}")
    fun deleteUser(@Path("userId") resourceId: String): Call<User>

}