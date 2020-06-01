package net.danielrickman.buried.repository;

import lombok.Data;
import net.danielrickman.api.repository.profile.GameProfile;
import net.danielrickman.api.repository.profile.IProfile;
import net.danielrickman.buried.sidebar.BuriedSidebar;

@Data
public class BuriedProfile extends GameProfile {

    private BuriedSidebar sidebar;

}
