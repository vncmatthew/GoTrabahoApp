package com.example.gotrabahomobile.Model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Booking(
    val bookingId: Int? = null,
    val customerId: Int? = null,
    val bookingDatetime: LocalDateTime? = null,
    val amount: BigDecimal? = null,
    val bookingStatus: Int? = null
)
