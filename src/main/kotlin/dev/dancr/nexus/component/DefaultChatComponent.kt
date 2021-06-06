package dev.dancr.nexus.component

import dev.dancr.nexus.rank.RankManager
import dev.dancr.nexus.team.TeamManager
import io.papermc.paper.chat.ChatRenderer
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

object DefaultChatComponent : ServerComponent() {

    init {
        require(RankManager)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onAsyncChat(event: AsyncChatEvent) {
        if (!RankManager.isEnabled) return

        val player = event.player
        val rank = RankManager.getRank(player)
        val team = TeamManager.get(player)

        event.renderer(ChatRenderer.viewerUnaware { _, sourceDisplayName, message ->
            val component = Component.text()

            if (rank.showPrefix) {
                component.append(Component.text("${rank.name} ").color(TextColor.fromHexString(rank.prefixColor)).decorate(TextDecoration.BOLD))
            }
            component.append(sourceDisplayName.color(if (TeamManager.isEnabled) team.getTextColor() else NamedTextColor.WHITE))

            component
                .append(Component.text(" \u00BB ").color(NamedTextColor.GRAY))
                .append(message)
                .build()
        })
    }
}