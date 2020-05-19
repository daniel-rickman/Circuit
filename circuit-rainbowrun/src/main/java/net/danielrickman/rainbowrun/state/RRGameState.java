package net.danielrickman.rainbowrun.state;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.state.TimedState;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.api.util.RandomUtil;
import net.danielrickman.rainbowrun.configuration.RRMapConfiguration;
import net.danielrickman.rainbowrun.listener.game.RRGamePlayerListener;
import net.danielrickman.rainbowrun.listener.game.RRGameTransitionListener;
import net.danielrickman.rainbowrun.RainbowRun;
import net.danielrickman.rainbowrun.repository.RRRepository;
import net.danielrickman.rainbowrun.task.RRBlockBreakTask;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.List;

public class RRGameState extends TimedState {

    private final RainbowRun rr;
    private final GlobalRepository global;
    private final RRRepository stats;

    public RRGameState(CircuitPlugin plugin, RainbowRun rr, GlobalRepository global, RRRepository stats) {
        super(plugin, 300);
        this.rr = rr;
        this.global = global;
        this.stats = stats;
    }

    @Override
    public void onTimerTick() {
        if (PlayerUtil.getAlivePlayers(global).size() <= 1) {
            getPlugin().nextState();
        } else {
            RRMapConfiguration mapConfiguration = rr.getMapConfiguration();
            var timeLeft = getTimer().getTimeLeft();
            var halfway = getTimer().getDuration() / 2;
            PlayerUtil.forEach(player -> stats.getSidebar(player.getUniqueId()).updateTimer(getTimer()));
            if (timeLeft <= halfway) {
                if (timeLeft == halfway) {
                    PlayerUtil.sendToAll(rr.getChatPrefix() + ChatColor.RED + ChatColor.BOLD + "The map is disintegrating! Watch your step!");
                    PlayerUtil.forEach(player -> player.playSound(player.getEyeLocation(), Sound.ENTITY_WITHER_HURT, 0.5f, 0f));
                }
                var region = mapConfiguration.getRegion();
                var blocks = region.getBlocksOfType(rr.getWorld(), RainbowRun.BREAKABLE_MATERIALS);
                if (!(blocks.isEmpty())) {
                    new RRBlockBreakTask(rr.getWorld().getBlockAt(RandomUtil.randomFrom(blocks))).runTaskTimer(getPlugin(), 0, 2);
                }
            }
        }
    }

    @Override
    public List<CircuitListener> getListeners() {
        return List.of(new RRGamePlayerListener(getPlugin(), rr, global, stats), new RRGameTransitionListener(getPlugin(), rr, global));
    }
}