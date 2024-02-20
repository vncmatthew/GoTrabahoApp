package com.example.gotrabahomobile.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Freelancer(
    @SerializedName("freelancerId")
    @Expose
    var freelancerId: Int? = null,

    @SerializedName("userId")
    @Expose
    var userId: Int? = null,

    @SerializedName("idType")
    @Expose
    var idType: Int? = null,

    @SerializedName("governmentId")
    @Expose
    var governmentId: String? = null,

    @SerializedName("verificationStatus")
    @Expose
    var verificationStatus: Boolean? = false,

    @SerializedName("totalIncome")
    @Expose
    var totalIncome: Int? = null

)
