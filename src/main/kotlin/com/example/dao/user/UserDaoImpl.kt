package com.example.dao.user

import com.example.dao.DatabaseFactory.dbQuery
import com.example.model.SignUpParams
import com.example.security.hashPassword
import com.example.util.IdGenerator
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus

class UserDaoImpl: UserDao {
    override suspend fun insert(params: SignUpParams): UserRow? {
        return dbQuery{
            val insertStatement = UserTable.insert {
                it[id] = IdGenerator.generateId()
                it[name] = params.name
                it[email] = params.email
                it[password] = hashPassword(params.password)
            }
            insertStatement.resultedValues?.singleOrNull()?.let {
                rowToUser(it)
            }
        }
    }

    override suspend fun findByEmail(email: String): UserRow? {
        return dbQuery {
            UserTable.selectAll().where { UserTable.email eq email }
                .map { rowToUser(it) }
                .singleOrNull()
        }
    }

    override suspend fun updateFollowsCount(follower: Long, following: Long, isFollowing: Boolean): Boolean {
        return dbQuery {
            val count = if (isFollowing) +1 else -1

            val success1 = UserTable.update({ UserTable.id eq follower }){
                it.update(column = followingCount, value = followingCount.plus(count))
            }>0

            val success2 = UserTable.update({ UserTable.id eq following }){
                it.update(column = followersCount, value = followersCount.plus(count))
            }>0

            success1 && success2
        }
    }

    private fun rowToUser(row: ResultRow):UserRow{
        return UserRow(
            id = row[UserTable.id],
            name = row[UserTable.name],
            bio = row[UserTable.bio],
            imageUrl = row[UserTable.imageUrl],
            password = row[UserTable.password],
            followersCount = row[UserTable.followersCount],
            followingCount = row[UserTable.followingCount],
        )
    }
}