package com.example.gotrabahomobile.Remote.BugReportRemote

import com.example.gotrabahomobile.Model.BugReport
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Path

interface BugReportInterface {
    @GET("api/BugReport")
    fun getBugReports(): Call<List<BugReport>>

    @GET("api/BugReport/{bugId}")
    fun getBugReport(@Path("bugId") bugId: Int): Call<BugReport>

    @POST("api/BugReport")
    fun insertBugReport(@Body request: BugReport): Call<BugReport>

    @PUT("api/BugReport/{bugId}")
    fun updateBugReport(@Path("bugId") resourceId: String, @Body updatedResource:BugReport): Call<BugReport>

    @DELETE("api/BugReport/{bugReportId}")
    fun deleteBugReport(@Path("bugId") resourceId: String): Call<BugReport>

}