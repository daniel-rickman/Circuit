package net.danielrickman.api.listener;

import lombok.Getter;
import net.danielrickman.api.plugin.CircuitPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class CircuitListener implements Listener {

    @Getter
    private final CircuitPlugin plugin;

    public CircuitListener(CircuitPlugin plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Bukkit.getLogger().info("Enabled Listener: " + this.getClass().getSimpleName());
    }

    public void disable() {
        HandlerList.unregisterAll(this);
        Bukkit.getLogger().info("Disabled Listener: " + this.getClass().getSimpleName());
    }
}