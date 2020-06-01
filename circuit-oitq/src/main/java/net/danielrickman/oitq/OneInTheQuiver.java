package net.danielrickman.oitq;

import lombok.Getter;
import net.danielrickman.api.plugin.Game;
import net.danielrickman.api.map.MapConfiguration;
import net.danielrickman.api.map.MapRepository;
import net.danielrickman.api.plugin.CircuitGame;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.plugin.GameObjective;
import net.danielrickman.api.state.State;
import net.danielrickman.oitq.repository.OITQRepository;
import net.danielrickman.oitq.state.OITQGameState;
import net.danielrickman.oitq.state.OITQPostGameState;
import net.danielrickman.oitq.state.OITQPreGameState;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

@Game
public class OneInTheQuiver extends CircuitGame {

    public static final int PRE_GAME_STATE_DURATION = 8;
    public static final int GAME_STATE_DURATION = 600;
    public static final int POST_GAME_STATE_DURATION = 8;

    public static final int LIVES = 4;
    public static final int POINTS_PER_KILL = 10;
    public static final int POINTS_PER_SURVIVAL = 5;
    public static final int COINS_PER_KILL = 10;
    public static final int COINS_PER_SURVIVAL = 5;

    @Getter
    private final OITQRepository stats;

    public OneInTheQuiver(CircuitPlugin plugin, MapRepository mapRepository) {
        super(plugin, mapRepository);
        this.stats = new OITQRepository();
    }

    @Override
    public String getIdentifier() {
        return "OITQ";
    }

    @Override
    public String getBoldDisplayName() {
        return ChatColor.GREEN.toString() + ChatColor.BOLD + "OITQ";
    }

    @Override
    public String getDisplayName() {
        return ChatColor.GREEN.toString() + "OITQ";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(
                "Welcome to " + ChatColor.GREEN + "One In The Quiver" + ChatColor.WHITE + ".",
                "You have " + ChatColor.GREEN + "four" + ChatColor.WHITE + " lives.",
                "Arrows" + ChatColor.GREEN + " instantly " + ChatColor.WHITE + "kill players.",
                "The player with the most score wins! Good luck!"
        );
    }

    @Override
    public Class<? extends MapConfiguration> getMapConfigurationClass() {
        return OITQMapConfiguration.class;
    }

    @Override
    public GameObjective getGameObjective() {
        return GameObjective.MOST_POINTS;
    }

    @Override
    public List<State> getStates() {
        return List.of(
                new OITQPreGameState(getPlugin(), this),
                new OITQGameState(getPlugin(), this),
                new OITQPostGameState(getPlugin(), this)
        );
    }
}