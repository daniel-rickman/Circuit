package dev.dancr.circuit.component.lobby

import dev.dancr.circuit.component.ServerComponent
import dev.dancr.circuit.component.global.Ranks
import io.papermc.paper.chat.ChatRenderer
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler

object LobbyChat : ServerComponent() {

    init {
        hardDepend(Ranks)
    }

    @EventHandler
    fun onAsyncChat(event: AsyncChatEvent) {
        val player = event.player
        val rank = Ranks.getRank(player)

        event.renderer(ChatRenderer.viewerUnaware { _, sourceDisplayName, message ->
            val component = Component.text()

            if (rank.showPrefix) {
                component.append(Ranks.getPrefix(rank))
            }
            component.append(sourceDisplayName.color(NamedTextColor.WHITE))

            component
                .append(Component.text(" \u00BB ").color(NamedTextColor.GRAY))
                .append(message)
                .build()
        })
    }

}