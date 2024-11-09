package com.example.route

import com.example.model.FollowAndUnfollowResponse
import com.example.model.FollowsParams
import com.example.repository.follows.FollowsRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.followRouting(){
    val repository by inject<FollowsRepository>()

     authenticate{
        route(path = "/follow"){
            post {
                val params = call.receiveNullable<FollowsParams>()

                if (params == null){
                    call.application.environment.log.warn("JWT authentication failed: Principal is null")

                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = FollowAndUnfollowResponse(
                            success = false,
                            message = "Oops, something went wrong, try again!"
                        )
                    )
                    return@post
                }

                val result = if (params.isFollowing){
                    repository.followUser(follower = params.follower, following = params.following)
                } else {
                    repository.unfollowUser(follower = params.follower, following = params.following)
                }

                call.respond(
                    status = result.code,
                    message = result.data
                )
            }
        }
    }
}