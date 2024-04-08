package com.example.gotrabahomobile.Remote.BookingRemote

import com.example.gotrabahomobile.DTO.BookingUserDTO
import com.example.gotrabahomobile.Model.Booking
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BookingInterface {

    @GET("api/Booking")
    fun getBookings(): Call<List<Booking>>
    @GET("api/Booking/{bookingId}")
    fun getBooking(@Path("bookingId") bookingId: Int): Call<Booking>

    @GET("api/Booking/BookingUser/{bookingId}")
    fun getUserBookings(@Path("bookingId") bookingId: Int): Call<List<BookingUserDTO>>

    @GET("api/Booking/freelancer/{freelancerId}/{bookingStatus}/{serviceTypeName}")
    fun getBookingStatus(
        @Path("freelancerId") freelancerId: Int,
        @Path("bookingStatus") bookingStatus: Int,
        @Path("serviceTypeName") serviceTypeName: String
    ): Call<List<Booking>>

    @GET("api/Booking/BookingUser/{userId}/{bookingStatus}")
    fun getCompletedBooking(
        @Path("userId") userId: Int,
        @Path("bookingStatus") bookingStatus: Int,
    ): Call<Int>

    @POST("api/Booking")
    fun insertBooking(@Body request: Booking): Call<ResponseBody>


    @PATCH("api/Booking/{id}/status/{status}")
    fun updateBookingStatus(@Path("id") bookingId: Int, @Path("status") newStatus: Int): Call<Void>

    @PUT("api/Booking/{bookingId}")
    fun updateBooking(@Path("bookingId") resourceId: String, @Body updatedResource: Booking): Call<Booking>

    @DELETE("api/Booking/{bookingId}")
    fun deleteBooking(@Path("bookingId") resourceId: String): Call<Booking>
}