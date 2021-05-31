package dev.dancr.circuit.component

import dev.dancr.nexus.component.Component
import dev.dancr.nexus.component.LobbySpawnComponent
import dev.dancr.nexus.config.ConfigScanner
import dev.dancr.nexus.util.playSoundAtHead
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerTeleportEvent

object LobbyLaunchComponent : Component() {

    private const val LAUNCH_VELOCITY = 3.0
    private const val LAUNCH_DELAY_TICKS = 10L

    private val config = ConfigScanner.getConfig<LobbySpawnComponent.LobbySpawnConfig>()

    @EventHandler
    public fun onPlayerTeleport(event: PlayerTeleportEvent) {
        val player = event.player
        val spawnPosition = config.spawnPosition
        if (spawnPosition.matches(event.to)) {
            Bukkit.getScheduler().runTaskLater(plugin, { _ ->
                player.playSoundAtHead(Sound.ENTITY_FIREWORK_ROCKET_LAUNCH)
                player.velocity = player.location.direction.normalize().multiply(LAUNCH_VELOCITY)
           }, LAUNCH_DELAY_TICKS)
        }
    }
}
