package com.example.gotrabahomobile.Remote.FreelancerRemote

import com.example.gotrabahomobile.DTO.FreelancerDTO
import com.example.gotrabahomobile.DTO.FreelancerDashboard
import com.example.gotrabahomobile.Model.Booking
import com.example.gotrabahomobile.Model.Freelancer
import com.example.gotrabahomobile.Model.FreelancerTesdaCertificate
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.Model.proofOfExperience
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface FreelancerInterface {
    @GET("api/Freelancer/FreelancerId/{userId}")
    fun getFreelancerId(@Path("userId") userId: Int?): Call<Freelancer>

    @Multipart
    @POST("api/Freelancer/CreateFreelancerGovernment")
    fun insertGovernment(
        @Part imageFile: MultipartBody.Part
    ): Call<Freelancer>

    @POST("api/Freelancer/CreateFreelancer")
    fun insertFreelancer(@Body request: Freelancer): Call<Freelancer>


    @POST("api/Freelancer/CreateCertificate")
    fun insertCertificate(@Body request: FreelancerTesdaCertificate): Call<FreelancerTesdaCertificate>


    @PATCH("api/Freelancer/Patch/{freelancerId}")
    fun patchFreelancer(@Path("freelancerId") resourceId: Int, @Body updatedResource: Freelancer): Call<Freelancer>
    @Multipart
    @POST("api/Freelancer/CreateFreelancerCertificate")
    fun insertCertificateImage(
        @Part imageFile: MultipartBody.Part
    ): Call<FreelancerTesdaCertificate>
    @POST("api/Freelancer/CreateProof")
    fun insertProof(@Body request: proofOfExperience): Call<proofOfExperience>

    @Multipart
    @POST("api/Freelancer/CreateFreelancerProof")
    fun insertProofImage(
        @Part imageFile: MultipartBody.Part
    ): Call<proofOfExperience>

    @GET("api/Freelancer/FreelancerDashboard/{freelancerId}")
    fun getDashboard(@Path("freelancerId") freelancerId: Int): Call<FreelancerDashboard>
}