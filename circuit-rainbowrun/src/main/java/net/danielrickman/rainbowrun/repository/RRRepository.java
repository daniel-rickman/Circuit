package net.danielrickman.rainbowrun.repository;

import lombok.RequiredArgsConstructor;
import net.danielrickman.api.repository.Repository;
import net.danielrickman.rainbowrun.sidebar.RRPlayerSidebar;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class RRRepository extends Repository<RRProfile> {

    public void setSidebar(UUID uuid, RRPlayerSidebar sidebar) {
        Objects
                .requireNonNull(getPlayerMap().get(uuid), "Error setting sidebar for " + uuid)
                .setSidebar(sidebar);
    }

    public RRPlayerSidebar getSidebar(UUID uuid) {
        return getPlayerMap().get(uuid).getSidebar();
    }

    public int getBlocksDestroyed(UUID uuid) {
        return getPlayerMap().get(uuid).getBlocksDestroyed();
    }

    public void incrementBlocksDestroyed(UUID uuid) {
        var profile = getPlayerMap().get(uuid);
        profile.setBlocksDestroyed(profile.getBlocksDestroyed() + 1);
    }
}
