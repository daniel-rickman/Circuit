package net.danielrickman.bmt;

import lombok.Data;
import net.danielrickman.api.map.BoxRegion;
import net.danielrickman.api.map.MapConfiguration;
import net.danielrickman.api.map.MapLocation;

import java.util.List;

@Data
public class BMTMapConfiguration extends MapConfiguration {

    private MapLocation builderSpawnLocation;
    private BoxRegion buildRegion;
    private List<String> words;
}
