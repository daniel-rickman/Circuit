package net.danielrickman.oitq.state;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.listener.generic.GenericDisallowJoinListener;
import net.danielrickman.api.listener.generic.GenericPreGameListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.TimedState;
import net.danielrickman.api.task.PreGameCountdownTask;
import net.danielrickman.oitq.OneInTheQuiver;
import net.danielrickman.oitq.listener.OITQPreGameListener;

import java.util.Arrays;
import java.util.List;

import static net.danielrickman.oitq.OneInTheQuiver.PRE_GAME_STATE_DURATION;

public class OITQPreGameState extends TimedState {

    private final OneInTheQuiver game;

    public OITQPreGameState(CircuitPlugin plugin, OneInTheQuiver game) {
        super(plugin, PRE_GAME_STATE_DURATION);
        this.game = game;
    }

    @Override
    public List<CircuitListener> getListeners() {
        return Arrays.asList(
                new GenericPreGameListener(getPlugin(), game, game.getMapConfiguration().getSpawnLocations()),
                new GenericDisallowJoinListener(getPlugin()),
                new OITQPreGameListener(getPlugin(), game)
        );
    }

    @Override
    public void onTimerTick() {
        new PreGameCountdownTask(getTimer()).run();
    }
}