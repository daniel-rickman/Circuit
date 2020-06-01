package net.danielrickman.oitq.repository;

import lombok.RequiredArgsConstructor;
import net.danielrickman.api.repository.Repository;
import net.danielrickman.api.util.CollectionUtil;
import net.danielrickman.oitq.sidebar.OITQSidebar;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OITQRepository extends Repository<OITQProfile> {

    public void setSidebar(UUID uuid, OITQSidebar sidebar) {
        Objects
                .requireNonNull(getPlayerMap().get(uuid), "Error setting sidebar for " + uuid)
                .setSidebar(sidebar);
    }

    public OITQSidebar getSidebar(UUID uuid) {
        return getPlayerMap().get(uuid).getSidebar();
    }

    public void removeLife(UUID uuid) {
        var profile = getPlayerMap().get(uuid);
        var lives = profile.getLives();
        profile.setLives(lives - 1);
        getPlayerMap().get(uuid).getSidebar().updateLivesLeft(profile.getLives());
    }

    public int getLives(UUID uuid) {
        return getPlayerMap().get(uuid).getLives();
    }

    public void addPoints(UUID uuid, int amount) {
        var profile = getPlayerMap().get(uuid);
        profile.setPoints(profile.getPoints() + amount);
        profile.getSidebar().updatePoints(profile.getPoints());
    }

    public void addKill(UUID uuid) {
        var profile = getPlayerMap().get(uuid);
        profile.setKills(profile.getKills() + 1);
        profile.getSidebar().updateKills(profile.getKills());
    }

    public void updateRankings(UUID uuid) {
        HashMap<UUID, Integer> pointsMap = new HashMap<>();
        getPlayerMap().forEach((k, v) -> pointsMap.put(k, v.getPoints()));
        getSidebar(uuid).updateRankings(CollectionUtil.orderByValue(pointsMap, false));
    }
}