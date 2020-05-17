package net.danielrickman.oitq.state;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.TimedState;
import net.danielrickman.oitq.OneInTheQuiver;
import net.danielrickman.oitq.listener.postgame.OITQPostGameTransitionListener;
import net.danielrickman.oitq.repository.OITQRepository;

import java.util.Collections;
import java.util.List;

public class OITQPostGameState extends TimedState {

    private final OneInTheQuiver oitq;
    private final OITQRepository stats;

    public OITQPostGameState(CircuitPlugin plugin, OneInTheQuiver oitq, OITQRepository stats) {
        super(plugin, 5);
        this.oitq = oitq;
        this.stats = stats;
    }

    @Override
    public List<CircuitListener> getListeners() {
        return Collections.singletonList(
                new OITQPostGameTransitionListener(getPlugin(), oitq, stats)
        );
    }

    @Override
    public void onTimerTick() {
    }
}
