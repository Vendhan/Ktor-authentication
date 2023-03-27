package com.vendhan.service

import com.vendhan.repository.AccountDao
import com.vendhan.repository.model.User
import com.vendhan.service.model.SignInData
import com.vendhan.service.model.SignUpData
import kotlinx.coroutines.runBlocking

class AuthenticationService(
    private val accountDao: AccountDao,
    private val authenticationTokenService: AuthenticationTokenService,
) {

    fun signUp(signUpData: SignUpData) {
        runBlocking {
            if (!isValidEmail(signUpData.emailID) || !isValidPhoneNumber(signUpData.phoneNumber) || !isValidPassword(
                    signUpData.password
                )
            )
                throw InvalidCredentialsException("Invalid phoneNumber or password")
            if (accountDao.getUserProfileData(signUpData.phoneNumber) != null) {
                throw CredentialAlreadyExistsException("Account already exists")
            } else {
                accountDao
                    .putUserProfileData(
                        user = User(
                            userName = signUpData.userName,
                            phoneNumber = signUpData.phoneNumber,
                            emailID = signUpData.emailID,
                            password = signUpData.password,
                        )
                    )
            }
        }
    }

    fun updateProfileData(phoneNumber: String,signUpData: SignUpData) {
        runBlocking {
            if (!isValidEmail(signUpData.emailID) || !isValidPhoneNumber(signUpData.phoneNumber) || !isValidPassword(
                    signUpData.password
                )
            ) {
                throw InvalidCredentialsException("Invalid phoneNumber or password")
            }
            accountDao
                .updateUserProfileData(
                    phoneNumber = phoneNumber,
                    user = User(
                        userName = signUpData.userName,
                        phoneNumber = signUpData.phoneNumber,
                        emailID = signUpData.emailID,
                        password = signUpData.password,
                    )
                )
        }
    }

    fun signIn(signInData: SignInData): String {
        return runBlocking {
            val userData = accountDao.getUserProfileData(signInData.phoneNumber)
            println(userData)
            when {
                userData == null -> throw InvalidCredentialsException("Invalid UserName or Password")
                userData.password != signInData.password -> throw InvalidCredentialsException("Invalid UserName or Password")
                userData.phoneNumber != signInData.phoneNumber -> throw InvalidCredentialsException("Invalid UserName or Password")
                else -> return@runBlocking authenticationTokenService.generateAuthToken(mobileNumber = signInData.phoneNumber)
            }
        }
    }

    fun getUserData(phoneNumber: String): User {
        return runBlocking {
            return@runBlocking accountDao.getUserProfileData(phoneNumber)
                ?: throw InvalidCredentialsException("Invalid UserName or Password")
        }
    }

    fun deleteUser(phoneNumber: String) {
        runBlocking {
            if (!isValidPhoneNumber(phoneNumber))
                throw InvalidCredentialsException("Invalid PhoneNumber")
            try {
                accountDao.deleteUserProfileData(phoneNumber = phoneNumber)
            } catch (e: Exception) {
                throw InvalidCredentialsException("Invalid PhoneNumber")
            }
        }
    }

    private fun isValidEmail(email: String?): Boolean {
        if (email.isNullOrEmpty())
            return true
        val emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex()
        return email.matches(emailRegex)
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#\$%^&+=])(?=\\S+\$).{8,12}\$".toRegex()
        return password.matches(passwordRegex)
    }

    private fun isValidPhoneNumber(mobileNumber: String): Boolean {
        val mobileRegex = "^[1-9]\\d{9}\$".toRegex()
        return mobileNumber.matches(mobileRegex)
    }
}

class CredentialAlreadyExistsException(override val message: String) : Exception(message)
class InvalidCredentialsException(override val message: String) : Exception(message)