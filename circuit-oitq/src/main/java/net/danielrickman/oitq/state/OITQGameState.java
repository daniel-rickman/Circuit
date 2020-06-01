package net.danielrickman.oitq.state;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.listener.generic.GenericDisallowJoinListener;
import net.danielrickman.api.listener.generic.GenericEliminationListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.TimedState;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.oitq.OneInTheQuiver;
import net.danielrickman.oitq.listener.OITQGameListener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

import static net.danielrickman.oitq.OneInTheQuiver.GAME_STATE_DURATION;

public class OITQGameState extends TimedState {

    private final OneInTheQuiver game;

    public OITQGameState(CircuitPlugin plugin, OneInTheQuiver game) {
        super(plugin, GAME_STATE_DURATION);
        this.game = game;
    }

    @Override
    public List<CircuitListener> getListeners() {
        return Arrays.asList(
                new OITQGameListener(getPlugin(), game, game.getStats()),
                new GenericDisallowJoinListener(getPlugin()),
                new GenericEliminationListener(getPlugin(), game)
        );
    }

    @Override
    public void onTimerTick() {
        var timer = getTimer();
        var alivePlayers = PlayerUtil.getAlivePlayers(getPlugin().getGlobalRepository());
        if (alivePlayers.size() < 2) {
            getPlugin().nextState();
        } else {
            PlayerUtil.forEach(player -> {
                game.getStats().getSidebar(player.getUniqueId()).updateTimer(timer);
            });
            alivePlayers.forEach(player -> {
                if (timer.getTimeLeft() % 5 == 0) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 40, 1, false, false));
                }
            });
        }
    }
}