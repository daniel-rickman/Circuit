package net.danielrickman.bukkit.listener;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitGame;
import net.danielrickman.api.util.CircuitPrefix;
import net.danielrickman.api.util.CollectionUtil;
import net.danielrickman.api.util.Logger;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.bukkit.Circuit;
import net.danielrickman.bukkit.repository.LobbyRepository;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;

import java.util.*;

public class VoteListener extends CircuitListener {

    private final HashMap<CircuitGame, List<UUID>> voteMap = new HashMap<>();
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
    private void onMapVote(NPCRightClickEvent e) {
        final var HOLOGRAM_TEXT = ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "YOUR VOTE!";
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
            final var configNPC = circuit.getLobbyMap().getConfiguration().getNPCByGameID(game.getIdentifier());
            voteMap.get(game).add(clicker.getUniqueId());
            clicker.playSound(clicker.getEyeLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1.0f);
            lobby.spawnVoteHologram(clicker.getUniqueId(), clicked, HOLOGRAM_TEXT);
            lobby.spawnVoteParticleEffect(clicker.getUniqueId(), clicked.getEntity().getLocation(), (configNPC.isPresent()) ? configNPC.get().getColor() : Color.WHITE);
            PlayerUtil.sendMessage(
                    clicker,
                    CircuitPrefix.VOTE.getPrefix() + "Vote received. You've voted for " + ChatColor.YELLOW + "%s" + ChatColor.WHITE + "." + ChatColor.GRAY + " (%d votes)",
                    game.getDisplayName(),
                    voteMap.get(game).size()
            );
        });
    }

    @EventHandler
    private void onGameStart(NPCRightClickEvent e) {
        var clicker = e.getClicker();
        if (e.getNPC().getEntity().getType() != EntityType.SHEEP) {
            return;
        }
        if (!e.getClicker().isOp()) {
            PlayerUtil.sendMessage(clicker, CircuitPrefix.ADMIN.getPrefix() + "Only Admins can start games!");
            return;
        }
        if (!isActive) {
            return;
        }
        if (Bukkit.getOnlinePlayers().size() < 2) {
            PlayerUtil.sendMessage(clicker, CircuitPrefix.ADMIN.getPrefix() + ChatColor.RED.toString() + "Games require at least 2 players to start.");
            return;
        }
        CircuitGame topVoted = CollectionUtil.getTopEntriesByElements(voteMap).get(0).getKey();
        Sheep sheepNPC = (Sheep) e.getNPC().getEntity();

        isActive = false;
        sheepNPC.setColor(DyeColor.GREEN);

        PlayerUtil.sendMessage(clicker, CircuitPrefix.ADMIN.getPrefix() + "Attempting to start the game.");
        PlayerUtil.sendToAll(CircuitPrefix.VOTE.getPrefix() + "Voting has ended!");
        PlayerUtil.sendToAll(CircuitPrefix.CIRCUIT.getPrefix() + "The next game will be " + ChatColor.YELLOW + "%s " + ChatColor.WHITE + ".", topVoted.getStrippedName());
        PlayerUtil.sendToAll(CircuitPrefix.CIRCUIT.getPrefix() + ChatColor.ITALIC + "You will be teleported to the game map in a few moments...");

        PlayerUtil.forEach(player -> {
            lobby.onVoteEnd(player.getUniqueId());
            player.playSound(player.getEyeLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 0.5f, 0.5f);
        });

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
}