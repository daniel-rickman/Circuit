package net.danielrickman.api.map;

import com.google.gson.Gson;
import com.google.inject.Inject;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.plugin.ICircuitModule;
import net.danielrickman.api.util.RandomUtil;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

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
            Bukkit.getLogger().warning("[MAPS] Directory not found: /" + module.getIdentifier());
            return null;
        }

        Bukkit.getLogger().info("[MAPS] Selecting random map for module: " + module.getIdentifier());
        File zippedMap = getRandomMap(module);

        if (zippedMap != null) {
            Bukkit.getLogger().info("[MAPS] Selected random map file: " + zippedMap.getName());
            unzip(zippedMap.getAbsolutePath(), String.join("/", worldContainerPath, module.getIdentifier()));

            T mapConfig = null;
            try {
                Bukkit.getLogger().info("[MAPS] Reading map.json file");
                mapConfig = GSON.fromJson(new FileReader(String.join("/", worldContainerPath, module.getIdentifier(), "map.json")), clazz);
            } catch (FileNotFoundException e) {
                Bukkit.getLogger().warning("[MAPS] JSON configuration file not found");
            }

            if (Bukkit.getWorld(module.getIdentifier()) != null) {
                Bukkit.getLogger().warning("World called " + module.getIdentifier() + " already exists. Removing...");
                Bukkit.unloadWorld(module.getIdentifier(), false);
            }

            Bukkit.getLogger().info("[MAPS] Creating world: " + module.getIdentifier());
            World world = new WorldCreator(module.getIdentifier())
                    .environment(World.Environment.NORMAL)
                    .createWorld();
            Bukkit.unloadWorld("world", false); //Unloads the default world loaded on startup
            return new Map<>(mapConfig, world);
        }

        Bukkit.getLogger().info("[MAPS] No map found for module: " + plugin.getIdentifier());
        return null;
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
        Bukkit.getLogger().info("[MAPS] Checking for directory: " + moduleDirectory.getAbsolutePath());
        if (moduleDirectory.exists()) {
            File[] mapFiles = moduleDirectory.listFiles();
            if (mapFiles != null && mapFiles.length > 0) {
                Bukkit.getLogger().info("[MAPS] Found Map Files: " + Arrays.toString(mapFiles));
                return RandomUtil.randomFrom(mapFiles);
            }
        }
        return null;
    }

    private boolean isGameDirectoryPresent(ICircuitModule module) {
        return new File(plugin.getDataFolder().getAbsolutePath() + "/" + module.getIdentifier()).exists();
    }
}