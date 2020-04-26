package net.danielrickman.bukkit.task;

import lombok.RequiredArgsConstructor;
import net.danielrickman.api.plugin.CircuitGame;
import net.danielrickman.bukkit.Circuit;
import org.bukkit.ChatColor;

@RequiredArgsConstructor
public class GameLoadTask implements Runnable {

    public final Circuit circuit;
    public final CircuitGame game;

    @Override
    public void run() {
        circuit.setGameMap(circuit.getMapLoader().loadMap(game, game.getMapConfigurationClass()));
        if (circuit.getGameMap() == null) {
            circuit.getPlayerRepository().forEach(cp -> cp.getPlayer().sendMessage(ChatColor.RED + "No maps found for game: " + game.getStrippedName()));
            circuit.queuePreGame();
        } else {
            circuit.queueGame(game);
        }
        circuit.nextState();
    }
}