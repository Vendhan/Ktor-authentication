package com.vendhan.plugins

import com.vendhan.service.AuthenticationTokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.text.toByteArray

fun Application.configureSecurity(authenticationTokenService: AuthenticationTokenService) {

    val hashKey = System.getenv("HASH_KEY").toByteArray()
    val hmacKey = SecretKeySpec(hashKey, "HmacSHA256")

    fun hash(password: String): String {
        val hmac = Mac.getInstance("HmacSHA256")
        hmac.init(hmacKey)
        return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
    }

    install(Authentication) {
        jwt(name = "jwt-auth") {
            realm = "sample ktor"
            verifier(authenticationTokenService.jwtVerifier)
            validate { jwtCredential ->
                println("jwtCredentials: $jwtCredential")
                if (jwtCredential.payload.getClaim("phoneNumber").asString() != "") {
                    println("jwtCredentials payload: ${jwtCredential.payload}")
                    JWTPrincipal(jwtCredential.payload)
                } else {
                    null
                }
               // CustomJWTPrincipal(mobileNumber = jwtCredential.payload.subject)
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}
