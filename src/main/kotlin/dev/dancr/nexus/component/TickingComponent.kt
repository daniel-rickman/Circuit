package dev.dancr.nexus.component

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

abstract class TickingComponent : Component() {

    private lateinit var task: BukkitTask

    abstract fun onTick()

    override fun enable() {
        super.enable()
        task = Bukkit.getScheduler().runTaskTimer(plugin, ::onTick, 0, 1)
    }

    override fun disable() {
        super.disable()
        task.cancel()
    }
}