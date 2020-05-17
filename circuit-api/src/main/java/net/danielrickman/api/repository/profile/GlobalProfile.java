package net.danielrickman.api.repository.profile;

import lombok.Data;
import net.danielrickman.api.role.PlayerRole;

@Data
public class GlobalProfile implements IProfile {

    private int coins = 0;
    private PlayerRole role = PlayerRole.PLAYER;
}
