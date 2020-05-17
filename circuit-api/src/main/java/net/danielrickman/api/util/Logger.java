package net.danielrickman.api.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class Logger {

    public void info(String info, Object... params) {
        Bukkit.getLogger().info("[INFO] " + String.format(info, params));
    }

    public void data(String data, Object... params) {
        Bukkit.getLogger().info("[DATA] " + String.format(data, params));
    }

    public void error(String error, Object... params) {
        Bukkit.getLogger().warning("[ERROR] " + String.format(error, params));
    }

    public void mapInfo(String info, Object... params) {
        Bukkit.getLogger().info("[MAPS] " + String.format(info, params));
    }

    public void mapError(String error, Object... params) {
        Bukkit.getLogger().severe("[MAPS] " + String.format(error, params));
    }

    public void listenerInfo(String info, Object... params) {
        Bukkit.getLogger().info("[LISTENER] " + String.format(info, params));
    }
}