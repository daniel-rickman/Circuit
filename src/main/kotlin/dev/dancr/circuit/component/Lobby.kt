package dev.dancr.circuit.component

import dev.dancr.circuit.component.global.WorldSettings
import dev.dancr.circuit.component.lobby.LobbyChat
import dev.dancr.circuit.component.lobby.LobbyJoining
import dev.dancr.circuit.component.lobby.LobbySpawning

object Lobby : TickingComponent() {

    init {
        hardDepend(LobbySpawning)
        hardDepend(LobbyChat)
        hardDepend(LobbyJoining)
        hardDepend(WorldSettings)
    }

    override fun onTick() {
        // todo
    }
}