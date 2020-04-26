package net.danielrickman.api.state;

import net.danielrickman.api.plugin.CircuitPlugin;

public abstract class SimpleState extends State {

    private CircuitPlugin plugin;

    public SimpleState(CircuitPlugin plugin) {
        this.plugin = plugin;
    }
}