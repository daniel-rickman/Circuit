package dev.dancr.circuit.component.global

import dev.dancr.circuit.component.ServerComponent
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.weather.WeatherChangeEvent
import org.bukkit.event.world.TimeSkipEvent

object WorldSettings : ServerComponent() {

    private val worldSettings: MutableMap<Setting, Boolean> = mutableMapOf()

    init {
        worldSettings.putAll(Setting.values().map { it to true })
    }

    fun modify(vararg modifiedSettings: Pair<Setting, Boolean>) = modifiedSettings.forEach { worldSettings[it.first] = it.second }

    fun modifyAll(isAllowed: Boolean) = worldSettings.replaceAll { _, _ -> isAllowed }

    fun isDisallowed(setting: Setting) : Boolean = !worldSettings[setting]!!

    @EventHandler
    private fun onEntityDamage(event: EntityDamageEvent) {
        if (event is EntityDamageByEntityEvent) {
            event.isCancelled = isDisallowed(Setting.ENTITY_DAMAGE_BY_ENTITY)
        } else {
            event.isCancelled = isDisallowed(Setting.ENTITY_DAMAGE_BY_ENVIRONMENT)
        }
    }

    @EventHandler
    private fun onBlockPlace(event: BlockPlaceEvent) {
        event.isCancelled = isDisallowed(Setting.BUILD)
    }

    @EventHandler
    private fun onBlockBreak(event: BlockBreakEvent) {
        event.isCancelled = isDisallowed(Setting.DESTROY)
    }

    @EventHandler
    private fun onFoodLevelChange(event: FoodLevelChangeEvent) {
        event.isCancelled = isDisallowed(Setting.FOOD_LEVEL_CHANGE)
    }

    @EventHandler
    private fun onWeatherChange(event: WeatherChangeEvent) {
        event.isCancelled = isDisallowed(Setting.WEATHER)
    }

    @EventHandler
    private fun onCreatureSpawn(event: CreatureSpawnEvent) {
        event.isCancelled = isDisallowed(Setting.CREATURE_SPAWNING)
    }

    @EventHandler
    private fun onTimeSkip(event: TimeSkipEvent) {
        event.isCancelled = isDisallowed(Setting.DAYLIGHT_CYCLE)
    }

    enum class Setting {
        ENTITY_DAMAGE_BY_ENVIRONMENT,
        ENTITY_DAMAGE_BY_ENTITY,
        BUILD,
        DESTROY,
        FOOD_LEVEL_CHANGE,
        WEATHER,
        CREATURE_SPAWNING,
        DAYLIGHT_CYCLE,
    }

}