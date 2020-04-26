package net.danielrickman.api.scoreboard;

import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.RenderType;

public interface IObjective {

    String getName();
    String getDisplayName();
    DisplaySlot getSlot();
    RenderType getRenderType();


}
