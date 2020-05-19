package net.danielrickman.bukkit;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.citizensnpcs.api.CitizensAPI;
import net.danielrickman.api.guice.APIBinderModule;
import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.listener.DefaultListener;
import net.danielrickman.api.map.pregame.LobbyConfiguration;
import net.danielrickman.api.plugin.CircuitGame;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.util.Logger;
import net.danielrickman.bukkit.repository.LobbyRepository;
import net.danielrickman.bukkit.state.PreGameState;

public class Circuit extends CircuitPlugin {

    @Inject
    private LobbyRepository lobby;

    @Override
    public void onEnable() {
        loadInjectors();
        loadGames();
        setLobbyMap(getMapLoader().loadMap(this, LobbyConfiguration.class));
        if (getLobbyMap() == null) {
            Logger.error("Lobby map is null");
        }
        CircuitListener.enable(new DefaultListener(this, getGlobalRepository()));
        queuePreGame();
        nextState();
        getMapLoader().loadGameMaps();
    }

    @Override
    public void onDisable() {
        CitizensAPI.getNPCRegistry().deregisterAll();
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

    public void queuePreGame() {
        getStates().add(new PreGameState(this, getGlobalRepository(), lobby));
    }

    public void queueGame(CircuitGame game) {
        if (game.getStates().size() == 0) {
            Logger.error("No game states found for %s", game.getStrippedName());
        } else {
            getStates().addAll(game.getStates());
            queuePreGame();
        }
    }
}