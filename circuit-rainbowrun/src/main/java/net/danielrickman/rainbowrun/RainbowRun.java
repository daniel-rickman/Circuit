package net.danielrickman.rainbowrun;

import lombok.Getter;
import net.danielrickman.api.plugin.Game;
import net.danielrickman.api.map.MapConfiguration;
import net.danielrickman.api.map.MapRepository;
import net.danielrickman.api.plugin.CircuitGame;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.plugin.GameObjective;
import net.danielrickman.api.state.State;
import net.danielrickman.rainbowrun.repository.RainbowRepository;
import net.danielrickman.rainbowrun.state.RainbowGameState;
import net.danielrickman.rainbowrun.state.RainbowPostGameState;
import net.danielrickman.rainbowrun.state.RainbowPreGameState;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

@Game
public class RainbowRun extends CircuitGame {

    public static final int PRE_GAME_STATE_DURATION = 8;
    public static final int GAME_STATE_DURATION = 300;
    public static final int POST_GAME_STATE_DURATION = 8;
    public final static int COINS_PER_SURVIVAL = 5;
    public final static List<Material> BREAKABLE_MATERIALS = List.of(Material.WHITE_WOOL, Material.GLOWSTONE);

    @Getter
    private final RainbowRepository stats;

    public RainbowRun(CircuitPlugin plugin, MapRepository mapRepository) {
        super(plugin, mapRepository);
        this.stats = new RainbowRepository();
    }

    @Override
    public String getIdentifier() {
        return "RR";
    }

    @Override
    public String getBoldDisplayName() {
        return ChatColor.translateAlternateColorCodes('&', "&c&lRa&e&lin&a&lbo&d&lwR&5&lun");
    }

    @Override
    public String getDisplayName() {
        return ChatColor.translateAlternateColorCodes('&', "&cRa&ein&abo&dwR&5un");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(
                "Welcome to " + getDisplayName() + ChatColor.WHITE + ".",
                "The floor is " + ChatColor.RED + "falling " + ChatColor.WHITE + "beneath your feet.",
                "Last player standing will be the winner!"
        );
    }

    @Override
    public Class<? extends MapConfiguration> getMapConfigurationClass() {
        return RRMapConfiguration.class;
    }

    @Override
    public GameObjective getGameObjective() {
        return GameObjective.LAST_MAN_STANDING;
    }

    @Override
    public List<State> getStates() {
        return Arrays.asList(new RainbowPreGameState(getPlugin(), this), new RainbowGameState(getPlugin(), this), new RainbowPostGameState(getPlugin(), this));
    }
}