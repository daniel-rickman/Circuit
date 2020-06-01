package net.danielrickman.api.listener.generic;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.map.MapLocation;
import net.danielrickman.api.plugin.CircuitGame;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.util.Logger;
import net.danielrickman.api.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.List;

public class GenericPreGameListener extends CircuitListener {

    private final CircuitGame game;
    private final List<MapLocation> spawnLocations;

    public GenericPreGameListener(CircuitPlugin plugin, CircuitGame game, List<MapLocation> spawnLocations) {
        super(plugin);
        this.game = game;
        this.spawnLocations = spawnLocations;
    }

    @EventHandler
    private void on(StateStartEvent e) {
        if (spawnLocations.isEmpty()) {
            Logger.mapError("No spawn locations found for map: %s", getPlugin().getMapRepository().getMap(game).getConfiguration().getWorldName());
            PlayerUtil.forEach(player -> player.teleport(game.getWorld().getSpawnLocation()));
        } else {
            var i = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (i >= spawnLocations.size()) {
                    i = 0;
                }
                PlayerUtil.reset(player);
                player.teleport(spawnLocations.get(i).toWorldLocation(game.getWorld()).toCenterLocation());
                player.playSound(player.getEyeLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.0f);
                i++;
            }
        }
        PlayerUtil.forEach(player -> {
            game.getDescription().forEach(msg -> PlayerUtil.sendMessage(player, game.getChatPrefix() + msg));
        });
    }
}