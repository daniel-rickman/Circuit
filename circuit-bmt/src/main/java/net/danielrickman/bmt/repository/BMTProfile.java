package net.danielrickman.bmt.repository;

import lombok.Data;
import net.danielrickman.api.repository.profile.GameProfile;
import net.danielrickman.bmt.sidebar.BMTSidebar;

@Data
public class BMTProfile extends GameProfile {

    private BMTSidebar sidebar;
    private boolean hasGuessed = false;
}
