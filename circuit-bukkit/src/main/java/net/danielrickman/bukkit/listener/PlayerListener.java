package net.danielrickman.bukkit.listener;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.rank.Rank;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.util.CircuitPrefix;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.bukkit.Circuit;
import net.danielrickman.bukkit.inventory.OperatorInventory;
import net.danielrickman.bukkit.repository.LobbyRepository;
import net.danielrickman.bukkit.sidebar.LobbySidebar;
import net.danielrickman.bukkit.task.HologramSpawnTask;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.List;

public class PlayerListener extends CircuitListener {

    private final static List<String> WELCOME_MESSAGES = Arrays.asList(
            "This server uses an arcade system to offer many different minigames.",
            ChatColor.YELLOW + "Right click " + ChatColor.WHITE + "one of the NPCs to vote for a game.",
            "An " + ChatColor.RED + "admin" + ChatColor.WHITE + " is required to start the games.",
            "Have fun!"
    );

    private final Circuit circuit;
    private final GlobalRepository global;
    private final LobbyRepository lobby;

    public PlayerListener(Circuit circuit, GlobalRepository global, LobbyRepository lobby) {
        super(circuit);
        this.circuit = circuit;
        this.global = global;
        this.lobby = lobby;
    }

    @EventHandler
    private void on(PlayerJoinEvent e) {
        var player = e.getPlayer();
        PlayerUtil.sendToAll(ChatColor.GRAY.toString() + ChatColor.ITALIC + "%s has joined the game", player.getName());
        PlayerUtil.sendMessage(player, CircuitPrefix.CIRCUIT.getPrefix() + "Welcome, %s" + ChatColor.WHITE + ".", Rank.getFormattedName(player));
        WELCOME_MESSAGES.forEach(msg -> PlayerUtil.sendMessage(player, CircuitPrefix.CIRCUIT.getPrefix() + msg));

        PlayerUtil.reset(player);
        onLobbyEnter(e.getPlayer());
    }

    @EventHandler
    public void on(PlayerChangedWorldEvent e) { //Called when player returns to Lobby from Game
        onLobbyEnter(e.getPlayer());
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        PlayerUtil.sendToAll(ChatColor.GRAY.toString() + ChatColor.ITALIC + String.format("%s has left the game", e.getPlayer().getName()));
        PlayerUtil.forEach(existingPlayer -> lobby.getSidebar(existingPlayer.getUniqueId()).update());
    }

    private void onLobbyEnter(Player newPlayer) {
        final var lobbyMap = circuit.getLobbyMap();
        PlayerUtil.reset(newPlayer);
        newPlayer.teleport(lobbyMap.getConfiguration().getSpawnLocation().toWorldLocation(lobbyMap.getWorld()).toCenterLocation());
        newPlayer.setPlayerListName(Rank.getFormattedName(newPlayer));
        lobby.setSidebar(newPlayer.getUniqueId(), new LobbySidebar(newPlayer, circuit, global).initialise());
        new HologramSpawnTask(newPlayer, lobbyMap.getConfiguration()).run();

        if (Rank.get(newPlayer) == Rank.OP) {
            OperatorInventory.giveAll(newPlayer);
        }

        PlayerUtil.forEach(existingPlayer -> lobby.getSidebar(existingPlayer.getUniqueId()).update());
    }
}