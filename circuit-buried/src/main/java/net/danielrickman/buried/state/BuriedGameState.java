package net.danielrickman.buried.state;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.listener.generic.GenericDisallowJoinListener;
import net.danielrickman.api.listener.generic.GenericEliminationListener;
import net.danielrickman.api.map.lobby.LobbyConfiguration;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.TimedState;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.buried.Buried;
import net.danielrickman.buried.BuriedMapConfiguration;
import net.danielrickman.buried.listener.BuriedGameListener;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.danielrickman.buried.Buried.GAME_STATE_DURATION;

public class BuriedGameState extends TimedState {

    private final Buried game;

    public BuriedGameState(CircuitPlugin plugin, Buried game) {
        super(plugin, GAME_STATE_DURATION);
        this.game = game;
    }

    @Override
    public void onTimerTick() {
        var timer = getTimer();
        var alivePlayers = PlayerUtil.getAlivePlayers(getPlugin().getGlobalRepository());
        if (alivePlayers.size() < 2) {
            getPlugin().nextState();
        } else {
            PlayerUtil.forEach(player -> {
                game.getStats().getSidebar(player.getUniqueId()).updateTimer(timer);
            });
        }
        ((BuriedMapConfiguration) game.getMapConfiguration())
                .getMapRegion()
                .getBlocksOfType(game.getWorld(), Collections.singletonList(Material.MAGMA_BLOCK))
                .forEach(location -> location.getBlock().setType(Material.LAVA));
    }

    @Override
    public List<CircuitListener> getListeners() {
        return Arrays.asList(
                new BuriedGameListener(getPlugin(), game, getTimer()),
                new GenericDisallowJoinListener(getPlugin()),
                new GenericEliminationListener(getPlugin(), game)
        );
    }
}