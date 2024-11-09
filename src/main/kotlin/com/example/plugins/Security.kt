package com.mrdip.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.dao.user.UserDao
import com.example.model.AuthResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject
import java.util.*

private val jwtAudience = System.getenv("jwt.audience")
private val jwtIssuer = System.getenv("jwt.domain")
private val jwtSecret = System.getenv("jwt.secret")

private const val CLAIM = "email"

fun Application.configureSecurity() {
    val userDao by inject<UserDao>()

    authentication {
        jwt {
            realm = "Access to 'follow'"
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtIssuer)
                    .build()
            )
            validate { credential ->
                val email = credential.payload.getClaim(CLAIM).asString()
                if (email != null && userDao.findByEmail(email) != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { _, _ ->
                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    message = AuthResponse(
                        errorMessage = "Token is not valid or has expired"
                    )
                )
            }
        }
    }
}

fun generateToken(email: String): String {
    return JWT.create()
        .withAudience(jwtAudience)
        .withIssuer(jwtIssuer)
        .withClaim(CLAIM, email)
        .withExpiresAt(Date(System.currentTimeMillis() + 3600_000))
        .sign(Algorithm.HMAC256(jwtSecret))
}