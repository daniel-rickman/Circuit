package net.danielrickman.api.map;

import lombok.Data;

@Data
public abstract class MapConfiguration {

    private int chunkRadius;
    private String worldName;

}
