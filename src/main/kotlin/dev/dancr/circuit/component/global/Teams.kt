package dev.dancr.circuit.component.global

import dev.dancr.circuit.component.ServerComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

//todo Rewrite this to use a variable team configuration. Current system isn't flexible enough.
object Teams : ServerComponent() {

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

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        set(event.player, nextAvailableTeam(memberLimit))
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        teamMap -= event.player
    }

    enum class Team(private val hexString: String, public val namedTextColor: NamedTextColor, val isUnrestricted: Boolean = false) {
        RED("#FF5555", NamedTextColor.RED),
        BLUE("#5555FF", NamedTextColor.BLUE),
        GREEN("#55FF55", NamedTextColor.GREEN),
        YELLOW("#DDD605", NamedTextColor.YELLOW),
        NONE("#AAAAAA", NamedTextColor.GRAY, true);

        public fun getTextColor() = TextColor.fromHexString(hexString)
    }
}