package com.example.gotrabahomobile.DTO

import okhttp3.MultipartBody

data class FreelancerDTO(
    /*    var idType: Int? = null,
    var governmentId: String? = null,
    var verificationStatus: Boolean? = null,
    var totalIncome: Int? = null*/

    var freelancerId: Int,
    var userId: Int? = null,
    var idType: Int? = null,
    var governmentId: String? = null,
    var imageFile: MultipartBody.Part? = null,
    var verificationStatus: Boolean? = null,
    var totalIncome: Int? = null
)
