package net.danielrickman.oitq.listener.game;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;

public class OITQGameWorldListener extends CircuitListener {

    public OITQGameWorldListener(CircuitPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void on(ProjectileHitEvent e) {
        e.getEntity().remove();
    }
}
