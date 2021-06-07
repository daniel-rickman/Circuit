package dev.dancr.nexus.component

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard

/**
 * Used to create and destroy player scoreboards.
 * Any sidebar-related shenanigans should be in a separate [TODO] object.
 */
object ScoreboardCreation : ServerComponent() {

    private val scoreboardMap: MutableMap<Player, Scoreboard> = mutableMapOf()

    override fun onComponentDisable() {
        scoreboardMap.values.forEach { it.clearSlot(DisplaySlot.SIDEBAR) }
        scoreboardMap.clear()
    }

    private fun createScoreboard() : Scoreboard {
        val scoreboard = Bukkit.getScoreboardManager().newScoreboard
        //val objective = scoreboard.registerNewObjective("scoreboard", "dummy", Component.text("scoreboard"))
        //objective.displaySlot = DisplaySlot.SIDEBAR
        return scoreboard
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        with(createScoreboard()) {
            scoreboardMap[player] = this
            player.scoreboard = this
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        scoreboardMap[player]!!.clearSlot(DisplaySlot.SIDEBAR)
        scoreboardMap -= player
    }

}