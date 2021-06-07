package dev.dancr.nexus.event

import dev.dancr.nexus.rank.Ranks
import org.bukkit.entity.Player

class RankUpdateEvent(val player: Player, val newRank: Ranks.PlayerRank) : NexusEvent()