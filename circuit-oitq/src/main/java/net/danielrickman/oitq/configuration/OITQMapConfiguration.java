package net.danielrickman.oitq.configuration;

import lombok.Data;
import net.danielrickman.api.map.MapConfiguration;
import net.danielrickman.api.map.MapLocation;

import java.util.List;

@Data
public class OITQMapConfiguration extends MapConfiguration {

    private List<MapLocation> spawnLocations;

}
