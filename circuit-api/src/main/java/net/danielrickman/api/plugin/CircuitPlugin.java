package net.danielrickman.api.plugin;

import lombok.Getter;
import net.danielrickman.api.player.PlayerRepository;
import net.danielrickman.api.state.State;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.LinkedList;

public abstract class CircuitPlugin extends JavaPlugin implements ICircuitModule {

    @Getter
    private LinkedList<State> states = new LinkedList<>();
    @Getter
    private State currentState;

    @Getter
    @Inject
    private PlayerRepository playerRepository;

    @Override
    public String getIdentifier() {
        return "plugin";
    }

    public void nextState() {
        if (states.isEmpty()) {
            Bukkit.broadcastMessage(ChatColor.RED + "Something went wrong: No new states queued");
            getLogger().warning("No new state queued. Staying in current state.");
            return;
        }
        if (currentState != null) {
            currentState.stop();
        }
        currentState = states.removeFirst();
        currentState.start();
    }

    public void queueGame(CircuitGame game) {
        states.addAll(game.getStates());
    }
}