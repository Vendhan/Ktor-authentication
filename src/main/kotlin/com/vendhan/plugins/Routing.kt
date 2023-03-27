package com.vendhan.plugins

import com.vendhan.ServiceLocator.authenticationService
import com.vendhan.service.route.auth
import com.vendhan.service.route.food
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        route("api") {
            auth(authenticationService)
            food()
        }
    }
}
