package net.danielrickman.api.state;

import net.danielrickman.api.listener.CircuitListener;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public abstract class State {

    private final List<CircuitListener> listeners = new ArrayList<>();

    public void start() {
        Bukkit.getLogger().info("Started state: " + getClass().getSimpleName());
        this.onStateStart();
    }

    public void stop() {
        Bukkit.getLogger().info("Stopped state: " + getClass().getSimpleName());
        this.onStateEnd();
        listeners.forEach(CircuitListener::disable);
    }

    public abstract void onStateStart();

    public abstract void onStateEnd();

    public final void enableListener(CircuitListener listener) {
        listeners.add(listener);
        listener.enable();
    }
}