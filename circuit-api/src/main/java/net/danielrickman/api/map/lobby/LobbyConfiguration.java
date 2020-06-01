package net.danielrickman.api.map.lobby;

import lombok.Data;
import net.danielrickman.api.hologram.HologramTemplate;
import net.danielrickman.api.map.MapConfiguration;

import java.util.List;
import java.util.Optional;

@Data
public class LobbyConfiguration extends MapConfiguration {

    private List<NPC> npcs;
    private List<HologramTemplate> holograms;
    private List<SkinTexture> textures;

    public SkinTexture getTextureByID(String id) {
        return textures
                .stream()
                .filter(texture -> texture.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public Optional<NPC> getNPCByGameID(String id) {
        return npcs
                .stream()
                .filter(npc -> npc.getId().equalsIgnoreCase(id))
                .findFirst();
    }
}