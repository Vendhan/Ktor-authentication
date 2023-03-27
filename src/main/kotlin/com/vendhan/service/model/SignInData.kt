package com.vendhan.service.model

@kotlinx.serialization.Serializable
data class SignInData(
    val phoneNumber: String,
    val password: String,
)
