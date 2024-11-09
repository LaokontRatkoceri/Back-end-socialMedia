package com.example.repository.follows

import com.example.dao.follows.FollowDao
import com.example.dao.user.UserDao
import com.example.model.FollowAndUnfollowResponse
import com.example.util.Response
import io.ktor.http.*

class FollowsRepositoryImpl(
    private val userDao: UserDao,
    private val followDao: FollowDao
) : FollowsRepository {

    override suspend fun followUser(follower: Long, following: Long): Response<FollowAndUnfollowResponse> {
        return if(followDao.isAlreadyFollowing(follower, following)){
            Response.Error(
                code = HttpStatusCode.Forbidden,
                data = FollowAndUnfollowResponse(
                    success = false,
                    message = "You are already following this user"
                )
            )
        }else{
            val success = followDao.followUser(follower, following)

            if (success){
                userDao.updateFollowsCount(follower, following, isFollowing = true)
                Response.Success(
                    data = FollowAndUnfollowResponse(success = true)
                )
            }else{
                Response.Error(
                    code = HttpStatusCode.InternalServerError,
                    data = FollowAndUnfollowResponse(
                        success = false,
                        message = "Oops, something went wrong on our side, please try again!"
                    )
                )
            }
        }
    }

    override suspend fun unfollowUser(follower: Long, following: Long): Response<FollowAndUnfollowResponse> {
        val success = followDao.unfollowUser(follower, following)

        return if (success){
            userDao.updateFollowsCount(follower, following, isFollowing = true)
            Response.Success(
                data = FollowAndUnfollowResponse(success = true)
            )
        }else{
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = FollowAndUnfollowResponse(
                    success = false,
                    message = "Oops, something went wrong on our side, please try again!"
                )
            )
        }
    }

}