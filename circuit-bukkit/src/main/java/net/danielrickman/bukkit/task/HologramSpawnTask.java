package net.danielrickman.bukkit.task;

import lombok.RequiredArgsConstructor;
import net.danielrickman.api.hologram.Hologram;
import net.danielrickman.api.map.pregame.LobbyConfiguration;
import net.danielrickman.api.rank.Rank;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

@RequiredArgsConstructor
public class HologramSpawnTask implements Runnable {

    private final Player player;
    private final LobbyConfiguration lobby;

    @Override
    public void run() {
        lobby.getHologramTemplates().forEach(template -> {
            var hologram = new Hologram(template.getText(), template.getMapLocation());
            if (hologram.getText().contains("{0}")) {
                hologram.setText(MessageFormat.format(hologram.getText(), Rank.getFormattedName(player)));
            }
            hologram.spawnEntity(player);
        });
    }
}
