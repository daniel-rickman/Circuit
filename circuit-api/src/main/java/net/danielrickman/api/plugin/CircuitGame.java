package net.danielrickman.api.plugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.danielrickman.api.map.MapConfiguration;
import net.danielrickman.api.map.MapRepository;
import net.danielrickman.api.state.State;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public abstract class CircuitGame implements ICircuitModule {

    @Getter
    private final CircuitPlugin plugin;
    private final MapRepository mapRepository;

    public abstract String getBoldDisplayName();

    public abstract String getDisplayName();

    public String getChatPrefix() {
        return this.getDisplayName() + ChatColor.DARK_GRAY + " \u00BB " + ChatColor.WHITE;
    }

    public String getStrippedName() {
        return ChatColor.stripColor(getDisplayName());
    }

    public List<String> getDescription() {
        return Collections.emptyList();
    }

    public List<State> getStates() {
        return Collections.emptyList();
    }

    public final World getWorld() {
        return Bukkit.getWorld(getIdentifier());
    }

    public abstract Class<? extends MapConfiguration> getMapConfigurationClass();

    /*
    Warning suppressed on line 57
    The map configuration is instantiated using #getMapConfigurationClass() so there's no chance of a Cast Exception
     */
    @SuppressWarnings("unchecked")
    public final <T extends MapConfiguration> T getMapConfiguration() {
        return (T) getMapConfigurationClass().cast(mapRepository.getMap(this).getConfiguration());
    }
}