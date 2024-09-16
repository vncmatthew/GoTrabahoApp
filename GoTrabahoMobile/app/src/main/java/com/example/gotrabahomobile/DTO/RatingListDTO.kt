package com.example.gotrabahomobile.DTO

import java.math.BigDecimal

data class RatingListDTO(
    var ratingId: Int? = null,
    var serviceName: String? = null,
    var freelancerId: Int? = null,
    var rating: BigDecimal? = null,
    var comments: String? = null,
    var customerName: String? = null
)
