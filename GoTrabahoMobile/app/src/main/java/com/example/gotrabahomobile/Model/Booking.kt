package com.example.gotrabahomobile.Model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Booking(
    val bookingId: Int? = null,
    val customerId: Int? = null,
    val bookingDatetime: String? = null,
    val amount: BigDecimal? = null,
    val serviceFee: Int? = null,
    val bookingStatus: Int? = null,
    val serviceId: Int? = null,
    val ratingId: Int? = null,


)
