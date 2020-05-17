package net.danielrickman.api.repository;

import lombok.Getter;
import net.danielrickman.api.repository.profile.IProfile;
import net.danielrickman.api.util.Logger;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public abstract class Repository<T extends IProfile> {

    @Getter
    private final HashMap<UUID, T> playerMap = new HashMap<>();

    public final void add(Player p, T profile) {
        playerMap.put(p.getUniqueId(), profile);
        Logger.data("Added profile of type %s for %s", profile.getClass().getSimpleName(), p.getName());
    }

    public final void remove(Player p) {
        if (playerMap.containsKey(p.getUniqueId())) {
            Logger.data("Removed profile of type %s for %s", playerMap.get(p.getUniqueId()).getClass().getSimpleName(), p.getName());
            playerMap.remove(p.getUniqueId());
        }
    }
}
