package net.danielrickman.oitq.repository;

import lombok.Data;
import net.danielrickman.api.repository.profile.GameProfile;
import net.danielrickman.api.repository.profile.IProfile;
import net.danielrickman.oitq.OneInTheQuiver;
import net.danielrickman.oitq.sidebar.OITQSidebar;

@Data
public class OITQProfile extends GameProfile {

    private OITQSidebar sidebar;
    private int lives = OneInTheQuiver.LIVES;
    private int kills = 0;
}
