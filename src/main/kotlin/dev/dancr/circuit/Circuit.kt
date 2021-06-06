package dev.dancr.circuit

import dev.dancr.circuit.component.LobbyLaunching
import dev.dancr.nexus.plugin.NexusPlugin
import dev.dancr.nexus.component.WorldSettings
import dev.dancr.nexus.team.Teams
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.GameRule
import org.bukkit.World

class Circuit : NexusPlugin() {

    val world: World
        get() = Bukkit.getWorlds().first()

    override fun onPluginEnable() {
        LobbyLaunching.enable()
        WorldSettings.enable()
        Teams.enable()

        // World settings
        Bukkit.setDefaultGameMode(GameMode.ADVENTURE)
        WorldSettings.modifyAll(false)
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
        world.time = 6000 // Noon
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
        world.setGameRule(GameRule.DO_INSOMNIA, false)

    }

}