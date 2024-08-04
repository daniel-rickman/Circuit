package dev.dancr.circuit.component.global

import dev.dancr.circuit.component.ServerComponent
import dev.dancr.circuit.config.Config
import dev.dancr.circuit.config.ConfigScanner
import dev.dancr.circuit.event.RankUpdateEvent
import dev.dancr.circuit.player.PlayerData
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.*

object Ranks : ServerComponent() {

    @Config("ranks")
    data class RankConfiguration(val ranks: List<PlayerRank>)

    data class PlayerRank(
        val name: String,
        val prefixColor: String,
        val showPrefix: Boolean,
        val isDefault: Boolean = false,
        val isAdmin: Boolean = false
    )

    private val config = ConfigScanner.getConfig<RankConfiguration>()
    private val ranks: MutableMap<UUID, PlayerRank> = mutableMapOf()

    override fun onComponentEnable() = Bukkit.getOnlinePlayers().forEach { load(it.uniqueId) }

    override fun onComponentDisable() = ranks.clear()

    public fun update(uuid: UUID, rankName: String) {
        val rank = getRankOrDefault(rankName)
        transaction {
            PlayerData.update({ PlayerData.uuid eq uuid }) {
                it[PlayerData.rank] = rank.name
            }
        }
        Bukkit.getPlayer(uuid)?.let { RankUpdateEvent(it, ranks[uuid]!!, rank).callEvent() }
        ranks[uuid] = rank
    }

    fun getRank(player: Player) : PlayerRank = ranks[player.uniqueId] ?: getDefaultRank()

    fun isAdmin(player: Player): Boolean = ranks[player.uniqueId]?.isAdmin ?: player.isOp

    private fun load(uuid: UUID) = transaction {
        val query = PlayerData.slice(PlayerData.uuid, PlayerData.rank).select { PlayerData.uuid eq uuid }

        if (query.empty()) {
            insert(uuid)
        } else {
            query.first().apply {
                ranks[this[PlayerData.uuid]] = getRankOrDefault(this[PlayerData.rank])
            }
        }
    }

    private fun insert(uuid: UUID) = transaction {
        PlayerData.insert {
            it[PlayerData.uuid] = uuid
            it[PlayerData.rank] = getDefaultRank().name
        }
        ranks[uuid] = getDefaultRank()
    }

    private fun getDefaultRank(): PlayerRank = config.ranks.first { rank -> rank.isDefault }

    private fun getRankOrDefault(name: String): PlayerRank =
        config.ranks.firstOrNull { it.name == name } ?: getDefaultRank()

    fun getPrefix(rank: PlayerRank): Component = Component.text("${rank.name} ").color(TextColor.fromHexString(rank.prefixColor)).decorate(TextDecoration.BOLD)

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        load(event.player.uniqueId)
        event.player.isOp = isAdmin(event.player)
    }
}