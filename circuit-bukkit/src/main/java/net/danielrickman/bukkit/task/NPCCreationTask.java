package net.danielrickman.bukkit.task;

import net.danielrickman.api.npc.NPCBuilder;
import net.danielrickman.api.rank.Rank;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.bukkit.Circuit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;

public class NPCCreationTask implements Runnable {

    private final Circuit circuit;
    private final GlobalRepository global;

    public NPCCreationTask(Circuit circuit, GlobalRepository global) {
        this.circuit = circuit;
        this.global = global;
    }

    @Override
    public void run() {
        var configuration = circuit.getLobbyMap().getConfiguration();

        configuration.getNpcs().forEach(npc -> circuit.getGameByID(npc.getId()).ifPresent(game -> {
            NPCBuilder
                    .of(EntityType.PLAYER)
                    .withName(game.getBoldDisplayName())
                    .withSkin(npc.getSkinUuid(), npc.getSkinData(), npc.getSkinSignature())
                    .spawnEntity(npc.getMapLocation().toWorldLocation(circuit.getLobbyMap().getWorld()));
        }));

        var topPlayer = Bukkit.getPlayer(global.getTopCoinsPlayer());
        NPCBuilder builder;
        if (topPlayer == null) {
            builder = NPCBuilder.of(EntityType.PLAYER).withName(ChatColor.RED.toString() + ChatColor.BOLD + "Nobody yet!");
        } else {
            builder = NPCBuilder.ofPlayer(topPlayer, Rank.getFormattedName(topPlayer));
        }
        builder.spawnEntity(configuration.getTopPlayerNpc().getMapLocation().toWorldLocation(circuit.getLobbyMap().getWorld()));
    }
}
