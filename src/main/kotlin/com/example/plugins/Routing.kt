package com.example.plugins

import com.example.route.authRouting
import com.example.route.followRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authRouting()
        followRouting()
    }
}
