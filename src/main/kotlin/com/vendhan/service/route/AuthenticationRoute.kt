package com.vendhan.service.route

import com.vendhan.service.AuthenticationService
import com.vendhan.service.CredentialAlreadyExistsException
import com.vendhan.service.InvalidCredentialsException
import com.vendhan.service.model.SignInData
import com.vendhan.service.model.SignUpData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.auth(authenticationService: AuthenticationService) {

    route("auth") {

        post("register") {
            val signUpData = call.receive(SignUpData::class)
            try {
                authenticationService.signUp(signUpData)
                call.respond(HttpStatusCode.OK, "Registration Successful")
            } catch (e: CredentialAlreadyExistsException) {
                call.respond(HttpStatusCode.BadRequest, e.message)
            } catch (e: InvalidCredentialsException) {
                call.respond(HttpStatusCode.BadRequest, e.message)
            }
        }

        post("login") {
            val signInData = call.receive(SignInData::class)
            try {
                val authToken = authenticationService.signIn(signInData)
                call.response.header("x-auth-token", authToken)
                call.respond(HttpStatusCode.OK, "Login Successful")
            } catch (e: InvalidCredentialsException) {
                call.respond(HttpStatusCode.BadRequest, e.message)
            }
        }

        authenticate("jwt-auth") {
            get("user") {
                try {
                    val principal = call.principal<JWTPrincipal>()
                    val phoneNumber = principal?.payload?.getClaim("phoneNumber")?.asString() ?: ""
                    val userData = authenticationService.getUserData(phoneNumber)
                    call.respond(userData)
                } catch (e: CredentialAlreadyExistsException) {
                    call.respond(HttpStatusCode.BadRequest, e.message)
                } catch (e: InvalidCredentialsException) {
                    call.respond(HttpStatusCode.BadRequest, e.message)
                }
            }
        }

        authenticate("jwt-auth") {
            delete {
                val principal = call.principal<JWTPrincipal>()
                val phoneNumber = principal?.payload?.getClaim("phoneNumber")?.asString() ?: ""
                try {
                    authenticationService.deleteUser(phoneNumber)
                    call.respond(HttpStatusCode.OK, "Delete Successful")
                } catch (e: InvalidCredentialsException) {
                    call.respond(HttpStatusCode.BadRequest, e.message)
                }
            }
        }

        authenticate("jwt-auth") {
            put(path = "update-profile") {
                val signUpData = call.receive(SignUpData::class)
                try {
                    val principal = call.principal<JWTPrincipal>()
                    val phoneNumber = principal?.payload?.getClaim("phoneNumber")?.asString() ?: ""
                    authenticationService.updateProfileData(phoneNumber, signUpData)
                    call.respond(HttpStatusCode.OK, "Update Successful")
                } catch (e: CredentialAlreadyExistsException) {
                    call.respond(HttpStatusCode.BadRequest, e.message)
                } catch (e: InvalidCredentialsException) {
                    call.respond(HttpStatusCode.BadRequest, e.message)
                }
            }
        }
    }
}

