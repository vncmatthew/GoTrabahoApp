package com.example.gotrabahomobile.DTO

data class ServicesWUserId(
    var userId: Int? = null,
    var serviceId: Int? = null,
    var freelancerId: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var priceEstimate: Double? = null,
    var serviceTypeName: String? = null,
    var status: Int? = null,
    var location: String? = null,
    var rating: Float? = null
)
