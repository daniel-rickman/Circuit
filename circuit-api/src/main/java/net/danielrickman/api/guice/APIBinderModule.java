package net.danielrickman.api.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.danielrickman.api.plugin.CircuitPlugin;

public class APIBinderModule extends AbstractModule {

    private final CircuitPlugin plugin;

    public APIBinderModule(CircuitPlugin plugin) {
        this.plugin = plugin;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {
        this.bind(CircuitPlugin.class).toInstance(this.plugin);
    }
}