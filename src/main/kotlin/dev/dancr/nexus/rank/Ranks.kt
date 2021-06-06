package dev.dancr.nexus.rank

import dev.dancr.nexus.component.ServerComponent
import dev.dancr.nexus.config.Config
import dev.dancr.nexus.config.ConfigScanner
import dev.dancr.nexus.data.PlayerData
import java.util.UUID
import org.bukkit.Bukkit.getOnlinePlayers
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object Ranks : ServerComponent() {

    @Config("ranks.json")
    data class RankConfiguration(val ranks: List<PlayerRank>)
    data class PlayerRank(
        val name: String,
        val prefixColor: String,
        val showPrefix: Boolean = true,
        val isDefault: Boolean = false,
        val isAdmin: Boolean = false
    )

    private val config = ConfigScanner.getConfig<RankConfiguration>()
    private val rankMap: MutableMap<UUID, PlayerRank> = mutableMapOf()

    override fun onComponentEnable() = getOnlinePlayers().forEach { load(it.uniqueId) }

    override fun onComponentDisable() = rankMap.clear()

    public fun update(uuid: UUID, rankName: String) {
        val rank = getRankOrDefault(rankName)
        transaction {
            PlayerData.update({ PlayerData.uuid eq uuid }) {
                it[PlayerData.rank] = rank.name
            }
        }
        rankMap[uuid] = rank
    }

    public fun getRank(player: Player) : PlayerRank = rankMap[player.uniqueId] ?: getDefaultRank()

    public fun isAdmin(player: Player): Boolean = rankMap[player.uniqueId]?.isAdmin ?: player.isOp

    private fun load(uuid: UUID) = transaction {
        val query = PlayerData.slice(PlayerData.uuid, PlayerData.rank).select { PlayerData.uuid eq uuid }

        if (query.empty()) {
            insert(uuid)
        } else {
            query.first().apply {
                rankMap[this[PlayerData.uuid]] = getRankOrDefault(this[PlayerData.rank])
            }
        }
    }

    private fun insert(uuid: UUID) = transaction {
        PlayerData.insert {
            it[PlayerData.uuid] = uuid
            it[PlayerData.rank] = getDefaultRank().name
        }
        rankMap[uuid] = getDefaultRank()
    }

    private fun getDefaultRank(): PlayerRank = config.ranks.first { rank -> rank.isDefault }

    private fun getRankOrDefault(name: String): PlayerRank =
        config.ranks.firstOrNull { it.name == name } ?: getDefaultRank()

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        load(event.player.uniqueId)
        if (isAdmin(event.player)) {
            event.player.isOp = true
        }
    }
}