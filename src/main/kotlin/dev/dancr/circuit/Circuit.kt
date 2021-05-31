package dev.dancr.circuit

import dev.dancr.circuit.component.LobbyLaunchComponent
import dev.dancr.nexus.plugin.NexusPlugin

class Circuit : NexusPlugin() {

    override fun onPluginEnable() {
        LobbyLaunchComponent.enable()
    }

}