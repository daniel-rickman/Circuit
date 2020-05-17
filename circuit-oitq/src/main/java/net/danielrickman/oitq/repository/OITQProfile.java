package net.danielrickman.oitq.repository;

import lombok.Data;
import net.danielrickman.api.repository.profile.IProfile;
import net.danielrickman.oitq.OneInTheQuiver;
import net.danielrickman.oitq.sidebar.OITQPlayerSidebar;

@Data
public class OITQProfile implements IProfile {

    private OITQPlayerSidebar sidebar;
    private int lives = OneInTheQuiver.LIVES;
    private int points = 0;
    private int kills = 0;
}
