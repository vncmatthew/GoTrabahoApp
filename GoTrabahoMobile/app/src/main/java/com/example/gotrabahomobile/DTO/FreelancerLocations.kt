package com.example.gotrabahomobile.DTO

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class FreelancerLocations(
    var serviceId: Int?,
    var longitude: Double?,
    var latitude: Double?,
    var name: String?,
    var description: String?,
    var rating: Float?,
    var priceEstimate: Double?,
    var location: String?

)
