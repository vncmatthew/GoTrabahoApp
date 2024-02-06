package com.example.gotrabahomobile.Remote.BookingRemote

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BookingInstance {
    val gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl("http://10.0.2.2:5179/")
        .build()
        .create(BookingInterface::class.java)
}
