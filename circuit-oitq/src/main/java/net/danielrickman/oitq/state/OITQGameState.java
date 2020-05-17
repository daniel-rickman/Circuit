package net.danielrickman.oitq.state;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.state.TimedState;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.oitq.OneInTheQuiver;
import net.danielrickman.oitq.listener.game.OITQGameCombatListener;
import net.danielrickman.oitq.listener.game.OITQGamePlayerListener;
import net.danielrickman.oitq.listener.game.OITQGameTransitionListener;
import net.danielrickman.oitq.listener.game.OITQGameWorldListener;
import net.danielrickman.oitq.repository.OITQRepository;

import java.util.Arrays;
import java.util.List;

public class OITQGameState extends TimedState {

    private final OneInTheQuiver oitq;
    private final GlobalRepository global;
    private final OITQRepository stats;

    public OITQGameState(CircuitPlugin plugin, OneInTheQuiver oitq, GlobalRepository global, OITQRepository stats) {
        super(plugin, 600);
        this.oitq = oitq;
        this.global = global;
        this.stats = stats;
    }

    @Override
    public List<CircuitListener> getListeners() {
        return Arrays.asList(
                new OITQGameCombatListener(getPlugin(), oitq, global, stats),
                new OITQGamePlayerListener(getPlugin(), oitq, global, stats),
                new OITQGameTransitionListener(getPlugin(), oitq, global),
                new OITQGameWorldListener(getPlugin())
        );
    }

    @Override
    public void onTimerTick() {
        var timer = getTimer();
        var alivePlayers = PlayerUtil.getAlivePlayers(global);
        if (alivePlayers.size() < 2) {
            getPlugin().nextState();
        } else {
            PlayerUtil.forEach(player -> {
                oitq.getStats().getSidebar(player.getUniqueId()).updateTimer(timer);
            });
            //alivePlayers.forEach(player -> {
            //    if (timer.getTimeLeft() % 5 == 0) {
            //        //todo Glowing
            //    }
            //});
        }
    }
}