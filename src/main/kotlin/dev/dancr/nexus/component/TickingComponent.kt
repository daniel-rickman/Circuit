package dev.dancr.nexus.component

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

abstract class TickingComponent : ServerComponent() {

    private lateinit var task: BukkitTask

    abstract fun onTick()

    override fun onComponentEnable() {
        super.onComponentEnable()
        task = Bukkit.getScheduler().runTaskTimer(plugin, ::onTick, 0, 1)
    }

    override fun onComponentDisable() {
        super.onComponentDisable()
        task.cancel()
    }
}