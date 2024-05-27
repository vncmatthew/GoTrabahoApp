package com.example.gotrabahomobile.Model

import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("email")
    var email: String? = null,

    @SerializedName("password")
    var password: String? = null,
)
