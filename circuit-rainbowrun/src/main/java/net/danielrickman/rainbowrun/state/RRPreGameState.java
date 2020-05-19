package net.danielrickman.rainbowrun.state;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.TimedState;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.rainbowrun.listener.pregame.RRPreGamePlayerListener;
import net.danielrickman.rainbowrun.listener.pregame.RRPreGameTransitionListener;
import net.danielrickman.rainbowrun.RainbowRun;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.Arrays;
import java.util.List;

public class RRPreGameState extends TimedState {

    private final RainbowRun rr;

    public RRPreGameState(CircuitPlugin plugin, RainbowRun rr) {
        super(plugin, 10);
        this.rr = rr;
    }

    @Override
    public List<CircuitListener> getListeners() {
        return Arrays.asList(
                new RRPreGamePlayerListener(getPlugin(), rr),
                new RRPreGameTransitionListener(getPlugin(), rr)
        );
    }

    @Override
    public void onTimerTick() {
        var remaining = getTimer().getTimeLeft();
        if (remaining < 3) {
            PlayerUtil.forEach(player -> {
                player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 0.5f, 0f);
                player.sendTitle(ChatColor.YELLOW.toString() + ChatColor.BOLD + (remaining + 1), "Game starting in...", 0, 20, 0);
            });
        }
    }
}
