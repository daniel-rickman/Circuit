package net.danielrickman.bukkit.map;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.danielrickman.api.util.MapLocation;

@Getter
@RequiredArgsConstructor
public class NPC {

    private final String id;
    private final String skinUuid;
    private final String skinData;
    private final String skinSignature;
    private final MapLocation mapLocation;

}
