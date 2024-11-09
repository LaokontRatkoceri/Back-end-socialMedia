package com.example.dao.follows

import org.jetbrains.exposed.sql.CurrentOrFollowing

interface FollowDao {

    suspend fun followUser(follower:Long, following: Long):Boolean

    suspend fun unfollowUser(follower: Long, following: Long):Boolean

    suspend fun getFollowers(userId: Long, pageNumber: Int, pageSize: Int): List<Long>

    suspend fun getFollowing(userId: Long, pageNumber: Int, pageSize: Int): List<Long>

    suspend fun isAlreadyFollowing(follower: Long, following: Long): Boolean
}