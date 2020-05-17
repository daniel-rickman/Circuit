package net.danielrickman.api.hologram;

import lombok.Data;
import net.danielrickman.api.map.MapLocation;

@Data
public class HologramTemplate {

    private final String text;
    private final MapLocation mapLocation;
}
