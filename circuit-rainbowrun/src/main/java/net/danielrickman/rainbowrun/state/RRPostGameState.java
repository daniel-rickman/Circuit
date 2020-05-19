package net.danielrickman.rainbowrun.state;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.state.TimedState;
import net.danielrickman.rainbowrun.listener.postgame.RRPostGameTransitionListener;
import net.danielrickman.rainbowrun.RainbowRun;
import net.danielrickman.rainbowrun.repository.RRRepository;

import java.util.Arrays;
import java.util.List;

public class RRPostGameState extends TimedState {

    private final GlobalRepository global;
    private final RainbowRun rr;
    private final RRRepository stats;

    public RRPostGameState(CircuitPlugin plugin, GlobalRepository global, RainbowRun rr, RRRepository stats) {
        super(plugin, 10);
        this.global = global;
        this.rr = rr;
        this.stats = stats;
    }

    @Override
    public void onTimerTick() {

    }

    @Override
    public List<CircuitListener> getListeners() {
        return Arrays.asList(new RRPostGameTransitionListener(getPlugin(), global, rr, stats));
    }
}
