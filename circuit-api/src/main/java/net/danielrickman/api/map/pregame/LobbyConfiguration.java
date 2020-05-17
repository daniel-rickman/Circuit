package net.danielrickman.api.map.pregame;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.danielrickman.api.hologram.HologramTemplate;
import net.danielrickman.api.map.MapConfiguration;
import net.danielrickman.api.map.MapLocation;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data()
public class LobbyConfiguration extends MapConfiguration {

    private MapLocation spawnLocation;
    private List<NPC> npcs;
    private NPC topPlayerNpc;
    private List<HologramTemplate> hologramTemplates;

}