package net.danielrickman.oitq.listener.pregame;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.util.Logger;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.oitq.OneInTheQuiver;
import net.danielrickman.oitq.configuration.OITQMapConfiguration;
import net.danielrickman.oitq.repository.OITQProfile;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class OITQPreGameTransitionListener extends CircuitListener {

    private final OneInTheQuiver oitq;

    public OITQPreGameTransitionListener(CircuitPlugin plugin, OneInTheQuiver oitq) {
        super(plugin);
        this.oitq = oitq;
    }

    @EventHandler
    private void on(StateStartEvent e) {
        OITQMapConfiguration mapConfiguration = oitq.getMapConfiguration();
        var spawnLocations = mapConfiguration.getSpawnLocations();
        oitq.getWorld().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        if (spawnLocations.isEmpty()) {
            Logger.mapError("No spawn locations found for map: %s", mapConfiguration.getWorldName());
            PlayerUtil.forEach(player -> player.teleport(oitq.getWorld().getSpawnLocation()));
        } else {
            var i = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (spawnLocations.get(i) == null) {
                    i = 0;
                }
                PlayerUtil.reset(player);
                player.teleport(spawnLocations.get(i).toWorldLocation(oitq.getWorld()));
                player.playSound(player.getEyeLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.0f);
                i++;
            }
        }
        PlayerUtil.forEach(player -> {
            oitq.getDescription().forEach(msg -> PlayerUtil.sendMessage(player, oitq.getChatPrefix() + msg));
            oitq.getStats().add(player, new OITQProfile());
        });
    }
}
