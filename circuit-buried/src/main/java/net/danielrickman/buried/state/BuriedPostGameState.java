package net.danielrickman.buried.state;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.listener.generic.GenericDisallowJoinListener;
import net.danielrickman.api.listener.generic.GenericPostGameListener;
import net.danielrickman.api.listener.generic.GenericPreGameListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.TimedState;
import net.danielrickman.buried.Buried;
import net.danielrickman.buried.listener.BuriedPostGameListener;
import net.danielrickman.buried.listener.BuriedPreGameListener;
import net.danielrickman.buried.repository.BuriedRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.danielrickman.buried.Buried.POST_GAME_STATE_DURATION;

public class BuriedPostGameState extends TimedState {

    private final Buried game;

    public BuriedPostGameState(CircuitPlugin plugin, Buried game) {
        super(plugin, POST_GAME_STATE_DURATION);
        this.game = game;
    }

    @Override
    public void onTimerTick() {

    }

    @Override
    public List<CircuitListener> getListeners() {
        return Arrays.asList(
                new GenericPostGameListener(getPlugin(), game, game.getStats()),
                new GenericDisallowJoinListener(getPlugin()),
                new BuriedPostGameListener(getPlugin(), game.getStats())
        );
    }
}
