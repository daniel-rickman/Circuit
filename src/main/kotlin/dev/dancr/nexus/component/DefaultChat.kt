package dev.dancr.nexus.component

import dev.dancr.nexus.rank.Ranks
import dev.dancr.nexus.team.Teams
import io.papermc.paper.chat.ChatRenderer
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

object DefaultChat : ServerComponent() {

    init {
        require(Ranks)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onAsyncChat(event: AsyncChatEvent) {
        if (!Ranks.isEnabled) return

        val player = event.player
        val rank = Ranks.getRank(player)
        val team = Teams.get(player)

        event.renderer(ChatRenderer.viewerUnaware { _, sourceDisplayName, message ->
            val component = Component.text()

            if (rank.showPrefix) {
                component.append(Ranks.getPrefix(rank))
            }
            component.append(sourceDisplayName.color(if (Teams.isEnabled) team.getTextColor() else NamedTextColor.WHITE))

            component
                .append(Component.text(" \u00BB ").color(NamedTextColor.GRAY))
                .append(message)
                .build()
        })
    }
}