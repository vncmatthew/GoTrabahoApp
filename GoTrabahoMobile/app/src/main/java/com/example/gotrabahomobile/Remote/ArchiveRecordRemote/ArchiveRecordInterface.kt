package com.example.gotrabahomobile.Remote.ArchiveRecordRemote

import com.example.gotrabahomobile.Model.ArchiveRecord
import retrofit2.http.GET
import retrofit2.Call
interface ArchiveRecordInterface {
    @GET("api/ArchiveRecord")
    fun getArchiveRecords(): Call<List<ArchiveRecord>>
}