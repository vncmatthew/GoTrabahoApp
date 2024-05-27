package com.example.gotrabahomobile.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Negotiation(

    @SerializedName("negotiationId")
    @Expose
    var negotiationId: Int? = null,

    @SerializedName("freelancerPrice")
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
    var negotiationStatus: Boolean? = true,

    @SerializedName("tracker")
    @Expose
    var tracker: String? = null
)
