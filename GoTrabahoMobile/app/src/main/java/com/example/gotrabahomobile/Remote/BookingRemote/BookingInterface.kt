package com.example.gotrabahomobile.Remote.BookingRemote

import com.example.gotrabahomobile.DTO.BookingByServiceDTO
import com.example.gotrabahomobile.DTO.BookingUserDTO
import com.example.gotrabahomobile.DTO.ServiceDetails
import com.example.gotrabahomobile.Model.Booking
import com.example.gotrabahomobile.Model.BookingSummary
import com.example.gotrabahomobile.Model.Services
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

    @GET("api/Booking/ServiceDetails/{bookingId}")
    fun getServiceDetails(@Path("bookingId") bookingId: Int): Call<ServiceDetails>
    
    @GET("api/Booking/BookingUser/{bookingId}")
    fun getUserBookings(@Path("bookingId") bookingId: Int): Call<List<BookingUserDTO>>

    @GET("api/Booking/freelancer/{freelancerId}/{bookingStatus}/{serviceTypeName}")
    fun getBookingStatus(
        @Path("freelancerId") freelancerId: Int,
        @Path("bookingStatus") bookingStatus: Int,
        @Path("serviceTypeName") serviceTypeName: String
    ): Call<List<Booking>>


    @GET("api/Booking/BookingSummary/{bookingId}")
    fun getBookingSummary(
        @Path("bookingId") bookingId: Int
    ): Call<BookingSummary>

    @GET("api/Booking/bookingCount/{customerId}")
    fun getBookingDiscount(
        @Path("customerId") customerId: Int
    ): Call<Boolean>

    @GET("api/Booking/BookingUser/{userId}/{bookingStatus}")
    fun getCompletedBooking(
        @Path("userId") userId: Int,
        @Path("bookingStatus") bookingStatus: Int,
    ): Call<Int>

    @POST("api/Booking")
    fun insertBooking(@Body request: Booking): Call<Booking>


    @PATCH("api/Booking/{id}/status/{status}")
    fun updateBookingStatus(@Path("id") bookingId: Int, @Path("status") newStatus: Int): Call<Void>

    @PUT("api/Booking/{bookingId}")
    fun updateBooking(@Path("bookingId") resourceId: String, @Body updatedResource: Booking): Call<ResponseBody>

    @DELETE("api/Booking/{bookingId}")
    fun deleteBooking(@Path("bookingId") resourceId: Int): Call<Booking>
}