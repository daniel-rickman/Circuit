package dev.dancr.nexus.data

import java.util.UUID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object PlayerData : Table("player_data") {
    val uuid: Column<UUID> = uuid("uuid")
    val rank: Column<String> = varchar("rank", 16)



}