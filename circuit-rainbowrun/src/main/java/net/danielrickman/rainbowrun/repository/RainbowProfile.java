package net.danielrickman.rainbowrun.repository;

import lombok.Data;
import net.danielrickman.api.repository.profile.GameProfile;
import net.danielrickman.api.repository.profile.IProfile;
import net.danielrickman.rainbowrun.sidebar.RainbowSidebar;

@Data
public class RainbowProfile extends GameProfile {

    private RainbowSidebar sidebar;
    private int blocksDestroyed;
}
