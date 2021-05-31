package dev.dancr.nexus.plugin

import dev.dancr.nexus.component.LobbySpawnComponent
import dev.dancr.nexus.config.ConfigScanner
import org.bukkit.plugin.java.JavaPlugin

open class NexusPlugin : JavaPlugin() {

    companion object {
        public inline fun <reified T : JavaPlugin> getPlugin() = getPlugin(T::class.java)
    }

    final override fun onEnable() {
        // Spawn player in lobby
        LobbySpawnComponent.enable()

        onPluginEnable()
    }

    open fun onPluginEnable() {}

    final override fun onDisable() {
        onPluginDisable()
    }

    open fun onPluginDisable() {}



}