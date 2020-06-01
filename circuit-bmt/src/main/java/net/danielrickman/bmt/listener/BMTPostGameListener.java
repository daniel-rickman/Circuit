package net.danielrickman.bmt.listener;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.bmt.BuildMyThing;
import org.bukkit.event.EventHandler;

public class BMTPostGameListener extends CircuitListener {

    private final BuildMyThing game;

    public BMTPostGameListener(CircuitPlugin plugin, BuildMyThing game) {
        super(plugin);
        this.game = game;
    }

    @EventHandler
    private void on(StateStartEvent e) {
        PlayerUtil.forEach(PlayerUtil::reset);
    }
}