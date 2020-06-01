package net.danielrickman.bmt.listener;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.bmt.BuildMyThing;
import net.danielrickman.bmt.repository.BMTProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class BMTPreGameListener extends CircuitListener {

    private final BuildMyThing game;

    public BMTPreGameListener(CircuitPlugin plugin, BuildMyThing game) {
        super(plugin);
        this.game = game;
    }

    @EventHandler
    private void on(StateStartEvent e) {
        PlayerUtil.forEach(player -> game.getStats().add(player, new BMTProfile()));
    }

    @EventHandler
    private void on(PlayerQuitEvent e) {
        game.getStats().remove(e.getPlayer());
    }
}