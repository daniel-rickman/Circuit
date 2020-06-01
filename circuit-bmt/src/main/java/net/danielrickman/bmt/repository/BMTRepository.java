package net.danielrickman.bmt.repository;

import net.danielrickman.api.repository.Repository;
import net.danielrickman.api.util.CollectionUtil;
import net.danielrickman.bmt.sidebar.BMTSidebar;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class BMTRepository extends Repository<BMTProfile> {

    public void setSidebar(UUID uuid, BMTSidebar sidebar) {
        Objects
                .requireNonNull(getPlayerMap().get(uuid), "Error setting sidebar for " + uuid)
                .setSidebar(sidebar);
    }

    public BMTSidebar getSidebar(UUID uuid) {
        return getPlayerMap().get(uuid).getSidebar();
    }

    public void setGuessedCorrectly(UUID uuid, boolean hasGuessedCorrectly) {
        getPlayerMap().get(uuid).setHasGuessed(hasGuessedCorrectly);
    }

    public boolean hasGuessedCorrectly(UUID uuid) {
        return getPlayerMap().get(uuid).isHasGuessed();
    }

    public void addPoints(UUID uuid, int amount) {
        var profile = getPlayerMap().get(uuid);
        profile.setPoints(profile.getPoints() + amount);
        profile.getSidebar().updatePoints(profile.getPoints());
    }

    public void updateRankings(UUID uuid) {
        HashMap<UUID, Integer> pointsMap = new HashMap<>();
        getPlayerMap().forEach((k, v) -> pointsMap.put(k, v.getPoints()));
        getSidebar(uuid).updateRankings(CollectionUtil.orderByValue(pointsMap, false));
    }
}