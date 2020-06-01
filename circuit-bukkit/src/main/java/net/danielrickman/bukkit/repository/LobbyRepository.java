package net.danielrickman.bukkit.repository;

import net.citizensnpcs.api.npc.NPC;
import net.danielrickman.api.hologram.Hologram;
import net.danielrickman.api.particle.HelixEffectTask;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.repository.Repository;
import net.danielrickman.bukkit.sidebar.LobbySidebar;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitTask;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class LobbyRepository extends Repository<LobbyProfile> {

    private final CircuitPlugin plugin;

    @Inject
    public LobbyRepository(CircuitPlugin plugin) {
        this.plugin = plugin;
    }

    public void setSidebar(UUID uuid, LobbySidebar sidebar) {
        Objects
                .requireNonNull(getPlayerMap().get(uuid), "Error setting sidebar for " + uuid)
                .setLobbySidebar(sidebar);
    }

    public LobbySidebar getSidebar(UUID uuid) {
        return getPlayerMap().get(uuid).getLobbySidebar();
    }

    public void spawnVoteHologram(UUID uuid, NPC gameNPC, String hologramText) {
        final var hologram = new Hologram(hologramText, gameNPC.getStoredLocation().clone().add(0, 0.6, 0));
        final var player = Bukkit.getPlayer(uuid);
        getPlayerMap().get(uuid).setVoteHologram(hologram);
        hologram.spawnEntity(player);
    }

    public void spawnVoteParticleEffect(UUID uuid, Location location, Color color) {
        getPlayerMap().get(uuid).setVoteEffectTask(
                Bukkit.getScheduler().runTaskTimer(plugin, new HelixEffectTask(Bukkit.getPlayer(uuid), location, 1, Particle.REDSTONE, color), 0, 2)
        );
    }

    public void onVoteEnd(UUID uuid) {
        var profile = getPlayerMap().get(uuid);
        var player = Bukkit.getPlayer(uuid);
        Optional.ofNullable(profile.getVoteHologram()).ifPresent(hologram -> hologram.destroyEntity(player));
        Optional.ofNullable(profile.getVoteEffectTask()).ifPresent(BukkitTask::cancel);
    }
}