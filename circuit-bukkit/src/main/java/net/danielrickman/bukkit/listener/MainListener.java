package net.danielrickman.bukkit.listener;

import com.destroystokyo.paper.Title;
import net.danielrickman.api.hologram.Hologram;
import net.danielrickman.api.player.CorePlayer;
import net.danielrickman.api.util.WorldSetting;
import net.danielrickman.bukkit.Circuit;
import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.bukkit.event.OperatorAttemptStartEvent;
import net.danielrickman.bukkit.map.LobbyConfiguration;
import net.danielrickman.api.map.Map;
import net.danielrickman.bukkit.sidebar.LobbySidebar;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.MessageFormat;
import java.util.Arrays;

public class MainListener extends CircuitListener {

    private final static String PREFIX = ChatColor.GOLD + "Circuit " + ChatColor.DARK_GRAY + "\u00BB " + ChatColor.WHITE;

    private final Circuit circuit;
    private final Map<LobbyConfiguration> lobby;

    public MainListener(Circuit circuit) {
        super(circuit);
        this.circuit = circuit;
        this.lobby = circuit.getLobby();
    }

    @EventHandler
    public void on(PlayerJoinEvent e) { //Called when player joins server
        if (circuit.getPlayerRepository().getPlayer(e.getPlayer()).isPresent()) {
            var cp = circuit.getPlayerRepository().getPlayer(e.getPlayer()).get();
            spawnPlayer(cp.getPlayer());

            Arrays.asList("Welcome, " + cp.getFormattedName() + ChatColor.WHITE + ".",
                    "This server uses an arcade system to offer many different minigames.",
                    ChatColor.YELLOW + "Right click " + ChatColor.WHITE + "one of the NPCs to vote for a game.",
                    "An " + ChatColor.RED + "admin" + ChatColor.WHITE + " is required to start the games.",
                    "Have fun!"
            ).forEach(msg -> cp.getPlayer().sendMessage(PREFIX + msg));
        }
    }

    @EventHandler
    public void on(PlayerChangedWorldEvent e) { //Called when player returns to Lobby from Game
        spawnPlayer(e.getPlayer());
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        circuit.getPlayerRepository().forEach(cp -> cp.getSidebar().update());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void on(OperatorAttemptStartEvent e) {
        circuit.getPlayerRepository().forEach(cp -> {
            var p = cp.getPlayer();
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.3f, 1.0f);
            p.sendTitle(new Title("", ChatColor.GRAY + "Loading Game, one moment...", 5, 10,5));
        });
    }

    public void spawnPlayer(Player p) {
        var playerRepository = circuit.getPlayerRepository();
        playerRepository.getPlayer(p).ifPresentOrElse(
                (cp) -> {
                    cp.getPlayer().teleport(lobby.getConfiguration().getSpawnLocation().toWorldLocation(lobby.getWorld()).toCenterLocation());
                    cp.getPlayer().setPlayerListName(cp.getFormattedName() + "   " + ChatColor.WHITE + cp.getCoins() + ChatColor.GOLD + "\u26C3");
                    cp.setSidebar(new LobbySidebar(circuit, cp).initialise());

                    spawnHolograms(cp);
                },
                () -> p.kickPlayer("Error when loading player into world"));
        playerRepository.forEach(cp -> cp.getSidebar().update());
    }

    private void spawnHolograms(CorePlayer cp) {
        lobby.getConfiguration().getHologramTemplates().forEach(hologramTemplate -> {
            Hologram hologram = new Hologram(hologramTemplate.getText(), hologramTemplate.getMapLocation());
            if (hologram.getText().contains("{0}")) {
                hologram.setText(MessageFormat.format(hologram.getText(), cp.getFormattedName()));
            }
            hologram.spawnEntity(cp);
        });
    }
}