package com.example.gotrabahomobile.Remote.ArchiveRecordRemote

import com.example.gotrabahomobile.Model.ArchiveRecord
import com.example.gotrabahomobile.Model.Cities
import retrofit2.http.GET
import retrofit2.Call
interface CityInterface {
    @GET("api/Cities")
    fun getCities(): Call<List<Cities>>

}