package net.danielrickman.api.map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;

@Getter
@RequiredArgsConstructor
public class Map<T extends MapConfiguration> {

    private final T configuration;
    private final World world;
}