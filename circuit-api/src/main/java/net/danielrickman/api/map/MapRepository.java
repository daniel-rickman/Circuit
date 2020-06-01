package net.danielrickman.api.map;

import net.danielrickman.api.plugin.CircuitGame;
import net.danielrickman.api.util.Logger;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Singleton
public class MapRepository {

    private final HashMap<CircuitGame, Map<? extends MapConfiguration>> gameMaps = new HashMap<>();

    public void addMap(CircuitGame game, Map<? extends MapConfiguration> map) {
        gameMaps.put(game, map);
    }

    public Map<? extends MapConfiguration> getMap(CircuitGame game) {
        for (java.util.Map.Entry<CircuitGame, Map<? extends MapConfiguration>> entry : gameMaps.entrySet()) {
            if (entry.getKey().getIdentifier().equals(game.getIdentifier())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void unloadAll() {
        if (!gameMaps.isEmpty()) {
            gameMaps.forEach((game, map) -> {
                if (map.getWorld() != null && Bukkit.getServer().getWorlds().contains(map.getWorld())) {
                    Logger.mapInfo("Unloaded world: %s", map.getWorld().getName());
                    Bukkit.getServer().unloadWorld(map.getWorld(), false);
                }
            });
        }
    }
}