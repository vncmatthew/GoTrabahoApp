package com.example.gotrabahomobile.Model

import java.math.BigDecimal
import java.time.LocalDate

data class Rating(
    var ratingId: Int? = null,
    var bookingId: Int? = null,
    var star: BigDecimal? = null,
    var dateRecorded: String? = null,
    var customerId: Int? = null
)
