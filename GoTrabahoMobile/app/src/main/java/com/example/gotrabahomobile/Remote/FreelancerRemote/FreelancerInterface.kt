package com.example.gotrabahomobile.Remote.FreelancerRemote

import com.example.gotrabahomobile.DTO.FreelancerDTO
import com.example.gotrabahomobile.Model.Booking
import com.example.gotrabahomobile.Model.Freelancer
import com.example.gotrabahomobile.Model.FreelancerTesdaCertificate
import com.example.gotrabahomobile.Model.proofOfExperience
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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
    fun getBookings(@Path("freelancerId") freelancerId: Int): Call<Booking>

    @GET("api/Freelancer/certificates/{freelancerId}")
    fun getCertificates(@Path("freelancerId") freelancerId: Int): Call<FreelancerTesdaCertificate>

    @GET("api/Freelancer/proof/{freelancerId}")
    fun getProofs(@Path("proofId") proofId: Int): Call<proofOfExperience>


    @Multipart
    @POST("api/Freelancer/CreateFreelancer")
    fun createFreelancer(
        @Part("userId") userId: Int,
        @Part imageFile: MultipartBody.Part,
        @Part("freelancerDTO") freelancerDTO: RequestBody
    ): Call<Freelancer>


    @POST("CreateProof")
    fun insertProof(@Body request: proofOfExperience): Call<proofOfExperience>

    @PUT("api/Freelancer/{freelancerId}")
    fun updateFreelancer(@Path("freelancerId") resourceId: String, @Body updatedResource: Freelancer): Call<Freelancer>
    @PUT("UpdateProof/{proofId")
    fun updateProof(@Path("proofId") resourceId: String, @Body updatedResource: proofOfExperience): Call<proofOfExperience>
    @PUT("UpdateCertificate/{tesdaId}")
    fun updateTesda(@Path("tesdaId") resourceId: String, @Body updatedResource: FreelancerTesdaCertificate): Call<FreelancerTesdaCertificate>
    @DELETE("api/Freelancer/{freelancerId}")
    fun deleteFreelancer(@Path("freelancerId") resourceId: String): Call<Freelancer>
    @DELETE("DeleteCertificate/{tesdaId}")
    fun deleteTesda(@Path("tesdaId") resourceId: String): Call<FreelancerTesdaCertificate>
    @DELETE("DeleteProof/{proofId}")
    fun deleteProof(@Path("proofId") resourceId: String): Call<proofOfExperience>

}