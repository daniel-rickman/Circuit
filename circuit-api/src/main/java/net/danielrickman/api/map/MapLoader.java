package net.danielrickman.api.map;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.inject.Inject;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.plugin.ICircuitModule;
import net.danielrickman.api.util.Logger;
import net.danielrickman.api.util.RandomUtil;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MapLoader {

    private final Gson GSON = new Gson();
    private final CircuitPlugin plugin;
    private final String dataFolderPath;
    private final String worldContainerPath;

    @Inject
    public MapLoader(CircuitPlugin plugin) {
        this.plugin = plugin;
        this.dataFolderPath = plugin.getDataFolder().getAbsolutePath();
        this.worldContainerPath = plugin.getServer().getWorldContainer().getAbsolutePath();
    }

    public <T extends MapConfiguration> Map<T> loadMap(ICircuitModule module, Class<T> clazz) {
        if (!isGameDirectoryPresent(module)) {
            Logger.mapError("Directory not found: /%s", module.getIdentifier());
            return null;
        }

        Logger.mapInfo("Selecting random map for module: %s", module.getIdentifier());
        File zippedMap = getRandomMap(module);

        if (zippedMap != null) {
            Logger.mapInfo("Selected random map file: %s", zippedMap.getName());
            unzip(zippedMap.getAbsolutePath(), String.join("/", worldContainerPath, module.getIdentifier()));

            T mapConfig = null;
            try {
                Logger.mapInfo("Reading map.json file from %s", zippedMap.getName());
                mapConfig = GSON.fromJson(new FileReader(String.join("/", worldContainerPath, module.getIdentifier(), "map.json")), clazz);
            } catch (FileNotFoundException e) {
                Logger.mapError("JSON configuration file not found for %s.zip", zippedMap.getName());
            }

            if (Bukkit.getWorld(module.getIdentifier()) != null) {
                File worldFolder = new File(worldContainerPath + "/" + module.getIdentifier());
                Logger.mapInfo("World called %s already exists. Removing it...", module.getIdentifier());
                Bukkit.unloadWorld(module.getIdentifier(), false);
                worldFolder.delete();
            }
            Logger.mapInfo("Creating world %s", module.getIdentifier());
            World world = new WorldCreator(module.getIdentifier())
                    .environment(World.Environment.NORMAL)
                    .createWorld();
            Bukkit.unloadWorld("world", false); //Unloads the default world loaded on startup
            if (mapConfig != null) {
                loadChunks(world, mapConfig.getChunkRadius());
            }
            return new Map<>(mapConfig, world);
        }

        Logger.mapError("No maps found for module: %s", plugin.getIdentifier());
        return null;
    }

    public void loadGameMaps() {
        var maps = plugin.getMapRepository();
        maps.unloadAll();
        plugin.getLoadedGames().forEach(game -> {
            var map = plugin.getMapLoader().loadMap(game, game.getMapConfigurationClass());
            maps.addMap(game, map);
            Logger.mapInfo("Loaded Map %s for module %s", map.getConfiguration().getWorldName(), game.getStrippedName());
        });
    }

    private void unzip(String source, String destination) {
        try {
            ZipFile zipFile = new ZipFile(source); //New zip file
            zipFile.extractAll(destination); //Extract to destination
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    private File getRandomMap(ICircuitModule module) {
        File moduleDirectory = new File(String.join("/", dataFolderPath, module.getIdentifier()));
        Logger.mapInfo("Checking for directory: %s", moduleDirectory.getAbsolutePath());
        if (moduleDirectory.exists()) {
            File[] mapFiles = moduleDirectory.listFiles();
            if (mapFiles != null && mapFiles.length > 0) {
                Logger.mapInfo("Found map files: %s", Arrays.toString(mapFiles));
                return RandomUtil.randomFrom(mapFiles);
            }
        }
        return null;
    }

    private boolean isGameDirectoryPresent(ICircuitModule module) {
        return new File(plugin.getDataFolder().getAbsolutePath() + "/" + module.getIdentifier()).exists();
    }

    private void loadChunks(World world, int radius) {
        Preconditions.checkNotNull(world);
        var length = (radius * 2) + 1;
        Set<Chunk> chunks = new HashSet<>(length * length);

        var center = world.getSpawnLocation().getChunk();
        var chunkX = center.getX();
        var chunkZ = center.getZ();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                chunks.add(world.getChunkAt(chunkX + x, chunkZ + z));
            }
        }

        chunks.forEach(chunk -> {
            if (!chunk.isLoaded()) {
                Logger.mapInfo("Loaded chunk X=%s Z=%s", chunk.getX(), chunk.getZ());
                chunk.load(false);
            }
        });
    }
}