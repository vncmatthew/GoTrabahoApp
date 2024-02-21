package com.example.gotrabahomobile.Model

import java.time.LocalDate

data class proofOfExperience(
    var proofId: Int? = null,
    var freelancerId: Int? = null,
    var proofName: String? = null,
    var dateSent: LocalDate? = null
)
