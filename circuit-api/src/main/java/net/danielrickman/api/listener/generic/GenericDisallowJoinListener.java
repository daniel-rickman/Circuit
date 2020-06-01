package net.danielrickman.api.listener.generic;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;

public class GenericDisallowJoinListener extends CircuitListener {

    public GenericDisallowJoinListener(CircuitPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void on(PlayerLoginEvent e) {
        e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server is not joinable at this time");
    }
}
