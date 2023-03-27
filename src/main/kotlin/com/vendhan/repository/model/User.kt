package com.vendhan.repository.model

@kotlinx.serialization.Serializable
data class User(
    val userName: String,
    val phoneNumber: String,
    val emailID: String? = null,
    val password: String? = null,
)
