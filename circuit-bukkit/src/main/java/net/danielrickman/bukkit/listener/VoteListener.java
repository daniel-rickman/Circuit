package net.danielrickman.bukkit.listener;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitGame;
import net.danielrickman.api.util.CircuitPrefix;
import net.danielrickman.api.util.Logger;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.bukkit.Circuit;
import net.danielrickman.bukkit.inventory.OperatorInventory;
import net.danielrickman.bukkit.repository.LobbyRepository;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.*;

public class VoteListener extends CircuitListener {

    public final HashMap<CircuitGame, List<UUID>> voteMap = new HashMap<>();
    private final Circuit circuit;
    private final LobbyRepository lobby;
    private boolean isActive = true;

    public VoteListener(Circuit circuit, LobbyRepository lobby) {
        super(circuit);
        this.circuit = circuit;
        this.lobby = lobby;
        circuit.getLoadedGames().forEach(game -> voteMap.put(game, new ArrayList<>()));
    }

    @EventHandler
    private void on(NPCRightClickEvent e) {
        final var HOLOGRAM_TEXT = ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "YOUR VOTE!";
        final var clicker = e.getClicker();
        final var clicked = e.getNPC();

        circuit.getGameByID(ChatColor.stripColor(clicked.getFullName())).ifPresent(game -> {
            if (!isActive) {
                PlayerUtil.sendMessage(clicker, CircuitPrefix.VOTE.getPrefix() + ChatColor.RED.toString() + "Voting is currently disabled.");
                return;
            }
            if (game.getStates().isEmpty()) {
                PlayerUtil.sendMessage(clicker, CircuitPrefix.VOTE.getPrefix() + ChatColor.RED.toString() + "This game is currently unavailable.");
                return;
            }
            if (voteMap.entrySet().stream().anyMatch(entry -> entry.getValue().contains(clicker.getUniqueId()))) {
                PlayerUtil.sendMessage(clicker, CircuitPrefix.VOTE.getPrefix() + ChatColor.RED.toString() + "You've already voted for a game.");
                clicker.playSound(clicker.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1.0f, 0.1f);
                return;
            }
            voteMap.get(game).add(clicker.getUniqueId());
            clicker.playSound(clicker.getEyeLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1.0f);
            lobby.spawnVoteHologram(clicker.getUniqueId(), clicked, HOLOGRAM_TEXT);
            lobby.spawnVoteParticleEffect(clicker.getUniqueId(), clicked.getEntity().getLocation());
            PlayerUtil.sendMessage(
                    clicker,
                    CircuitPrefix.VOTE.getPrefix() + "Vote received. You've voted for " + ChatColor.YELLOW + "%s" + ChatColor.WHITE + "." + ChatColor.GRAY + " (%d votes)",
                    game.getDisplayName(),
                    voteMap.get(game).size()
            );
        });
    }

    @EventHandler
    private void on(PlayerInteractEvent e) {
        if (!OperatorInventory.START_ITEM.matches(e.getItem())) {
            return;
        }
        if (!isActive) { //Player can't start game twice
            return;
        }
        if (Bukkit.getOnlinePlayers().size() < 2) {
            PlayerUtil.sendMessage(e.getPlayer(), CircuitPrefix.ADMIN.getPrefix() + ChatColor.RED.toString() + "Games require at least 2 players to start.");
            return;
        }
        var topVoted = getTopVotedGame();
        isActive = false;
        PlayerUtil.sendMessage(e.getPlayer(), CircuitPrefix.ADMIN.getPrefix() + "Attempting to start the game.");
        PlayerUtil.sendToAll(CircuitPrefix.VOTE.getPrefix() + "Voting has ended!");
        PlayerUtil.sendToAll("The next game will be " + ChatColor.YELLOW + "%s " + ChatColor.WHITE + ".", getTopVotedGame().getStrippedName());
        PlayerUtil.sendToAll(ChatColor.ITALIC + "You will be teleported to the game map in a few moments...");

        PlayerUtil.forEach(player -> lobby.onVoteEnd(player.getUniqueId()));

        Bukkit.getScheduler().runTaskLater(circuit, task -> {
            if (circuit.getMapRepository().getMap(topVoted) == null) {
                Logger.error("No maps found for game: %s", topVoted.getStrippedName());
                PlayerUtil.sendToAll(CircuitPrefix.ADMIN.getPrefix() + "Error starting game, we're staying where we are.");
                circuit.queuePreGame();
            } else {
                circuit.queueGame(topVoted);
            }
            circuit.nextState();
        }, 40);
    }

    private CircuitGame getTopVotedGame() {
        CircuitGame topVoted = null;
        for (Map.Entry<CircuitGame, List<UUID>> entry : voteMap.entrySet()) {
            if (topVoted == null || entry.getValue().size() > voteMap.get(topVoted).size()) {
                topVoted = entry.getKey();
            }
        }
        return topVoted;
    }
}