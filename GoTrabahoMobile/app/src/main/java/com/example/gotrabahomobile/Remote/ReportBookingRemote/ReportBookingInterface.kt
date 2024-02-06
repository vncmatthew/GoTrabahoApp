package com.example.gotrabahomobile.Remote.ReportBookingRemote

import com.example.gotrabahomobile.Model.ReportBooking
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReportBookingInterface {

    @GET("api/ReportBooking")
    fun getReportBookings(): Call<List<ReportBooking>>

    @GET("api/ReportBooking/{reportId}")
    fun getReportBooking(@Path("reportId") reportId: Int): Call<ReportBooking>

    @POST("api/ReportBooking")
    fun insertReportBooking(@Body request: ReportBooking): Call<ReportBooking>

    @PUT("api/ReportBooking/{reportId}")
    fun updateReportBooking(@Path("reportId") resourceId: String, @Body updatedResource: ReportBooking): Call<ReportBooking>

    @DELETE("api/ReportBooking/{reportId}")
    fun deleteReportBooking(@Path("reportId") resourceId: String): Call<ReportBooking>

}