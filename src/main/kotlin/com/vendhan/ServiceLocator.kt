package com.vendhan

import com.typesafe.config.ConfigFactory
import com.vendhan.repository.database.AccountDaoImpl
import com.vendhan.service.AuthenticationService
import com.vendhan.service.AuthenticationTokenService
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.RSAPrivateKeySpec
import java.security.spec.RSAPublicKeySpec
import java.time.Clock

object ServiceLocator {

    private val rootConfig = ConfigFactory.load()
    private val serverConfig = rootConfig.getConfig("ktor-server")
    val httpPort = serverConfig.getInt("port")

    val authenticationTokenService = AuthenticationTokenService(clock = Clock.systemUTC()!!)

    private val accountDao = AccountDaoImpl()

    val authenticationService = AuthenticationService(
        accountDao = accountDao,
        authenticationTokenService = authenticationTokenService
    )
}