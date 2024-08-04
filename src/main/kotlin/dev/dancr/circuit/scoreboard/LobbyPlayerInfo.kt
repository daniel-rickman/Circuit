package dev.dancr.circuit.scoreboard

import dev.dancr.circuit.component.global.Ranks
import dev.dancr.circuit.config.ConfigScanner
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

object LobbyPlayerInfo : ScoreboardTemplate() {

    private val config = ConfigScanner.getConfig<Ranks.RankConfiguration>()

    override fun updateObjective(scoreboard: Scoreboard, objective: Objective) {
        objective.displayName(Component.text("Circuit")
            .color(NamedTextColor.GOLD)
            .decorate(TextDecoration.BOLD)
            .appendSpace()
            .append(Component.text("(1/2)").color(NamedTextColor.GRAY)))
    }

    override fun updateTeams(scoreboard: Scoreboard) {
        for (rank in config.ranks) {
            val team = scoreboard.registerNewTeam(rank.name)
            team.prefix(Ranks.getPrefix(rank))
            team.color(NamedTextColor.WHITE)
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS)
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
        }
    }

    override fun updateSidebar(scoreboard: Scoreboard, vararg params: Any) {
        val objective = scoreboard.getObjective(DisplaySlot.SIDEBAR) ?: return


    }

}