package net.danielrickman.buried.listener;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.buried.Buried;
import net.danielrickman.buried.repository.BuriedProfile;
import org.bukkit.event.EventHandler;

public class BuriedPreGameListener extends CircuitListener {

    private final Buried buried;

    public BuriedPreGameListener(CircuitPlugin plugin, Buried buried) {
        super(plugin);
        this.buried = buried;
    }

    @EventHandler
    private void on(StateStartEvent e) {
        PlayerUtil.forEach(player -> buried.getStats().getPlayerMap().put(player.getUniqueId(), new BuriedProfile()));
    }
}