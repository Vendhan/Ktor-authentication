package com.vendhan.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import io.ktor.server.auth.*
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Clock
import java.util.*

class AuthenticationTokenService(
    private val clock: Clock
) {
    private val issuer = "foodAppIssuer"
    private val jwtSecret = System.getenv("JWT_SECRET")
    private val jwtAlgorithm = Algorithm.HMAC256(jwtSecret)
    val jwtVerifier: JWTVerifier = JWT
        .require(jwtAlgorithm)
        .withIssuer(issuer)
        .build()

    fun generateAuthToken(mobileNumber: String): AuthToken {
        try {
            return JWT.create()
                .withIssuer(issuer)
                .withSubject("FoodAppAuthentication")
                .withClaim("phoneNumber", mobileNumber)
                .withExpiresAt(Date.from(clock.instant().plusSeconds(3600L)))
                .sign(jwtAlgorithm)
        } catch (e: JWTCreationException) {
            //Invalid Signing configuration / Couldn't convert Claims.
            throw e
        }
    }

    fun decodeAuthToken(token: AuthToken) {
        val decodedJWT = JWT()
            .decodeJwt(token)
        println("decodedJWT $decodedJWT")
    }
}

typealias AuthToken = String

data class CustomJWTPrincipal(val mobileNumber: String) : Principal