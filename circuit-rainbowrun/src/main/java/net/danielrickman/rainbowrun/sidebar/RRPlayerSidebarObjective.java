package net.danielrickman.rainbowrun.sidebar;

import lombok.RequiredArgsConstructor;
import net.danielrickman.api.scoreboard.IObjective;
import net.danielrickman.rainbowrun.RainbowRun;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.RenderType;

@RequiredArgsConstructor
public class RRPlayerSidebarObjective implements IObjective {

    private final RainbowRun rr;

    @Override
    public String getName() {
        return "sidebar";
    }

    @Override
    public String getDisplayName() {
        return rr.getBoldDisplayName();
    }

    @Override
    public DisplaySlot getSlot() {
        return DisplaySlot.SIDEBAR;
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.INTEGER;
    }
}
