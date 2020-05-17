package net.danielrickman.api.listener;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.danielrickman.api.plugin.CircuitPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class CircuitListener implements Listener {

    @Getter
    private final CircuitPlugin plugin;

    public static void enable(CircuitListener listener) {
        Bukkit.getPluginManager().registerEvents(listener, listener.getPlugin());
    }
}