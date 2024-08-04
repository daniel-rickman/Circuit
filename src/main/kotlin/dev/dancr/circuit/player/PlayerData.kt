package dev.dancr.circuit.player

import org.jetbrains.exposed.sql.Table

object PlayerData : Table("players") {
    val uuid = uuid("uuid")
    val rank = varchar("rank", 16)

    override val primaryKey = PrimaryKey(uuid)
}