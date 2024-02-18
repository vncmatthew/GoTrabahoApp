package com.example.gotrabahomobile.Model

import okhttp3.MultipartBody
import java.time.LocalDate

data class FreelancerTesdaCertificate(
    var tesdaId: Int? = null,
    var freelancerId: Int? = null,
    var certificateName: String? = null,
    var certificateImage: String? = null,
    var imageFile: MultipartBody? = null,
    var dateSent: LocalDate? = null

)
