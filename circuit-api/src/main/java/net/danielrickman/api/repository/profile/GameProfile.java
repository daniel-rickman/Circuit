package net.danielrickman.api.repository.profile;

import lombok.Data;

@Data
public abstract class GameProfile implements IProfile {

    public int points = 0;
}
