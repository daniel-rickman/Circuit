package net.danielrickman.rainbowrun;

import lombok.Data;
import net.danielrickman.api.map.MapConfiguration;
import net.danielrickman.api.map.MapLocation;
import net.danielrickman.api.map.BoxRegion;

@Data
public class RRMapConfiguration extends MapConfiguration {

    private BoxRegion region;
    private int deathY;
}
