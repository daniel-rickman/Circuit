package net.danielrickman.bukkit.state;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.state.SimpleState;
import net.danielrickman.bukkit.Circuit;
import net.danielrickman.bukkit.listener.MapListener;
import net.danielrickman.bukkit.listener.PlayerListener;
import net.danielrickman.bukkit.listener.TransitionListener;
import net.danielrickman.bukkit.listener.VoteListener;
import net.danielrickman.bukkit.repository.LobbyRepository;

import java.util.List;

public class PreGameState extends SimpleState {

    private final Circuit circuit;
    private final LobbyRepository lobby;

    public PreGameState(Circuit circuit, LobbyRepository lobby) {
        super(circuit);
        this.circuit = circuit;
        this.lobby = lobby;
    }

    @Override
    public List<CircuitListener> getListeners() {
        return List.of(
                new MapListener(circuit),
                new PlayerListener(circuit, lobby),
                new TransitionListener(circuit, lobby),
                new VoteListener(circuit, lobby)
        );
    }
}