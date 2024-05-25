package com.example.gotrabahomobile.Model

data class Booking(
    val bookingId: Int? = null,
    val customerId: Int? = null,
    val bookingDatetime: String? = null,
    val amount: Double? = null,
    val bookingStatus: Int? = null,
    val serviceId: Int ,
    val serviceFee: Double? = null,
    val negotiationId: Int? = null)
