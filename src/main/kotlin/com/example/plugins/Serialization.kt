package com.example.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        get("/") {
            call.respond(highscoreList)
        }
    }
}

@Serializable
data class HighScore(val name: String, var score: Int) {}

var highscoreList = listOf<HighScore>(
    HighScore("Donald Trump", 0),
    HighScore("Maximilian Hertenstein", 100)
)

