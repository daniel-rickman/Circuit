package net.danielrickman.bukkit.listener;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.danielrickman.api.hologram.Hologram;
import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.particle.HelixEffectTask;
import net.danielrickman.api.plugin.CircuitGame;
import net.danielrickman.bukkit.Circuit;
import net.danielrickman.bukkit.event.OperatorAttemptStartEvent;
import net.danielrickman.bukkit.task.GameLoadTask;
import net.danielrickman.bukkit.util.LobbySetting;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoteListener extends CircuitListener {

    //OnGameStart - End Voting, Announce Winner / Choose Game if Tie
    //NPCClickEvent - Display effect, add vote to game, show Hologram

    private final static String PREFIX = ChatColor.LIGHT_PURPLE + "Voting " + ChatColor.DARK_GRAY + "\u00BB ";
    private final static String HOLOGRAM_TEXT = ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "YOUR VOTE!";

    private final Circuit circuit;
    private final HashMap<Player, Hologram> playerHolograms = new HashMap<>();
    private final HashMap<Player, BukkitTask> playerParticleEffects = new HashMap<>();

    public HashMap<CircuitGame, List<Player>> voteMap = new HashMap<>();

    public VoteListener(Circuit circuit) {
        super(circuit);
        this.circuit = circuit;
        circuit.getLoadedGames().forEach(game -> voteMap.put(game, new ArrayList<>()));
        LobbySetting.VOTE.set(true);
    }

    @EventHandler
    public void on(NPCRightClickEvent e) {
        var clicker = e.getClicker();
        var optionalGame = circuit.getGameByID(ChatColor.stripColor(e.getNPC().getFullName()));
        optionalGame.ifPresent(circuitGame -> {
            //If voting is disabled
            if (!LobbySetting.VOTE.isAllowed()) {
                clicker.sendMessage(PREFIX + ChatColor.RED + "Voting is currently disabled");
                return;
            }

            //If player has already voted
            if (hasPlayerVoted(clicker)) {
                clicker.sendMessage(PREFIX + ChatColor.RED + "You've already voted for a game");
                clicker.playSound(clicker.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1.0f, 0.1f);
                return;
            }

            var npcLocation = e.getNPC().getStoredLocation().toCenterLocation();
            //Add vote
            voteMap.get(circuitGame).add(clicker);

            //Sound + Message
            clicker.playSound(clicker.getEyeLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            clicker.sendMessage(PREFIX + ChatColor.WHITE + "Vote received. You've voted for " + ChatColor.YELLOW + circuitGame.getStrippedName() + ChatColor.WHITE + ".");

            //Show Hologram
            playerHolograms.put(clicker, new Hologram(HOLOGRAM_TEXT, npcLocation.add(0, 0.75, 0))); //Add hologram above NPC's head
            playerHolograms.get(clicker).spawnEntity(clicker);

            //Show Particle Effect
            playerParticleEffects.put(clicker,
                    new HelixEffectTask(clicker, npcLocation.subtract(0, 2, 0), 1, Particle.REDSTONE, Color.YELLOW)
                            .runTaskTimer(circuit, 0, 2)
            );
        });
    }

    @EventHandler(priority = EventPriority.LOW)
    public void on(OperatorAttemptStartEvent e) {
        LobbySetting.VOTE.set(false);
        circuit.getPlayerRepository().forEach(cp -> {
            //todo add #sendMessage() to CorePlayer
            cp.getPlayer().sendMessage(PREFIX + ChatColor.WHITE + "Voting has ended!");
            cp.getPlayer().sendMessage(PREFIX + ChatColor.WHITE + "The next game will be " + ChatColor.YELLOW + getWinningGame().getStrippedName() + ChatColor.WHITE + ".");
        });

        playerHolograms.forEach((p, hologram) -> hologram.destroyEntity(p));
        playerParticleEffects.forEach((p, bukkitTask) -> bukkitTask.cancel());

        new GameLoadTask(circuit, getWinningGame()).run();
    }

    private boolean hasPlayerVoted(Player player) {
        for (CircuitGame game : voteMap.keySet()) {
            if (voteMap.get(game).contains(player)) {
                return true;
            }
        }
        return false;
    }

    private CircuitGame getWinningGame() {
        CircuitGame topVoted = null;
        for (Map.Entry<CircuitGame, List<Player>> entry : voteMap.entrySet()) {
            if (topVoted == null || entry.getValue().size() > voteMap.get(topVoted).size()) {
                topVoted = entry.getKey();
            }
        }
        return topVoted;
    }
}