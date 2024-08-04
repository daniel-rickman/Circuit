package dev.dancr.circuit.component

import dev.dancr.circuit.Circuit
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

abstract class ServerComponent : Listener {

    protected val plugin = Circuit.plugin
    public var isEnabled = false
        private set

    public fun enable() {
        if (isEnabled) return;
        isEnabled = true
        Bukkit.getPluginManager().registerEvents(this, plugin)
        Bukkit.getLogger().info("${this.javaClass.simpleName} is enabled")
        onComponentEnable()
    }

    public fun disable() {
        if (!isEnabled) return;
        isEnabled = false
        HandlerList.unregisterAll(this)
        Bukkit.getLogger().info("${this.javaClass.simpleName} is disabled")
        onComponentDisable()
    }

    fun softDepend(component: ServerComponent) {
        if (component.isEnabled) return
        Circuit.plugin.logger.warning("${component::class.simpleName} is a soft dependency for ${this::class.simpleName}. ${this::class.simpleName} may not fully function with it.")
    }

    fun hardDepend(component: ServerComponent) {
        if (component.isEnabled) return;
        component.enable()
    }

    protected open fun onComponentEnable() {}

    protected open fun onComponentDisable() {}

}