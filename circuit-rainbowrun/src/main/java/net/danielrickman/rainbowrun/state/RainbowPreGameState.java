package net.danielrickman.rainbowrun.state;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.listener.generic.GenericDisallowJoinListener;
import net.danielrickman.api.listener.generic.GenericPreGameListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.TimedState;
import net.danielrickman.api.task.PreGameCountdownTask;
import net.danielrickman.rainbowrun.listener.RainbowPreGameListener;
import net.danielrickman.rainbowrun.RainbowRun;

import java.util.Arrays;
import java.util.List;

import static net.danielrickman.rainbowrun.RainbowRun.PRE_GAME_STATE_DURATION;

public class RainbowPreGameState extends TimedState {

    private final RainbowRun rr;

    public RainbowPreGameState(CircuitPlugin plugin, RainbowRun rr) {
        super(plugin, PRE_GAME_STATE_DURATION);
        this.rr = rr;
    }

    @Override
    public List<CircuitListener> getListeners() {
        return Arrays.asList(
                new GenericPreGameListener(getPlugin(), rr, rr.getMapConfiguration().getSpawnLocations()),
                new GenericDisallowJoinListener(getPlugin()),
                new RainbowPreGameListener(getPlugin(), rr)
        );
    }

    @Override
    public void onTimerTick() {
        new PreGameCountdownTask(getTimer()).run();
    }
}
