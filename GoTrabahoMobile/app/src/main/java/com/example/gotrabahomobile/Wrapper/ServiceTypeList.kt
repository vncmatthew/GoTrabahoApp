package com.example.gotrabahomobile.Wrapper

import android.os.Parcelable
import com.example.gotrabahomobile.Model.Services
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServiceTypeList(
    @SerializedName("\$values")
    val ServiceList: List<Services>
): Parcelable
