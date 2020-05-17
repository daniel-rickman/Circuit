package net.danielrickman.api.state;

import lombok.Getter;
import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.state.event.StateEndEvent;
import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.List;

public abstract class State {

    private final List<CircuitListener> listeners = new ArrayList<>();

    public void start() {
        Logger.info("Started state: %s", getClass().getSimpleName());
        getListeners().forEach(this::enableListener);
        Bukkit.getPluginManager().callEvent(new StateStartEvent());
    }

    public void stop() {
        Logger.info("Stopped state: %s", getClass().getSimpleName());
        Bukkit.getPluginManager().callEvent(new StateEndEvent());
        disableAll();
    }

    public abstract List<CircuitListener> getListeners();

    private void enableListener(CircuitListener circuitListener) {
        CircuitListener.enable(circuitListener);
        listeners.add(circuitListener);
        Logger.listenerInfo("Enabled %s", circuitListener.getClass().getSimpleName());
    }

    private void disableAll() {
        listeners.forEach(listener -> {
            HandlerList.unregisterAll(listener);
            Logger.listenerInfo("Disabled %s", listener.getClass().getSimpleName());
        });
    }
}