package net.danielrickman.bukkit.map;

import lombok.Data;
import net.danielrickman.api.hologram.Hologram;
import net.danielrickman.api.hologram.HologramTemplate;
import net.danielrickman.api.map.MapConfiguration;
import net.danielrickman.api.util.MapLocation;

import java.util.List;

@Data
public class LobbyConfiguration extends MapConfiguration {

    private MapLocation spawnLocation;
    private List<NPC> npcs;
    private NPC topPlayerNpc;
    private List<HologramTemplate> hologramTemplates;

}