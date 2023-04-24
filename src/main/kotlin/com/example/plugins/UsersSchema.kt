package com.example.plugins

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlinx.serialization.Serializable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*

@Serializable
data class Player(val name: String, val score: Int)


fun playerToHTMLString(player: Player): String {
    return "<tr> <td> ${player.name}  <td> <td> ${player.score}  <td>  </tr>"
}
fun playersToHTMLString(players: List<Player>):String{
    return "<h1> Highscores </h1> <table>" + players.joinToString(" ") { player -> playerToHTMLString(player) } + "</table>"
}


class PlayerService(database: Database) {
    object Users : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", length = 50)
        val score = integer("score")

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(player: Player): Int = dbQuery {
        Users.insert {
            it[name] = player.name
            it[score] = player.score
        }[Users.id]
    }



    suspend fun getTable(): List<Player> {
        return dbQuery {
            Users.selectAll().orderBy(Users.score, SortOrder.DESC)
                .map { Player(it[Users.name], it[Users.score]) }

        }
    }

    //    suspend fun read(id: Int): Player? {
//        return dbQuery {
//            Users.select { Users.id eq id }
//                .map { Player(it[Users.name], it[Users.score]) }
//                .singleOrNull()
//        }
//    }
//
//    suspend fun update(id: Int, player: Player) {
//        dbQuery {
//            Users.update({ Users.id eq id }) {
//                it[name] = player.name
//                it[score] = player.score
//            }
//        }
//    }
//
//    suspend fun delete(id: Int) {
//        dbQuery {
//            Users.deleteWhere { Users.id.eq(id) }
//        }
//    }


}