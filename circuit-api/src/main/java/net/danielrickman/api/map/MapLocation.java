package net.danielrickman.api.map;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;

@Data
@RequiredArgsConstructor
public class MapLocation {

    private final double x, y, z;
    private final float yaw, pitch;

    public static MapLocation fromWorldLocation(Location location) {
        return new MapLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public Location toWorldLocation(World world) {
        return new Location(world, x, y, z, yaw, pitch);
    }
}