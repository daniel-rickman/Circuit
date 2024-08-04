package dev.dancr.circuit.component.lobby

import dev.dancr.circuit.component.ServerComponent
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

object LobbyJoining : ServerComponent() {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        with (event.player) {
            this.health = this.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.defaultValue
            this.foodLevel = 20
        }
    }
}