package net.danielrickman.rainbowrun;

import lombok.Getter;
import net.danielrickman.api.annotation.Game;
import net.danielrickman.api.map.MapConfiguration;
import net.danielrickman.api.map.MapRepository;
import net.danielrickman.api.plugin.CircuitGame;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.State;
import net.danielrickman.rainbowrun.configuration.RRMapConfiguration;
import net.danielrickman.rainbowrun.repository.RRRepository;
import net.danielrickman.rainbowrun.state.RRGameState;
import net.danielrickman.rainbowrun.state.RRPostGameState;
import net.danielrickman.rainbowrun.state.RRPreGameState;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

@Game
public class RainbowRun extends CircuitGame {

    public final static int COINS_PER_SURVIVAL = 5;
    public final static List<Material> BREAKABLE_MATERIALS = List.of(Material.WHITE_WOOL, Material.GLOWSTONE);

    @Getter
    private final RRRepository stats;

    public RainbowRun(CircuitPlugin plugin, MapRepository mapRepository) {
        super(plugin, mapRepository);
        this.stats = new RRRepository();
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
    public List<State> getStates() {
        return Arrays.asList(new RRPreGameState(getPlugin(), this), new RRGameState(getPlugin(), this, getPlugin().getGlobalRepository(), stats), new RRPostGameState(getPlugin(), getPlugin().getGlobalRepository(), this, stats));
    }
}
