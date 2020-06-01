package net.danielrickman.bmt.state;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.listener.generic.GenericDisallowJoinListener;
import net.danielrickman.api.listener.generic.GenericPostGameListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.TimedState;
import net.danielrickman.bmt.BuildMyThing;
import net.danielrickman.bmt.listener.BMTPostGameListener;
import net.danielrickman.bmt.repository.BMTRepository;

import java.util.Arrays;
import java.util.List;

import static net.danielrickman.bmt.BuildMyThing.POST_GAME_STATE_DURATION;

public class BMTPostGameState extends TimedState {

    private final BuildMyThing game;
    private final BMTRepository stats;

    public BMTPostGameState(CircuitPlugin plugin, BuildMyThing game) {
        super(plugin, POST_GAME_STATE_DURATION);
        this.game = game;
        this.stats = game.getStats();
    }

    @Override
    public List<CircuitListener> getListeners() {
        return Arrays.asList(
                new GenericDisallowJoinListener(getPlugin()),
                new GenericPostGameListener(getPlugin(), game, stats),
                new BMTPostGameListener(getPlugin(), game)
        );
    }

    @Override
    public void onTimerTick() {

    }
}