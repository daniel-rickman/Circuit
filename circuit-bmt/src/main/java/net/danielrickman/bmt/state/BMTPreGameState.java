package net.danielrickman.bmt.state;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.listener.generic.GenericDisallowJoinListener;
import net.danielrickman.api.listener.generic.GenericPreGameListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.TimedState;
import net.danielrickman.api.task.PreGameCountdownTask;
import net.danielrickman.bmt.BuildMyThing;
import net.danielrickman.bmt.listener.BMTPreGameListener;

import java.util.Arrays;
import java.util.List;

public class BMTPreGameState extends TimedState {

    private final BuildMyThing game;

    public BMTPreGameState(CircuitPlugin plugin, BuildMyThing game) {
        super(plugin, BuildMyThing.PRE_GAME_STATE_DURATION);
        this.game = game;
    }

    @Override
    public List<CircuitListener> getListeners() {
        return Arrays.asList(
                new GenericDisallowJoinListener(getPlugin()),
                new GenericPreGameListener(getPlugin(), game, game.getMapConfiguration().getSpawnLocations()),
                new BMTPreGameListener(getPlugin(), game)
        );
    }

    @Override
    public void onTimerTick() {
        new PreGameCountdownTask(getTimer()).run();
    }
}