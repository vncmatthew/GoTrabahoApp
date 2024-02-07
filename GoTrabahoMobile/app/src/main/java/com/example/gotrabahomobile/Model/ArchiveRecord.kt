package com.example.gotrabahomobile.Model

import java.math.BigDecimal
import java.time.LocalDate

data class ArchiveRecord(
    val archiveId: Int? = null,
    val userId: Int? = null,
    var userType: Int =  0,
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var password: String? = null,
    var contactNumber: String? = null,
    var birthDate: LocalDate? = null,
    var address1: String? = null,
    var address2: String? = null,
    var barangay: String? = null,
    var city: String? = null,
    var longitude: BigDecimal? = null,
    var latitude: BigDecimal? = null,
    var idType: Int =  0,
    var governmentId: Int =  0
)
