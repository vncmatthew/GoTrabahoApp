package com.example.gotrabahomobile.Model

import okhttp3.MultipartBody
import java.time.LocalDate

data class proofOfExperience(
    var proofId: Int? = null,
    var freelancerId: Int? = null,
    var proofName: String? = null,
    var proofImage: String? = null,
    var imageFile: MultipartBody? = null,
    var dateSent: LocalDate? = null


)
