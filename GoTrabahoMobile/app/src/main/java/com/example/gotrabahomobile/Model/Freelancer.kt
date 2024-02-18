package com.example.gotrabahomobile.Model

import okhttp3.MultipartBody

data class Freelancer(
    var freelancerId: Int? = null,
    var userId: Int? = null,
    var idType: Int? = null,
    var governmentId: String? = null,
    var ImageFile: MultipartBody.Part? = null,
    var verificationStatus: Boolean? = null,
    var totalIncome: Int? = null

)
