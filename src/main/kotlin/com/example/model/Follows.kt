package com.example.model

import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.CurrentOrFollowing

@Serializable
data class FollowAndUnfollowResponse(
    val success: Boolean,
    val message: String? = null
)


@Serializable
data class FollowsParams(
    val follower: Long,
    val following: Long,
    val isFollowing: Boolean
)