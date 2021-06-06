package dev.dancr.circuit

import dev.dancr.circuit.component.LobbyLaunchComponent
import dev.dancr.nexus.plugin.NexusPlugin
import dev.dancr.nexus.component.WorldSettings
import dev.dancr.nexus.team.TeamManager
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.GameRule
import org.bukkit.World

class Circuit : NexusPlugin() {

    val world: World
        get() = Bukkit.getWorlds().first()

    override fun onPluginEnable() {
        LobbyLaunchComponent.enable()
        WorldSettings.enable()
        TeamManager.enable()

        // World settings
        Bukkit.setDefaultGameMode(GameMode.ADVENTURE)
        WorldSettings.modifyAll(false)
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
        world.time = 6000 // Noon
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
        world.setGameRule(GameRule.DO_INSOMNIA, false)

    }

}