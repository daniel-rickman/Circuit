package net.danielrickman.oitq.state;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.listener.generic.GenericDisallowJoinListener;
import net.danielrickman.api.listener.generic.GenericPostGameListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.TimedState;
import net.danielrickman.oitq.OneInTheQuiver;
import net.danielrickman.oitq.listener.OITQPostGameListener;
import net.danielrickman.oitq.repository.OITQRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.danielrickman.oitq.OneInTheQuiver.POST_GAME_STATE_DURATION;

public class OITQPostGameState extends TimedState {

    private final OneInTheQuiver game;
    private final OITQRepository stats;

    public OITQPostGameState(CircuitPlugin plugin, OneInTheQuiver game) {
        super(plugin, POST_GAME_STATE_DURATION);
        this.game = game;
        this.stats = game.getStats();
    }

    @Override
    public List<CircuitListener> getListeners() {
        return Arrays.asList(
                new GenericDisallowJoinListener(getPlugin()),
                new GenericPostGameListener(getPlugin(), game, stats),
                new OITQPostGameListener(getPlugin(), game)
        );
    }

    @Override
    public void onTimerTick() {
    }
}
