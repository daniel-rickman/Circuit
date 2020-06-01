package net.danielrickman.rainbowrun.state;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.listener.generic.GenericDisallowJoinListener;
import net.danielrickman.api.listener.generic.GenericPostGameListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.state.TimedState;
import net.danielrickman.rainbowrun.listener.RainbowPostGameListener;
import net.danielrickman.rainbowrun.RainbowRun;
import net.danielrickman.rainbowrun.repository.RainbowRepository;

import java.util.Arrays;
import java.util.List;

import static net.danielrickman.rainbowrun.RainbowRun.POST_GAME_STATE_DURATION;

public class RainbowPostGameState extends TimedState {

    private final RainbowRun game;

    public RainbowPostGameState(CircuitPlugin plugin, RainbowRun game) {
        super(plugin, POST_GAME_STATE_DURATION);
        this.game = game;
    }

    @Override
    public void onTimerTick() {

    }

    @Override
    public List<CircuitListener> getListeners() {
        return Arrays.asList(
                new GenericDisallowJoinListener(getPlugin()),
                new GenericPostGameListener(getPlugin(), game, game.getStats()),
                new RainbowPostGameListener(getPlugin(), game)
        );
    }
}
