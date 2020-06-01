package net.danielrickman.rainbowrun.state;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.listener.generic.GenericDisallowJoinListener;
import net.danielrickman.api.listener.generic.GenericEliminationListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.TimedState;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.api.util.RandomUtil;
import net.danielrickman.rainbowrun.RRMapConfiguration;
import net.danielrickman.rainbowrun.RainbowRun;
import net.danielrickman.rainbowrun.listener.RainbowGameListener;
import net.danielrickman.rainbowrun.task.RainbowBlockBreakTask;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.List;

import static net.danielrickman.rainbowrun.RainbowRun.GAME_STATE_DURATION;

public class RainbowGameState extends TimedState {

    private final RainbowRun game;

    public RainbowGameState(CircuitPlugin plugin, RainbowRun game) {
        super(plugin, GAME_STATE_DURATION);
        this.game = game;
    }

    @Override
    public void onTimerTick() {
        if (PlayerUtil.getAlivePlayers(getPlugin().getGlobalRepository()).size() <= 1) {
            getPlugin().nextState();
        } else {
            RRMapConfiguration mapConfiguration = game.getMapConfiguration();
            var timeLeft = getTimer().getTimeLeft();
            var halfway = getTimer().getDuration() / 2;
            PlayerUtil.forEach(player -> game.getStats().getSidebar(player.getUniqueId()).updateTimer(getTimer()));
            if (timeLeft <= halfway) {
                if (timeLeft == halfway) {
                    PlayerUtil.sendToAll(game.getChatPrefix() + ChatColor.RED + ChatColor.BOLD + "The map is disintegrating! Watch your step!");
                    PlayerUtil.forEach(player -> player.playSound(player.getEyeLocation(), Sound.ENTITY_WITHER_HURT, 0.5f, 0f));
                }
                var region = mapConfiguration.getRegion();
                var blocks = region.getBlocksOfType(game.getWorld(), RainbowRun.BREAKABLE_MATERIALS);
                if (!(blocks.isEmpty())) {
                    new RainbowBlockBreakTask(game.getWorld().getBlockAt(RandomUtil.randomFrom(blocks))).runTaskTimer(getPlugin(), 0, 2);
                }
            }
        }
    }

    @Override
    public List<CircuitListener> getListeners() {
        return List.of(
                new RainbowGameListener(getPlugin(), game),
                new GenericEliminationListener(getPlugin(), game),
                new GenericDisallowJoinListener(getPlugin())
        );
    }
}