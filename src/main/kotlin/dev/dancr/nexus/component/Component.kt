package dev.dancr.nexus.component

import dev.dancr.nexus.plugin.NexusPlugin
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

open class Component : Listener {

    internal val plugin = NexusPlugin.getPlugin<NexusPlugin>()

    public open fun enable() = Bukkit.getPluginManager().registerEvents(this, plugin)

    public open fun disable() = HandlerList.unregisterAll(this)

}