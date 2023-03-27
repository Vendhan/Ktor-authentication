package com.vendhan.service.route

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.food() {

    route("foods") {
        authenticate("jwt-auth") {
            get("/get-foods") {
                val principal = call.principal<JWTPrincipal>()
                val phoneNumber = principal?.payload?.getClaim("phoneNumber")?.asString()
                val expiresAt = principal?.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $phoneNumber! Token is expired at $expiresAt ms.")
            }
        }
    }
}