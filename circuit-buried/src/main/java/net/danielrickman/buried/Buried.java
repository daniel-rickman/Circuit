package net.danielrickman.buried;

import lombok.Getter;
import net.danielrickman.api.plugin.Game;
import net.danielrickman.api.map.MapConfiguration;
import net.danielrickman.api.map.MapRepository;
import net.danielrickman.api.plugin.CircuitGame;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.plugin.GameObjective;
import net.danielrickman.api.state.State;
import net.danielrickman.buried.repository.BuriedRepository;
import net.danielrickman.buried.state.BuriedGameState;
import net.danielrickman.buried.state.BuriedPostGameState;
import net.danielrickman.buried.state.BuriedPreGameState;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

@Game
public class Buried extends CircuitGame {

    public final static int PRE_GAME_STATE_DURATION = 10;
    public final static int GAME_STATE_DURATION = 300;
    public final static int POST_GAME_STATE_DURATION = 8;


    public final static int POINTS_PER_SURVIVAL = 5;
    public final static int COINS_PER_SURVIVAL = 5;
    public final static int POINTS_PER_GOLD_BLOCK = 10;
    public final static int STARTING_BLOCK_FALL_RATE = 10;

    @Getter
    private final BuriedRepository stats;

    public Buried(CircuitPlugin plugin, MapRepository mapRepository) {
        super(plugin, mapRepository);
        this.stats = new BuriedRepository();
    }

    @Override
    public String getIdentifier() {
        return "BURIED";
    }

    @Override
    public String getBoldDisplayName() {
        return ChatColor.YELLOW.toString() + ChatColor.BOLD + "Buried";
    }

    @Override
    public String getDisplayName() {
        return ChatColor.YELLOW + "Buried";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("Welcome to " + ChatColor.GOLD + "Buried" + ChatColor.WHITE + ".",
                "Blocks are falling from the sky. " + ChatColor.RED + "Avoid them!",
                "Earn points by " + ChatColor.GOLD + "surviving " + ChatColor.WHITE + "and mining " + ChatColor.GOLD + "treasure" + ChatColor.WHITE + ".",
                "The player with the most points wins. Good luck!"
                );
    }

    @Override
    public Class<? extends MapConfiguration> getMapConfigurationClass() {
        return BuriedMapConfiguration.class;
    }

    @Override
    public GameObjective getGameObjective() {
        return GameObjective.MOST_POINTS;
    }

    @Override
    public List<State> getStates() {
        return Arrays.asList(
                new BuriedPreGameState(getPlugin(), this),
                new BuriedGameState(getPlugin(), this),
                new BuriedPostGameState(getPlugin(), this)
        );
    }
}