package net.danielrickman.api.map;

import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BoxRegion {

    private final MapLocation lower, upper;

    public boolean containsLocation(Location location) {
        return getAllLocations(location.getWorld()).stream().anyMatch(loc -> loc.getX() == location.getBlockX() && loc.getY() == location.getBlockY() && loc.getZ() == location.getBlockZ());
    }

    public boolean containsEntity(Entity entity) {
        return containsLocation(entity.getLocation());
    }

    //public boolean containsBlock(World world, Material material) {
    //    return getAllLocations(world).stream().anyMatch(location -> world.getBlockAt(location).getType() == material);
    //}

    public List<Location> getBlocksOfType(World world, List<Material> materials) {
        List<Location> locations = new ArrayList<>();
        getAllLocations(world)
                .stream()
                .filter(location -> materials.contains(location.getBlock().getType()))
                .forEach(locations::add);
        return locations;
    }

    public List<Location> getAllLocations(World world) {
        List<Location> locations = new ArrayList<>();
        for (double x = Math.min(lower.getX(), upper.getX()); x <= Math.max(lower.getX(), upper.getX()); x++) {
            for (double y = Math.min(lower.getY(), upper.getY()); y <= Math.max(lower.getY(), upper.getY()); y++) {
                for (double z = Math.min(lower.getZ(), upper.getZ()); z <= Math.max(lower.getZ(), upper.getZ()); z++) {
                    locations.add(new Location(world, x, y, z).toBlockLocation());
                }
            }
        }
        return locations;
    }
}