package net.danielrickman.bukkit.listener;

import net.citizensnpcs.api.CitizensAPI;
import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.role.PlayerRole;
import net.danielrickman.api.state.event.StateEndEvent;
import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.bukkit.Circuit;
import net.danielrickman.bukkit.repository.LobbyProfile;
import net.danielrickman.bukkit.repository.LobbyRepository;
import net.danielrickman.bukkit.task.NPCCreationTask;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TransitionListener extends CircuitListener {

    private final Circuit circuit;
    private final GlobalRepository global;
    private final LobbyRepository lobby;

    public TransitionListener(Circuit circuit, GlobalRepository global, LobbyRepository lobby) {
        super(circuit);
        this.circuit = circuit;
        this.global = global;
        this.lobby = lobby;
    }

    @EventHandler
    private void on(StateStartEvent e) {
        new NPCCreationTask(circuit, global).run();
        PlayerUtil.forEach(player -> {
            global.setRole(player.getUniqueId(), PlayerRole.PLAYER);
            player.teleport(circuit.getLobbyMap().getWorld().getSpawnLocation());
        });
    }

    @EventHandler
    private void on(StateEndEvent e) {
        CitizensAPI.getNPCRegistry().deregisterAll();
        PlayerUtil.forEach(player -> lobby.getSidebar(player.getUniqueId()).destroy());
    }

    @EventHandler
    private void on(PlayerLoginEvent e) {
        lobby.add(e.getPlayer(), new LobbyProfile());
    }

    @EventHandler
    private void on(PlayerQuitEvent e) {
        lobby.remove(e.getPlayer());
    }
}