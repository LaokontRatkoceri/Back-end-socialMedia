package com.example.dao.follows

import com.example.dao.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class FollowDaoImpl : FollowDao {
    override suspend fun followUser(follower: Long, following: Long): Boolean {
        return dbQuery{
            val instantStatement = FollowsTable.insert {
                it[followerId] = follower
                it[followingId] = following
            }

            instantStatement.resultedValues?.singleOrNull() != null
        }
    }

    override suspend fun unfollowUser(follower: Long, following: Long): Boolean {
        return dbQuery {
            FollowsTable.deleteWhere {
                (followerId eq follower) and (followingId eq following)
            } > 0
        }
    }

    override suspend fun getFollowers(userId: Long, pageNumber: Int, pageSize: Int): List<Long> {
        return dbQuery {
            FollowsTable.selectAll().where { FollowsTable.followerId eq userId }
                .orderBy(FollowsTable.followDate, SortOrder.DESC)
                .limit(count = pageSize).offset(start = ((pageNumber - 1) + pageSize).toLong())
                .map { it[FollowsTable.followingId] }
        }
    }

    override suspend fun getFollowing(userId: Long, pageNumber: Int, pageSize: Int): List<Long> {
        return dbQuery {
            FollowsTable.selectAll().where { FollowsTable.followingId eq userId }
                .orderBy(FollowsTable.followDate, SortOrder.DESC)
                .limit(count = pageSize).offset(start = ((pageNumber - 1) + pageSize).toLong())
                .map { it[FollowsTable.followerId] }
        }
    }

    override suspend fun isAlreadyFollowing(follower: Long, following: Long): Boolean {
        return dbQuery {
            val condition = (FollowsTable.followerId eq follower) and (FollowsTable.followingId eq following)
            !FollowsTable.selectAll().where(condition).empty()
        }
    }
}