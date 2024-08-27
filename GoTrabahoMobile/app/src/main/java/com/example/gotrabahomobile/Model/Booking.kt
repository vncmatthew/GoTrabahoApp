package com.example.gotrabahomobile.Model

data class Booking(
    var bookingId: Int? = null,
    var customerId: Int? = null,
    var bookingDatetime: String? = null,
    var amount: Double? = null,
    var bookingStatus: Int? = null,
    var serviceId: Int? = null,
    var serviceFee: Double? = null,
    var negotiationId: Int? = null,
    var paymentStatus: Boolean? = null,
    var refundFreelancer: Int? = null)
