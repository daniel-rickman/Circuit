package net.danielrickman.oitq;

import net.danielrickman.api.annotation.Game;
import net.danielrickman.api.map.MapConfiguration;
import net.danielrickman.api.plugin.CircuitGame;
import net.danielrickman.api.state.State;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;

@Game
public class OneInTheQuiver extends CircuitGame {

    @Override
    public Class<? extends MapConfiguration> getMapConfigurationClass() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "OITQ";
    }

    @Override
    public String getDisplayName() {
        return ChatColor.GREEN.toString() + ChatColor.BOLD + "OITQ";
    }

    @Override
    public String getStrippedName() {
        return ChatColor.stripColor(getDisplayName());
    }

    @Override
    public Material getIcon() {
        return Material.MAP;
    }

    @Override
    public List<String> getDescription() {
        return Collections.emptyList();
    }

    @Override
    public List<State> getStates() {
        return Collections.emptyList();
    }
}
