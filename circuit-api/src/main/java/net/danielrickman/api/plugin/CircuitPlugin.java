package net.danielrickman.api.plugin;

import lombok.Getter;
import lombok.Setter;
import net.danielrickman.api.annotation.Game;
import net.danielrickman.api.map.Map;
import net.danielrickman.api.map.MapConfiguration;
import net.danielrickman.api.map.MapLoader;
import net.danielrickman.api.map.MapRepository;
import net.danielrickman.api.map.pregame.LobbyConfiguration;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.state.State;
import net.danielrickman.api.util.ClassUtil;
import net.danielrickman.api.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class CircuitPlugin extends JavaPlugin implements ICircuitModule {

    private final static String PACKAGE_NAME = "net.danielrickman";

    @Getter
    private final List<CircuitGame> loadedGames = new ArrayList<>();
    @Getter
    private final HashMap<CircuitGame, Map<? extends MapConfiguration>> gameMaps = new HashMap<>();

    @Getter
    private final LinkedList<State> states = new LinkedList<>();
    @Getter
    private State currentState;

    @Inject
    @Getter
    private MapRepository mapRepository;
    @Inject
    @Getter
    private MapLoader mapLoader;
    @Getter
    @Setter
    private Map<LobbyConfiguration> lobbyMap;

    @Inject
    @Getter
    private GlobalRepository globalRepository;


    @Override
    public String getIdentifier() {
        return "plugin";
    }

    protected void loadGames() {
        ClassUtil.getClassesWithAnnotation(Game.class, PACKAGE_NAME).forEach(clazz -> {
            if (CircuitGame.class.isAssignableFrom(clazz)) {
                try {
                    loadedGames.add((CircuitGame) clazz.getDeclaredConstructor(CircuitPlugin.class, MapRepository.class).newInstance(this, getMapRepository()));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        });
        Logger.info("Loaded games: %s", Arrays.toString(loadedGames.toArray()));
    }

    public Optional<CircuitGame> getGameByID(String identifier) {
        if (!loadedGames.isEmpty()) {
            return loadedGames
                    .stream()
                    .filter(game -> game.getIdentifier().equalsIgnoreCase(identifier))
                    .findFirst();
        }
        return Optional.empty();
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
}