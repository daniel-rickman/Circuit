package net.danielrickman.api.map.lobby;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SkinTexture {

    private final String id;
    private final String uuid;
    private final String data;
    private final String signature;

}