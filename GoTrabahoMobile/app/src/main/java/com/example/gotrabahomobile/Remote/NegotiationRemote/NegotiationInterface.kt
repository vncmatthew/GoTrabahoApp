package com.example.gotrabahomobile.Remote.NegotiationRemote



import com.example.gotrabahomobile.DTO.NegotiationResponse
import com.example.gotrabahomobile.Model.Freelancer
import com.example.gotrabahomobile.Model.Negotiation
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NegotiationInterface {

    @GET("api/Negotiation")
    fun getNegotiation(): Call<List<Negotiation>>

    @GET("api/Negotiation/CheckStatus/{negotiationId")
    fun getNegotiationStatus(@Path("negotiationId") negotiationId: Int?): Call<Negotiation>

    @POST("api/Negotiation")
    fun insertNegotiation(@Body request: Negotiation): Call<Negotiation>

    @DELETE("api/Negotiation/{negotiationId}")
    fun deleteNegotiation(@Path("negotiationId") resourceId: String): Call<Negotiation>

    @PUT("api/Negotiation/{negotiationId}")
    fun updateNegotiation(@Path("negotiationId") resourceId: String, @Body updatedResource: Negotiation): Call<Negotiation>

}