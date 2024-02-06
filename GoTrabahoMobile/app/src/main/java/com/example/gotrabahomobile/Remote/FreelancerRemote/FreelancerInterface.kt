package com.example.gotrabahomobile.Remote.FreelancerRemote

import com.example.gotrabahomobile.Model.Freelancer
import com.example.gotrabahomobile.Model.FreelancerTesdaCertificate
import com.example.gotrabahomobile.Model.proofOfExperience
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.Call
import retrofit2.http.Path

interface FreelancerInterface {
    @GET("api/Freelancer")
    fun getFreelancers(): Call<List<Freelancer>>

    @GET("api/Freelancer/GetCertificates")
    fun getCertificates(): Call<List<FreelancerTesdaCertificate>>

    @GET("api/Freelancer/GetProofs")
    fun getProofs(): Call<List<proofOfExperience>>

    @GET("api/Freelancer/{freelancerId}")
    fun getFreelancer(@Path("freelancerId") freelancerId: Int): Call<Freelancer>

    @GET("api/Freelancer/{tesdaId}")
    fun getTesda(@Path("tesdaID") tesdaId: Int): Call<FreelancerTesdaCertificate>

    @GET("api/Freelancer/{proofId}")
    fun getProof(@Path("proofId") proofId: Int): Call<proofOfExperience>

    @GET("api/Freelancer/bookings/{freelancerId}")


    @GET("api/Freelancer/certificates/{freelancerId}")


    @GET("api/Freelancer/proof/{freelancerId}")
    @POST("api/Freelancer/CreateFreelancer")
    @POST("api/Freelancer/CreateTesdaCertificate")
    @POST("api/Freelancer/CreateProof")
    @PUT("api/Freelancer/{freelancerId}")
    @PUT("api/Freelancer/{proofId")
    @PUT("api/Freelancer/{tesdaId}")
    @DELETE("api/Freelancer/{freelancerId}")
    @DELETE("api/Freelancer/{tesdaId}")
    @DELETE("api/Freelancer/{proofId}")
}