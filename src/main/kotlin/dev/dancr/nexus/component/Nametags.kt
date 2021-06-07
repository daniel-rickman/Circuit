package dev.dancr.nexus.component

import dev.dancr.nexus.event.RankUpdateEvent
import dev.dancr.nexus.rank.Ranks
import dev.dancr.nexus.team.Teams
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit.getOnlinePlayers
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

object NameTags : ServerComponent() {

    init {
        require(Ranks)
        require(ScoreboardCreation)
    }

    override fun onComponentEnable() = reloadAll()

    override fun onComponentDisable() = getOnlinePlayers().forEach { unregisterTeams(it.scoreboard) }

    private fun reloadAll() {
        for (player in getOnlinePlayers()) {
            unregisterTeams(player.scoreboard)
            getOnlinePlayers().forEach {
                addTeam(it, player.scoreboard)
            }
        }
    }

    private fun addTeam(player: Player, scoreboard: Scoreboard) {
        if (scoreboard.getTeam(player.name) != null) return

        val team = scoreboard.registerNewTeam(player.name)
        val rank = Ranks.getRank(player)

        team.prefix(Ranks.getPrefix(rank).append(Component.text()))
        team.color(if (Teams.isEnabled && rank.showPrefix) Teams.get(player).getNamedTextColor() else NamedTextColor.WHITE)
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
        team.addEntry(player.name)
    }

    private fun unregisterTeams(scoreboard: Scoreboard) = scoreboard.teams.forEach { it.unregister() }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) = reloadAll()

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        for (player in getOnlinePlayers()) {
            player.scoreboard.teams.filter { it.name == event.player.name }.forEach { it.unregister() }
        }
    }

    @EventHandler
    fun onRankUpdate(event: RankUpdateEvent) = reloadAll()
}