package com.example.gotrabahomobile.Model

import java.time.LocalDate

data class FreelancerTesdaCertificate(
    var tesdaId: Int? = null,
    var freelancerId: Int? = null,
    var certificateName: String? = null,
    var dateSent: LocalDate? = null

)
