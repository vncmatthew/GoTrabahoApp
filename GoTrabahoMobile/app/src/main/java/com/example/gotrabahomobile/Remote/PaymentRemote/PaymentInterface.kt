package com.example.gotrabahomobile.Remote.PaymentRemote

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentInterface {
    @POST("/generate-invoice")
    fun paymentBook(@Body request: String): Call<ResponseBody>


}