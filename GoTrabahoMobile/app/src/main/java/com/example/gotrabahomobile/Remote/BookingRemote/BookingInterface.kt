package com.example.gotrabahomobile.Remote.BookingRemote

import com.example.gotrabahomobile.Model.Booking
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BookingInterface {

    @GET("api/Booking")
    fun getBookings(): Call<List<Booking>>
    @GET("api/Booking/{bookingId}")
    fun getBooking(@Path("bookingId") bookingId: Int): Call<Booking>

    @GET("api/Booking/freelancer/{freelancerId}/{bookingstatus}")
    fun getBookingStatus(): Call<List<Booking>>

    @POST("api/Booking")
    fun insertBooking(@Body request: Booking): Call<Booking>

    @PUT("api/Booking/{bookingId}")
    fun updateBooking(@Path("bookingId") resourceId: String, @Body updatedResource: Booking): Call<Booking>

    @DELETE("api/Booking/{bookingId}")
    fun deleteBooking(@Path("bookingId") resourceId: String): Call<Booking>
}