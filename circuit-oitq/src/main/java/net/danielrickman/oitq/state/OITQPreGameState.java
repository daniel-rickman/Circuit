package net.danielrickman.oitq.state;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.TimedState;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.oitq.OneInTheQuiver;
import net.danielrickman.oitq.listener.pregame.OITQPreGamePlayerListener;
import net.danielrickman.oitq.listener.pregame.OITQPreGameTransitionListener;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.Arrays;
import java.util.List;

public class OITQPreGameState extends TimedState {

    private final OneInTheQuiver oitq;

    public OITQPreGameState(CircuitPlugin plugin, OneInTheQuiver oitq) {
        super(plugin, 8);
        this.oitq = oitq;
    }

    @Override
    public List<CircuitListener> getListeners() {
        return Arrays.asList(
                new OITQPreGamePlayerListener(getPlugin(), oitq),
                new OITQPreGameTransitionListener(getPlugin(), oitq)
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