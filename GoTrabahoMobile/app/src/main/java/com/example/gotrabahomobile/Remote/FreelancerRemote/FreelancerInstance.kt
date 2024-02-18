package com.example.gotrabahomobile.Remote.FreelancerRemote

import com.example.gotrabahomobile.Remote.ArchiveRecordRemote.ArchiveRecordInterface
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import com.example.gotrabahomobile.Remote.UserRemote.UserInterface
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FreelancerInstance {
    val gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl("http://10.0.2.2:5025/")
        .build()
        .create(FreelancerInterface::class.java)
}