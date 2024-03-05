package com.example.gotrabahomobile.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Negotiation(

    @SerializedName("negotiationId")
    @Expose
    val negotiationId: Int? = null,

    @SerializedName("freelancerPrice")
    @Expose
    val freelancerPrice: Double? = null,

    @SerializedName("customerPrice")
    @Expose
    val customerPrice: Double? = null,

    @SerializedName("customerId")
    @Expose
    val customerId: Int? = null,

    @SerializedName("serviceId")
    @Expose
    val serviceId: Int? = null,

    @SerializedName("negotiationStatus")
    @Expose
    val negotiationStatus: Boolean? = true,

    @SerializedName("tracker")
    @Expose
    val tracker: String? = null
)
