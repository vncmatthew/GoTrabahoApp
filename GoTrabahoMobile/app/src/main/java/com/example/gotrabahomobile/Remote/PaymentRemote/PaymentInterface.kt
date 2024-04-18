package com.example.gotrabahomobile.Remote.PaymentRemote

import com.example.gotrabahomobile.DTO.PaymentDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentInterface {
    @POST("/generate-invoice/{negotiationId}")
    fun paymentBook(@Body request: PaymentDTO, @Path("negotiationId") negotiationId: Int ): Call<ResponseBody>

    @POST("/generate-invoice-customer/{bookingId}")
    fun paymentBookCustomer(@Body request: PaymentDTO, @Path("bookingId") bookingId: Int ): Call<ResponseBody>


}
