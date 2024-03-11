package com.example.gotrabahomobile.DTO

data class BookingUserDTO(
    val bookingId: Int? = null,
    val customerId: Int? = null,
    val bookingDatetime: String? = null,
    val amount: Double? = null,
    val serviceFee: Double? = null,
    val bookingStatus: Int? = null,
    val serviceId: Int? = null,
    val ratingId: Int? = null,
    val freelancerFirst: String? = null,
    val freelancerLast: String? = null,
    val serviceType: String? = null
)
