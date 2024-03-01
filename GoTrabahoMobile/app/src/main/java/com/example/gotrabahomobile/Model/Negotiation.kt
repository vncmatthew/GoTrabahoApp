package com.example.gotrabahomobile.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Negotiation(

    @SerializedName("userId")
    @Expose
    var negotiationId: Int? = null,

    @SerializedName("freelancerId")
    @Expose
    var freelancerPrice: Double? = null,

    @SerializedName("customerPrice")
    @Expose
    var customerPrice: Double? = null,

    @SerializedName("customerId")
    @Expose
    var customerId: Int? = null,

    @SerializedName("serviceId")
    @Expose
    var serviceId: Int? = null,

    @SerializedName("negotiationStatus")
    @Expose
    var negotiationStatus: Boolean? = null,
)
