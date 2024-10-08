package com.example.gotrabahomobile.Remote.ServicesRemote

import com.example.gotrabahomobile.DTO.FreelancerLocations
import com.example.gotrabahomobile.DTO.ServicesWUserId
import com.example.gotrabahomobile.DTO.SubService
import com.example.gotrabahomobile.DTO.SubServicesTypes
import com.example.gotrabahomobile.Model.Freelancer

import com.example.gotrabahomobile.Model.Services
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ServicesInterface {

    @GET("api/Services")
    fun getServices(): Call<List<Services>>

    @GET("api/Services/{serviceId}/rating")
    fun getServiceRating(@Path("serviceId") serviceId: Int): Call<Services>

    @GET("api/SubServicesType/SubServiceTypeListFreelancer/{subServiceType}/{serviceId}")
    fun getSubServiceList( @Path("subServiceType") subServiceType: String?, @Path("serviceId") serviceId: Int): Call<List<SubServicesTypes>>


    @GET("api/Services/FreelancerServices/{freelancerId}/{serviceTypeName}")
    fun getServiceIdByFreelancer(@Path("freelancerId") freelancerId: Int, @Path("serviceTypeName") serviceTypeName: String): Call<Services>

    @GET("api/Services/GetLocations/{serviceTypeName}")
    fun getServiceLocations(@Path("serviceTypeName") serviceTypeName: String): Call<List<FreelancerLocations>>

    @GET("api/Services/ServiceList/{freelancerId}")
    fun getFreelancerServices(@Path("freelancerId") freelancerId: Int): Call<List<Services>>

    @PATCH("api/Services/Patch")
    fun patchServices(@Body updatedResource: Services): Call<Services>

    @GET("api/Services/{serviceId}")
    fun getService(@Path("serviceId") serviceId: Int): Call<Services>

    @GET("api/Services/freelancer/{serviceTypeName}")
    fun getServicesFreelancer(@Path("serviceTypeName") serviceTypeName: String): Call<Services>

    @POST("api/Services")
    fun insertService(@Body request: Services): Call<Services>

    @PUT("api/Services/{serviceId}")
    fun updateServices(@Path("serviceId") resourceId: String, @Body updatedResource: Services): Call<Services>

    @DELETE("api/Services/CustomDelete/{serviceId}")
    fun deleteService(@Path("serviceId") resourceId: Int): Call<Services>

    @GET("api/Services/List/{serviceTypeName}")
    fun getServicesType(@Path("serviceTypeName") serviceTypeName: String?): Call<List<ServicesWUserId>>
    @GET("api/SubServicesType/{subServiceType}")
    fun getSubServicesPerService(@Path("subServiceType") subServiceType: String?): Call<List<SubServicesTypes>>

    @GET("api/SubServicesType/GetServicesPerSubService/{subService}")
    fun getServicesPerSubService(@Path("subService") subService: String?): Call<List<ServicesWUserId>>

    @GET("api/Services/ServiceTypeDiscount/{serviceTypeName}")
    fun getAverageAmountPerService(@Path("serviceTypeName") serviceTypeName: String?): Call<Double>

    @POST("api/SubServices")
    fun insertSubService(@Body request: SubService): Call<SubService>

}