package dev.dancr.nexus.component

import dev.dancr.nexus.config.Config
import dev.dancr.nexus.config.ConfigScanner
import dev.dancr.nexus.config.Position
import org.bukkit.Bukkit
import org.bukkit.WorldCreator
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

object LobbySpawnComponent : Component() {

    @Config("lobby.json")
    data class LobbySpawnConfig(
        val worldName: String,
        val spawnPosition: Position
    )

    private val config: LobbySpawnConfig = ConfigScanner.getConfig()

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val world = Bukkit.createWorld(WorldCreator(config.worldName)) ?: throw IllegalArgumentException("World specified in lobby.json is missing")
        event.player.teleport(config.spawnPosition.toBukkitLocation(world))
    }

}