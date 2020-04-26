package net.danielrickman.bukkit;

import com.google.inject.Injector;
import lombok.Getter;
import lombok.Setter;
import net.danielrickman.api.annotation.Game;
import net.danielrickman.api.inject.APIBinderModule;
import net.danielrickman.api.listener.CoreListener;
import net.danielrickman.api.map.Map;
import net.danielrickman.api.map.MapConfiguration;
import net.danielrickman.api.map.MapLoader;
import net.danielrickman.api.plugin.CircuitGame;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.util.ClassUtil;
import net.danielrickman.bukkit.map.LobbyConfiguration;
import net.danielrickman.bukkit.state.PreGameState;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Circuit extends CircuitPlugin {

    @Inject
    @Getter
    private MapLoader mapLoader;
    @Getter
    private Map<LobbyConfiguration> lobby;
    @Getter
    @Setter
    private Map<? extends MapConfiguration> gameMap;
    @Getter
    private List<CircuitGame> loadedGames = new ArrayList<>();

    @Override
    public void onEnable() {
        loadInjectors();
        loadGames();
        Bukkit.getLogger().info("Loaded games: " + Arrays.toString(getLoadedGames().toArray()));
        lobby = mapLoader.loadMap(this, LobbyConfiguration.class);
        if (lobby == null) {
            this.getLogger().severe("Lobby not loaded correctly");
        }
        new CoreListener(this, getPlayerRepository()).enable();
        queuePreGame();
        nextState();
    }

    @Override
    public void onDisable() {
        getCurrentState().stop();
    }

    @Override
    public String getIdentifier() {
        return "core";
    }

    private void loadInjectors() {
        APIBinderModule module = new APIBinderModule(this);
        Injector injector = module.createInjector();
        injector.injectMembers(this);
    }


    public void loadGames() {
        ClassUtil.getClassesWithAnnotation(Game.class, "net.danielrickman").forEach(clazz -> {
            if (CircuitGame.class.isAssignableFrom(clazz)) {
                try {
                    loadedGames.add((CircuitGame) clazz.getDeclaredConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }});
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

    public void queuePreGame() {
        getStates().add(new PreGameState(this));
    }

    public boolean isGameRunning() {
        return !(getCurrentState() instanceof PreGameState);
    }

}