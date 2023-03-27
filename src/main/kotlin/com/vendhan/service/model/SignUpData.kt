package com.vendhan.service.model

@kotlinx.serialization.Serializable
data class SignUpData(
    val userName: String,
    val phoneNumber: String,
    val password: String,
    val emailID: String?,
)
