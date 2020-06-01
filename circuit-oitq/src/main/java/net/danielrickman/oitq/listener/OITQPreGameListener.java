package net.danielrickman.oitq.listener;

import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.oitq.OneInTheQuiver;
import net.danielrickman.oitq.repository.OITQProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class OITQPreGameListener extends CircuitListener {

    private final OneInTheQuiver game;

    public OITQPreGameListener(CircuitPlugin plugin, OneInTheQuiver game) {
        super(plugin);
        this.game = game;
    }

    @EventHandler
    private void on(StateStartEvent e) {
        PlayerUtil.forEach(player -> game.getStats().add(player, new OITQProfile()));
    }

    @EventHandler
    public void on(PlayerMoveEvent e) {
        if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getFrom().getZ()) {
            e.getPlayer().teleport(e.getFrom());
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        game.getStats().remove(e.getPlayer());
    }
}