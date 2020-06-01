package net.danielrickman.bmt;

import lombok.Getter;
import net.danielrickman.api.map.MapConfiguration;
import net.danielrickman.api.map.MapRepository;
import net.danielrickman.api.plugin.CircuitGame;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.plugin.Game;
import net.danielrickman.api.plugin.GameObjective;
import net.danielrickman.api.state.State;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.bmt.repository.BMTRepository;
import net.danielrickman.bmt.state.BMTPostGameState;
import net.danielrickman.bmt.state.BMTPreGameState;
import net.danielrickman.bmt.state.BMTRoundState;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Game
public class BuildMyThing extends CircuitGame {

    public final static int PRE_GAME_STATE_DURATION = 5;
    public final static int ROUND_DURATION = 60;
    public final static int POST_GAME_STATE_DURATION = 8;
    public final static int DELAY_BETWEEN_ROUNDS_IN_TICKS = 25;

    public final static int POINTS_FOR_FIRST_GUESS = 10;
    public final static int POINTS_FOR_BUILDER_PER_GUESS = 2;
    public final static int MINIMUM_POINTS_FOR_GUESS = 2;

    @Getter
    private final List<String> builtWords = new ArrayList<>();

    @Getter
    private final BMTRepository stats;

    public BuildMyThing(CircuitPlugin plugin, MapRepository mapRepository) {
        super(plugin, mapRepository);
        stats = new BMTRepository();
    }

    @Override
    public String getIdentifier() {
        return "BMT";
    }

    @Override
    public String getBoldDisplayName() {
        return ChatColor.AQUA.toString() + ChatColor.BOLD + "Build My Thing";
    }

    @Override
    public String getDisplayName() {
        return ChatColor.AQUA + "Build My Thing";
    }

    @Override
    public String getChatPrefix() {
        return ChatColor.AQUA + "BMT" + ChatColor.DARK_GRAY + " \u00BB " + ChatColor.WHITE;
    }

    @Override
    public Class<? extends MapConfiguration> getMapConfigurationClass() {
        return BMTMapConfiguration.class;
    }

    @Override
    public GameObjective getGameObjective() {
        return GameObjective.MOST_POINTS;
    }

    @Override
    public List<State> getStates() {
        List<State> states = new ArrayList<>();
        states.add(new BMTPreGameState(getPlugin(), this));
        PlayerUtil.forEach(player -> {
            states.add(new BMTRoundState(getPlugin(), this, player));
        });
        states.add(new BMTPostGameState(getPlugin(), this));
        return states;
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(
                "Welcome to " + ChatColor.YELLOW + "Build My Thing" + ChatColor.WHITE + "!",
                "Each player will get a chance to draw a " + ChatColor.YELLOW + "random word" + ChatColor.WHITE + ".",
                "Earn points by " + ChatColor.YELLOW + "guessing peoples builds" + ChatColor.WHITE + ".",
                "You also earn points when other players " + ChatColor.YELLOW + "guess your build!" + ChatColor.WHITE + ".",
                "The player with the most points wins!"
        );
    }
}
