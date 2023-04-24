package com.example.plugins

import org.jetbrains.exposed.sql.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureDatabases() {
    val database = Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            user = "root",
            driver = "org.h2.Driver",
            password = ""
        )
    val userService = PlayerService(database)

    routing {
        // Create player
        post("/players") {
            val player = call.receive<Player>()
            val id = userService.create(player)
            call.respond(HttpStatusCode.Created, id)
        }

        // Get all players
        get("/allPlayers") {
            val players = userService.getTable()
            call.respond(HttpStatusCode.OK, players)
        }

        get("/allPlayersHTMLTable") {
            val players = userService.getTable()
            call.respondText(playersToHTMLString(players), ContentType.Text.Html)
        }




//        // Read player
//        get("/players/{id}") {
//            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
//            val player = userService.read(id)
//            if (player != null) {
//                call.respond(HttpStatusCode.OK, player)
//            } else {
//                call.respond(HttpStatusCode.NotFound)
//            }
//        }
//        // Update player
//        put("/players/{id}") {
//            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
//            val player = call.receive<Player>()
//            userService.update(id, player)
//            call.respond(HttpStatusCode.OK)
//        }
//        // Delete player
//        delete("/players/{id}") {
//            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
//            userService.delete(id)
//            call.respond(HttpStatusCode.OK)
//        }
    }
}
