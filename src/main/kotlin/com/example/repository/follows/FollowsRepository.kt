package com.example.repository.follows

import com.example.model.FollowAndUnfollowResponse
import com.example.util.Response


interface FollowsRepository {

    suspend fun followUser(follower: Long, following: Long): Response<FollowAndUnfollowResponse>

    suspend fun unfollowUser(follower: Long, following: Long): Response<FollowAndUnfollowResponse>

}