package dev.dancr.nexus.component

import dev.dancr.nexus.plugin.NexusPlugin
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

open class ServerComponent : Listener {

    protected val plugin = NexusPlugin.getPlugin<NexusPlugin>()
    public var isEnabled = false
        private set

    public fun enable() {
        isEnabled = true
        Bukkit.getPluginManager().registerEvents(this, plugin)
        onComponentEnable()
    }

    public fun disable() {
        isEnabled = false
        HandlerList.unregisterAll(this)
        onComponentDisable()
    }

    protected fun require(component: ServerComponent) {
        if (component.isEnabled) return
        plugin.logger.warning("${component::class.simpleName} is a required component for ${this::class.simpleName} so must be enabled in advance")
    }

    protected open fun onComponentEnable() {}

    protected open fun onComponentDisable() {}

}