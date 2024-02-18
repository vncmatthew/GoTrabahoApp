package com.example.gotrabahomobile.Model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.time.LocalDate
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(
    @SerializedName("userId")
    @Expose
    var userId: Int? =  null,

    @SerializedName("userType")
    @Expose
    var userType: Int? =  null,

    @SerializedName("firstName")
    @Expose
    var firstName: String? = null,

    @SerializedName("lastName")
    @Expose
    var lastName: String? = null,


    @SerializedName("email")
    @Expose
    var email: String,

    @SerializedName("password")
    @Expose
    var password: String? = null,

    @SerializedName("contactNumber")
    @Expose
    var contactNumber: String? = null,

    @SerializedName("birthdate")
    @Expose
    var birthdate: String? = null,

    @SerializedName("address1")
    @Expose
    var address1: String? = null,

    @SerializedName("address2")
    @Expose
    var address2: String? = null,

    @SerializedName("barangay")
    @Expose
    var barangay: String? = null,

    @SerializedName("city")
    @Expose
    var city: String? = null,

    @SerializedName("longitude")
    @Expose
    var longitude: Double? = null,

    @SerializedName("latitude")
    @Expose
    var latitude: Double? = null,

    @SerializedName("merit")
    @Expose
    var merit: Int? =  null,

    @SerializedName("status")
    @Expose
    var status: Boolean? = false,

    @SerializedName("fine")
    @Expose
    var fine: Int? =  null
): Parcelable
