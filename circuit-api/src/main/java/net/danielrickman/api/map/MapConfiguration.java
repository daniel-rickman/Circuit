package net.danielrickman.api.map;

import lombok.Data;

import java.util.List;

@Data
public abstract class MapConfiguration {

    private int chunkRadius;
    private String worldName;
    private List<MapLocation> spawnLocations;
}
