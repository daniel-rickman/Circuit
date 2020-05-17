package net.danielrickman.oitq.listener.pregame;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.oitq.OneInTheQuiver;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class OITQPreGamePlayerListener extends CircuitListener {

    private final OneInTheQuiver oitq;

    public OITQPreGamePlayerListener(CircuitPlugin plugin, OneInTheQuiver oitq) {
        super(plugin);
        this.oitq = oitq;
    }

    @EventHandler
    public void on(PlayerLoginEvent e) {
        e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server is not joinable at this time");
    }

    @EventHandler
    public void on(PlayerMoveEvent e) {
        if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getFrom().getZ()) {
            e.getPlayer().teleport(e.getFrom());
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        oitq.getStats().remove(e.getPlayer());
    }
}