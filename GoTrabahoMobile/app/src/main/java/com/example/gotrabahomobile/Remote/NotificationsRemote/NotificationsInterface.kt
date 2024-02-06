package com.example.gotrabahomobile.Remote.NotificationsRemote

import android.adservices.adid.AdId
import android.app.Notification
import com.example.gotrabahomobile.Model.Booking
import com.example.gotrabahomobile.Model.Freelancer
import com.example.gotrabahomobile.Model.Notifications
import com.example.gotrabahomobile.Model.proofOfExperience
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotificationsInterface {
    @GET("api/Notifications")
    fun getNotifications(): Call<List<Notifications>>

    @GET("api/Notifications/{notificationId}")
    fun getNotification(@Path("notificationId") notifcationId: Int): Call<Notifications>

    @POST("api/Notifications")
    fun insertNotification(@Body request: Notifications): Call<Notification>

    @PUT("api/Notifications/{notificationId}")
    fun updateNotification(@Path("notificationId") resourceId: String, @Body updatedResource: Notifications): Call<Notifications>

    @DELETE("api/Notifications/{notificationId}")
    fun deleteNotification(@Path("notificationId") resourceId: String): Call<Notifications>
}