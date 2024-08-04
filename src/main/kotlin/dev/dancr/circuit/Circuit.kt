package dev.dancr.circuit

import dev.dancr.circuit.component.Lobby
import dev.dancr.circuit.component.global.WorldSettings
import dev.dancr.circuit.player.PlayerData
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.GameRule
import org.bukkit.World
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

class Circuit : JavaPlugin() {

    companion object {
        private const val DATA_FILE_NAME = "data.db"

        val plugin: JavaPlugin
            get() = getPlugin(Circuit::class.java)
    }

    val world: World
        get() = Bukkit.getWorlds().first()

    final override fun onEnable() {
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        Database.connect("jdbc:sqlite:/${dataFolder.absolutePath}/$DATA_FILE_NAME")
        transaction {
            TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
            SchemaUtils.create(PlayerData)
        }

        // World settings
        Bukkit.setDefaultGameMode(GameMode.ADVENTURE)
        WorldSettings.modifyAll(false)
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
        world.time = 6000 // Noon
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
        world.setGameRule(GameRule.DO_INSOMNIA, false)

        Lobby.enable()
    }

    final override fun onDisable() {
    }

}