package com.example.gotrabahomobile.DTO

import kotlinx.serialization.Serializable


@Serializable
data class ReverseGeoCodeResponse(
    val place_id: Long, // Assuming place_id is a long integer
    val licence: String,
    val display_name: String? = null
)
