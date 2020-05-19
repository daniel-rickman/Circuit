package net.danielrickman.rainbowrun.listener.pregame;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.rainbowrun.configuration.RRMapConfiguration;
import net.danielrickman.rainbowrun.RainbowRun;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RRPreGamePlayerListener extends CircuitListener {

    private final RainbowRun rr;

    public RRPreGamePlayerListener(CircuitPlugin plugin, RainbowRun rr) {
        super(plugin);
        this.rr = rr;
    }

    @EventHandler
    public void on(PlayerLoginEvent e) {
        e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server is not joinable at this time");
    }

    @EventHandler
    public void on(PlayerMoveEvent e) {
        RRMapConfiguration mapConfiguration = rr.getMapConfiguration();
        if (e.getTo().getY() <= mapConfiguration.getDeathY()) {
            e.getPlayer().teleport(mapConfiguration.getSpawnLocation().toWorldLocation(rr.getWorld()));
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        rr.getStats().remove(e.getPlayer());
    }
}
