package net.danielrickman.api.map.pregame;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.danielrickman.api.map.MapLocation;

@Getter
@RequiredArgsConstructor
public class NPC {

    private final String id;
    private final String skinUuid;
    private final String skinData;
    private final String skinSignature;
    private final MapLocation mapLocation;

}
