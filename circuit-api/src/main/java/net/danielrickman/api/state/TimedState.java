package net.danielrickman.api.state;

import lombok.Getter;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.util.Timer;

public abstract class TimedState extends State {

    @Getter
    private final CircuitPlugin plugin;
    @Getter
    private final Timer timer;

    public TimedState(CircuitPlugin plugin, int duration) {
        this.plugin = plugin;
        this.timer = new Timer(plugin, duration, this::onTimerTick, plugin::nextState);
    }

    public TimedState(CircuitPlugin plugin, int duration, int delay) {
        super(delay);
        this.plugin = plugin;
        this.timer = new Timer(plugin, duration, this::onTimerTick, plugin::nextState);
    }

    public void start() {
        super.start();
        timer.start();
    }

    public abstract void onTimerTick();

    public void stop() {
        super.stop();
        timer.stop();
    }
}