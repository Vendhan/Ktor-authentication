package com.vendhan.repository

import com.vendhan.repository.model.User

interface AccountDao {

    suspend fun putUserProfileData(user: User)

    suspend fun getUserProfileData(phoneNumber: String): User?

    suspend fun updateUserProfileData(phoneNumber: String, user: User)

    suspend fun deleteUserProfileData(phoneNumber: String)
}