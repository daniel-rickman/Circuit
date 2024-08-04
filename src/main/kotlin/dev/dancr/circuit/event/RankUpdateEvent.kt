package dev.dancr.circuit.event

import dev.dancr.circuit.component.global.Ranks
import org.bukkit.entity.Player

class RankUpdateEvent(val player: Player, val from: Ranks.PlayerRank, val to: Ranks.PlayerRank) : CircuitEvent()