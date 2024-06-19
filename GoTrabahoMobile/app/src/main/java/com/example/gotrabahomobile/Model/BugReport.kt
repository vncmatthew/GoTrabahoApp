package com.example.gotrabahomobile.Model

data class BugReport(
    var bugID: Int? = null,
    var userID: Int? = null,
    var featureID: String? = null,
    var description: String? = null,
    var status: Boolean? = null
)
