package net.danielrickman.buried.state;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.listener.generic.GenericDisallowJoinListener;
import net.danielrickman.api.listener.generic.GenericPreGameListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.TimedState;
import net.danielrickman.api.task.PreGameCountdownTask;
import net.danielrickman.buried.Buried;
import net.danielrickman.buried.listener.BuriedPreGameListener;

import java.util.Arrays;
import java.util.List;

import static net.danielrickman.buried.Buried.PRE_GAME_STATE_DURATION;

public class BuriedPreGameState extends TimedState {

    private final Buried game;

    public BuriedPreGameState(CircuitPlugin plugin, Buried game) {
        super(plugin, PRE_GAME_STATE_DURATION);
        this.game = game;
    }

    @Override
    public List<CircuitListener> getListeners() {
        return Arrays.asList(
                new GenericPreGameListener(getPlugin(), game, game.getMapConfiguration().getSpawnLocations()),
                new GenericDisallowJoinListener(getPlugin()),
                new BuriedPreGameListener(getPlugin(), game)
        );
    }

    @Override
    public void onTimerTick() {
        new PreGameCountdownTask(getTimer()).run();
    }
}
