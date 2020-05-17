package net.danielrickman.api.state;

import lombok.Getter;
import net.danielrickman.api.plugin.CircuitPlugin;

public abstract class SimpleState extends State {

    @Getter
    private final CircuitPlugin plugin;

    public SimpleState(CircuitPlugin plugin) {
        this.plugin = plugin;
    }
}