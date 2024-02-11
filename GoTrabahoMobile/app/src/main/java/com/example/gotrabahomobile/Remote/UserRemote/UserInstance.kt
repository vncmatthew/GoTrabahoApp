package com.example.gotrabahomobile.Remote.UserRemote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserInstance {

    val gson = GsonBuilder()
        .setLenient()
        .create()


    val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl("http://10.0.2.2:5025/")
        .client(OkHttpClient.Builder().followRedirects(true).build())
        .build()
        .create(UserInterface::class.java)

}
