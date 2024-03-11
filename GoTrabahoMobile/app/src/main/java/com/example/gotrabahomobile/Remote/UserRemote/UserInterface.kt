package com.example.gotrabahomobile.Remote.UserRemote


import com.example.gotrabahomobile.Model.Login
import com.example.gotrabahomobile.Model.Negotiation
import com.example.gotrabahomobile.Model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserInterface {

    @GET("api/User")
    fun getUsers(): Call<List<User>>
    @GET("api/User/{userId}")
    fun getUser(@Path("userId") userId: Int): Call<User>
    @GET("api/User/bookings/{userId}")
    fun getUserBookings(@Path("userId") userId: Int): Call<User>
    @GET("api/User/GetEmail/{email}")
    fun getEmail(@Path("email") email: String): Call<Int>
    @GET("api/User/GetUserEmail/{email}")
    fun getUserEmail(@Path("email") email: String): Call<String>
    @POST("api/User")
    fun registerUser(@Body request: User): Call<User>
    @POST("api/Login")
    fun loginUser(@Body request: Login): Call<User>

    @PUT("api/User{userId}")
    fun updateUser(@Path("userId") resourceId: String, @Body updatedResource: User): Call<User>

    @DELETE("api/User/{userId}")
    fun deleteUser(@Path("userId") resourceId: String): Call<User>

    @PATCH("api/User/Patch/{userId}")
    fun patchUser(@Path("userId") resourceId: Int, @Body updatedResource: User): Call<User>


}