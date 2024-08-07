package com.example.gotrabahomobile.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Services(
    var serviceId: Int? = null,
    var freelancerId: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var priceEstimate: Double? = null,
    var serviceTypeName: String? = null,
    var status: Int? = null,
    var showService: Boolean? = null,
    var location: String? = null,
    var rating: Float? = null
): Parcelable
