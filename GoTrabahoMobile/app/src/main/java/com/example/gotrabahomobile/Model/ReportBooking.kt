package com.example.gotrabahomobile.Model

import java.time.LocalDateTime

data class ReportBooking(
    var reportId: Int? = null,
    var userId: Int? = null,
    var title: String? = null,
    var timeStamp: LocalDateTime? = null,
    var status: Boolean? = null,
    var bookingId: Int? = null
)
