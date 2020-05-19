package net.danielrickman.rainbowrun.repository;

import lombok.Data;
import net.danielrickman.api.repository.profile.IProfile;
import net.danielrickman.rainbowrun.sidebar.RRPlayerSidebar;

@Data
public class RRProfile implements IProfile {

    private RRPlayerSidebar sidebar;
    private int blocksDestroyed;
}
