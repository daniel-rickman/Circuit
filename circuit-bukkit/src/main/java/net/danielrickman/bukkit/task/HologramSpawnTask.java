package net.danielrickman.bukkit.task;

import lombok.RequiredArgsConstructor;
import net.citizensnpcs.api.CitizensAPI;
import net.danielrickman.api.hologram.Hologram;
import net.danielrickman.api.map.pregame.LobbyConfiguration;
import net.danielrickman.api.rank.Rank;
import net.danielrickman.bukkit.Circuit;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Optional;

@RequiredArgsConstructor
public class HologramSpawnTask implements Runnable {

    private final Circuit circuit;
    private final Player player;
    private final LobbyConfiguration lobby;

    @Override
    public void run() {
        //Spawn holograms
        lobby.getHologramTemplates().forEach(template -> {
            var hologram = new Hologram(template.getText(), template.getMapLocation());
            if (hologram.getText().contains("{0}")) {
                hologram.setText(MessageFormat.format(hologram.getText(), Rank.getFormattedName(player)));
            }
            hologram.spawnEntity(player);
        });
        //Spawn NPC names
        circuit.getLoadedGames().forEach(game -> {
            CitizensAPI.getNPCRegistry().forEach(npc -> {
                if (npc.getFullName().equals(game.getIdentifier())) {
                    var hologram = new Hologram(game.getBoldDisplayName(), npc.getStoredLocation().add(0, 0.2, 0));
                    hologram.spawnEntity(player);
                }
            });
        });
    }
}
