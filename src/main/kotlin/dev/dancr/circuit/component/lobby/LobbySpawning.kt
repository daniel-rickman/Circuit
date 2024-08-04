package dev.dancr.circuit.component.lobby

import dev.dancr.circuit.component.ServerComponent
import dev.dancr.circuit.config.Config
import dev.dancr.circuit.config.ConfigScanner
import dev.dancr.circuit.config.Position
import org.bukkit.Bukkit
import org.bukkit.WorldCreator
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

object LobbySpawning : ServerComponent() {

    @Config("lobby_spawn")
    data class LobbySpawnConfig(val worldName: String, val spawnPosition: Position)

    private val config: LobbySpawnConfig = ConfigScanner.getConfig()

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val world = Bukkit.createWorld(WorldCreator(config.worldName)) ?: throw IllegalArgumentException("World specified in lobby.json is missing")
        event.player.teleport(config.spawnPosition.toBukkitLocation(world))
    }

}