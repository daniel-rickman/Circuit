package net.danielrickman.api.plugin;

import net.danielrickman.api.map.MapConfiguration;
import net.danielrickman.api.state.State;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;

public abstract class CircuitGame implements ICircuitModule {

    public String getDisplayName() {
        return ChatColor.GRAY + "Game";
    }

    public String getStrippedName() {
        return ChatColor.stripColor(getDisplayName());
    }

    public String getShortName() {
        return "Game";
    }

    public Material getIcon() {
        return Material.MAP;
    }

    public List<String> getDescription() {
        return Collections.emptyList();
    }

    public List<State> getStates() {
        return Collections.emptyList();
    }

    public abstract Class<? extends MapConfiguration> getMapConfigurationClass();

}