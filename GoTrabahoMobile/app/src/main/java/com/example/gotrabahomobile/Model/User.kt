package com.example.gotrabahomobile.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.type.DateTime

import java.math.BigDecimal
import java.time.LocalDate



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
    var email: String? = null,

    @SerializedName("password")
    @Expose
    var password: String? = null,

    @SerializedName("accountMade")
    @Expose
    var accountMade: String? = null,


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
    var city: Int? = null,

    @SerializedName("longitude")
    @Expose
    var longitude: Double? = null,

    @SerializedName("latitude")
    @Expose
    var latitude: Double? = null,

    @SerializedName("status")
    @Expose
    var status: Boolean? = false,

    @SerializedName("BanStartDate")
    @Expose
    var BanStartDate: DateTime? = null,

    @SerializedName("BanEndDate")
    @Expose
    var BanEndDate: DateTime? = null

)
