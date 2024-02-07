package com.example.gotrabahomobile.Model

import java.time.LocalDate

data class Notifications(
    var notificationId: Int? = null,
    var userId: Int? = null,
    var title: String? = null,
    var content: String? = null,
    var sendTime: LocalDate? = null
)
