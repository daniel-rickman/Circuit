package dev.dancr.nexus.team

import dev.dancr.nexus.component.ServerComponent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object TeamManager : ServerComponent() {

    private val teamMap: MutableMap<Player, Team> = mutableMapOf()
    private var activeTeams: MutableList<Team> = mutableListOf()
    private var memberLimit = 1

    override fun onComponentEnable() {
        memberLimit = 1
        setActiveTeams(Team.values().size)
    }

    override fun onComponentDisable() {
        teamMap.clear()
    }

    public fun set(player: Player, team: Team) = teamMap.put(player, team)

    public fun get(player: Player) : Team = teamMap[player] ?: Team.NONE

    public fun nextAvailableTeam(limit: Int) : Team = Team.values().first { team -> teamMap.count { it.value == team } < limit || team.isUnrestricted }

    public fun setMemberLimit(limit: Int) {
        memberLimit = limit
    }

    public fun setActiveTeams(amount: Int) {
        activeTeams.clear()
        activeTeams.addAll(Team.values().take(amount) + Team.NONE)

        activeTeams.removeIf { !it.isUnrestricted && !activeTeams.contains(it) }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        set(event.player, nextAvailableTeam(memberLimit))
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        teamMap -= event.player
    }

    enum class Team(private val hexString: String, public val isUnrestricted: Boolean = false) {
        RED("#FF5555"),
        BLUE("#5555FF"),
        GREEN("#55FF55"),
        YELLOW("#DDD605"),
        NONE("#AAAAAA", true);

        public fun getTextColor() = TextColor.fromHexString(hexString)
    }
}