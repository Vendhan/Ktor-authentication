package com.vendhan.repository.database

import com.vendhan.repository.AccountDao
import com.vendhan.repository.database.model.UserProfileData
import com.vendhan.repository.model.User
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class AccountDaoImpl : AccountDao {

    override suspend fun putUserProfileData(user: User) {
        transaction {
            UserProfileData.insert {
                it[userName] = user.userName
                it[phoneNumber] = user.phoneNumber
                it[emailID] = user.emailID ?: ""
                it[password] = user.password ?: ""
            }[UserProfileData.id]
        }
    }

    override suspend fun getUserProfileData(phoneNumber: String): User? {
        return dbQuery {
            UserProfileData.select { UserProfileData.phoneNumber eq phoneNumber }
                .map {
                    User(
                        userName = it[UserProfileData.userName],
                        phoneNumber = it[UserProfileData.phoneNumber],
                        emailID = it[UserProfileData.emailID],
                        password = it[UserProfileData.password]
                    )
                }
                .singleOrNull()
        }
    }

    override suspend fun updateUserProfileData(phoneNumber: String, user: User) {
        dbQuery {
            UserProfileData.update({ UserProfileData.phoneNumber eq phoneNumber }) {
                it[userName] = user.userName
                it[emailID] = user.emailID ?: ""
            }
        }
    }

    override suspend fun deleteUserProfileData(phoneNumber: String) {
        dbQuery {
            UserProfileData.deleteWhere { UserProfileData.phoneNumber.eq(phoneNumber) }
        }
    }


    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}