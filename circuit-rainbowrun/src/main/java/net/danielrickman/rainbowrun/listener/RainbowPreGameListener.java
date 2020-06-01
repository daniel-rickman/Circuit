package net.danielrickman.rainbowrun.listener;

import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.api.util.RandomUtil;
import net.danielrickman.rainbowrun.RainbowRun;
import net.danielrickman.rainbowrun.RRMapConfiguration;
import net.danielrickman.rainbowrun.repository.RainbowProfile;
import net.danielrickman.rainbowrun.task.RainbowBlockBreakTask;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RainbowPreGameListener extends CircuitListener {

    private final RainbowRun game;

    public RainbowPreGameListener(CircuitPlugin plugin, RainbowRun game) {
        super(plugin);
        this.game = game;
    }

    @EventHandler
    private void on(StateStartEvent e) {
        PlayerUtil.forEach(player -> {
            Bukkit.getPluginManager().callEvent(new PlayerMoveEvent(player, player.getLocation(), player.getLocation()));
            game.getStats().add(player, new RainbowProfile());
        });
    }

    @EventHandler
    public void on(PlayerMoveEvent e) {
        RRMapConfiguration mapConfiguration = game.getMapConfiguration();
        if (e.getTo().getY() <= mapConfiguration.getDeathY()) {
            var location = RandomUtil.randomFrom(mapConfiguration.getSpawnLocations()).toWorldLocation(game.getWorld());
            e.getPlayer().teleport(location);
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        game.getStats().remove(e.getPlayer());
    }
}
