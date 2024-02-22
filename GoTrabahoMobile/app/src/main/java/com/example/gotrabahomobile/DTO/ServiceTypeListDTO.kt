package com.example.gotrabahomobile.DTO

data class ServiceTypeListDTO(
    var serviceId: Int? = null,
    var freelancerId: Int? = null,
    var name: String? = null,
    var priceEstimate: Double? = null,
    var serviceTypeName: String? = null,
    var location: String? = null,
    var rating: Float? = null
)
