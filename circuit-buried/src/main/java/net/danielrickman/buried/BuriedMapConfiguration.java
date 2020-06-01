package net.danielrickman.buried;

import lombok.Data;
import net.danielrickman.api.map.BoxRegion;
import net.danielrickman.api.map.MapConfiguration;

@Data
public class BuriedMapConfiguration extends MapConfiguration {

    private BoxRegion dropRegion;
    private BoxRegion mapRegion;
}
