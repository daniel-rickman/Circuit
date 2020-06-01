package net.danielrickman.rainbowrun.repository;

import lombok.RequiredArgsConstructor;
import net.danielrickman.api.repository.Repository;
import net.danielrickman.rainbowrun.sidebar.RainbowSidebar;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class RainbowRepository extends Repository<RainbowProfile> {

    public void setSidebar(UUID uuid, RainbowSidebar sidebar) {
        Objects
                .requireNonNull(getPlayerMap().get(uuid), "Error setting sidebar for " + uuid)
                .setSidebar(sidebar);
    }

    public RainbowSidebar getSidebar(UUID uuid) {
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
