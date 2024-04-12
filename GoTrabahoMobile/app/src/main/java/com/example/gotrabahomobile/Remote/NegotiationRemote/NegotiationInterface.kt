package com.example.gotrabahomobile.Remote.NegotiationRemote



import com.example.gotrabahomobile.Model.Negotiation
import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NegotiationInterface {

    @GET("api/Negotiation")
    fun getNegotiations(): Call<List<Negotiation>>

    @GET("api/Negotiation/{negotiationId}/PriceChecker")
    fun getPriceChecker(@Path("negotiationId") negotiationId: Int?): Call<Boolean>

    @GET("api/Negotiation/GetPrice/{negotiationId}")
    fun getNegotiationPrice(@Path("negotiationId") negotiationId: Int?): Call<Double>

    @GET("api/Negotiation/CheckStatus/{negotiationId}")
    fun getNegotiationStatus(@Path("negotiationId") negotiationId: Int?): Call<Negotiation>

    @GET("api/Negotiation/tracker/{tracker}")
    fun getNegotiationTracker(@Path("tracker") tracker: String?): Call<Negotiation>


    @POST("api/Negotiation")
    fun insertNegotiation(@Body request: Negotiation): Call<Negotiation>


    @DELETE("api/Negotiation/{negotiationId}")
    fun deleteNegotiation(@Path("negotiationId") resourceId: Int?): Call<ResponseBody>

    @PUT("api/Negotiation/{negotiationId}")
    fun updateNegotiation(@Path("negotiationId") resourceId: Int, @Body updatedResource: Negotiation): Call<Negotiation>

    @PATCH("api/Negotiation/Patch/{negotiationId}")
    fun patchNegotiation(@Path("negotiationId") resourceId: Int, @Body updatedResource: Negotiation): Call<Negotiation>

}