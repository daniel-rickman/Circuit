package net.danielrickman.rainbowrun.listener.pregame;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.util.Logger;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.rainbowrun.configuration.RRMapConfiguration;
import net.danielrickman.rainbowrun.RainbowRun;
import net.danielrickman.rainbowrun.repository.RRProfile;
import net.danielrickman.rainbowrun.task.RRBlockBreakTask;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

public class RRPreGameTransitionListener extends CircuitListener {

    private final RainbowRun rr;

    public RRPreGameTransitionListener(CircuitPlugin plugin, RainbowRun rr) {
        super(plugin);
        this.rr = rr;
    }

    @EventHandler
    private void on(StateStartEvent e) {
        RRMapConfiguration mapConfiguration = rr.getMapConfiguration();
        var spawnLocation = mapConfiguration.getSpawnLocation();
        if (spawnLocation == null) {
            Logger.mapError("Spawn location not found for %s", mapConfiguration.getWorldName());
            PlayerUtil.forEach(player -> player.teleport(rr.getWorld().getSpawnLocation()));
        } else {
            PlayerUtil.forEach(player -> {
                PlayerUtil.reset(player);
                player.teleport(spawnLocation.toWorldLocation(rr.getWorld()));
                player.playSound(player.getEyeLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.0f);
            });
            new RRBlockBreakTask(spawnLocation.toWorldLocation(rr.getWorld()).getBlock());
        }
        PlayerUtil.forEach(player -> {
            rr.getDescription().forEach(msg -> PlayerUtil.sendMessage(player, rr.getChatPrefix() + msg));
            rr.getStats().add(player, new RRProfile());
        });
    }
}