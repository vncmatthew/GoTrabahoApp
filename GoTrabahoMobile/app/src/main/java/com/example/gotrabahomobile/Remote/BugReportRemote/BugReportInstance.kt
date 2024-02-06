package com.example.gotrabahomobile.Remote.BugReportRemote

import com.example.gotrabahomobile.Remote.BookingRemote.BookingInterface
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BugReportInstance {
    val gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl("http://10.0.2.2:5179/")
        .build()
        .create(BugReportInterface::class.java)
}