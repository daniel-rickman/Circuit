package net.danielrickman.buried.repository;

import net.danielrickman.api.repository.Repository;
import net.danielrickman.api.util.CollectionUtil;
import net.danielrickman.buried.sidebar.BuriedSidebar;

import java.util.*;

public class BuriedRepository extends Repository<BuriedProfile> {

    public void setSidebar(UUID uuid, BuriedSidebar sidebar) {
        Objects
                .requireNonNull(getPlayerMap().get(uuid), "Error setting sidebar for " + uuid)
                .setSidebar(sidebar);
    }

    public BuriedSidebar getSidebar(UUID uuid) {
        return getPlayerMap().get(uuid).getSidebar();
    }

    public void addPoints(UUID uuid, int amount) {
        var profile = getPlayerMap().get(uuid);
        profile.setPoints(profile.getPoints() + amount);
    }

    public void updateRankings(UUID uuid) {
        HashMap<UUID, Integer> pointsMap = new HashMap<>();
        getPlayerMap().forEach((k, v) -> pointsMap.put(k, v.getPoints()));
        getSidebar(uuid).updateRankings(CollectionUtil.orderByValue(pointsMap, false));
    }
}