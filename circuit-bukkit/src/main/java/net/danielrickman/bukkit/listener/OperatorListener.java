package net.danielrickman.bukkit.listener;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.bukkit.event.OperatorAttemptStartEvent;
import net.danielrickman.bukkit.item.LobbyItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class OperatorListener extends CircuitListener {

    private final static String PREFIX = ChatColor.RED + "OP " + ChatColor.DARK_GRAY + "\u00BB " + ChatColor.WHITE;
    private boolean hasStarted;

    public OperatorListener(CircuitPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void on(PlayerJoinEvent e) {
        if (e.getPlayer().isOp()) {
            LobbyItem.START_ITEM.give(e.getPlayer());
        }
    }

    @EventHandler
    public void on(PlayerChangedWorldEvent e) { //Called when player returns to Lobby from Game
        if (e.getPlayer().isOp()) {
            LobbyItem.START_ITEM.give(e.getPlayer());
        }
    }

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (e.getPlayer().isOp()) {
            if (e.getItem() != null && e.getItem().getType() == Material.REDSTONE) {
                if (!hasStarted) {
                    e.getPlayer().sendMessage(PREFIX + "Attempting to start the game");
                    Bukkit.getPluginManager().callEvent(new OperatorAttemptStartEvent());
                    hasStarted = true;
                }
            }
        }
    }
}
