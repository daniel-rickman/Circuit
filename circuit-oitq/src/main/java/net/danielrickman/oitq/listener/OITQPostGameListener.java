package net.danielrickman.oitq.listener;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.oitq.OneInTheQuiver;
import net.danielrickman.oitq.repository.OITQRepository;
import org.bukkit.event.EventHandler;

public class OITQPostGameListener extends CircuitListener {

    private final OneInTheQuiver oitq;
    private final OITQRepository stats;

    public OITQPostGameListener(CircuitPlugin plugin, OneInTheQuiver oitq) {
        super(plugin);
        this.oitq = oitq;
        this.stats = oitq.getStats();
    }

    @EventHandler
    private void on(StateStartEvent e) {
        PlayerUtil.forEach(player -> stats.getSidebar(player.getUniqueId()).destroy());
    }
}