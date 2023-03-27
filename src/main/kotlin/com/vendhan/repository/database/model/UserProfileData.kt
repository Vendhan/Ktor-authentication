package com.vendhan.repository.database.model

import org.jetbrains.exposed.sql.Table

object UserProfileData : Table() {
    val id = integer("id").autoIncrement()
    val userName = varchar("userName", length = 50)
    val phoneNumber = varchar("phoneNumber", length = 12)
    val emailID = varchar("emailID", length = 50)
    val password = varchar("password", length = 10)
    override val primaryKey = PrimaryKey(id)
}