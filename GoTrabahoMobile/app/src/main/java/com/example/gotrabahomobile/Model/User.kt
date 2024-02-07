package com.example.gotrabahomobile.Model

import java.math.BigDecimal
import java.time.LocalDate

data class User(
    var userId: Int =  0,
    var userType: Int =  0,
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var password: String? = null,
    var contactNumber: String? = null,
    var birthdate: LocalDate? = null,
    var address1: String? = null,
    var address2: String? = null,
    var barangay: String? = null,
    var city: String? = null,
    var longitude: BigDecimal? = null,
    var latitude: BigDecimal? = null,
    var merit: Int =  0,
    var status: Boolean = false,
    var fine: Int =  0
)
