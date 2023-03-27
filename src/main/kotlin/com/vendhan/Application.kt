package com.vendhan

import com.vendhan.ServiceLocator.httpPort
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.vendhan.plugins.*
import com.vendhan.repository.database.model.UserProfileData
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    println("Initiating Account Server")
    embeddedServer(Netty, httpPort, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    initDatabase()
    configureSecurity(authenticationTokenService = ServiceLocator.authenticationTokenService)
    configureRouting()
}

fun initDatabase() {
    val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )
    transaction(database) {
        SchemaUtils.create(UserProfileData)
    }
}
