package net.danielrickman.api.map.lobby;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.danielrickman.api.map.MapLocation;
import org.bukkit.Color;
import org.bukkit.entity.EntityType;

@RequiredArgsConstructor
public class NPC {

    @Getter
    private final String id;
    private final String type;
    private final int colorRGB;
    @Getter
    private final MapLocation mapLocation;

    public EntityType getType() {
        return EntityType.valueOf(type);
    }

    public Color getColor() {
        return Color.fromRGB(colorRGB);
    }
}