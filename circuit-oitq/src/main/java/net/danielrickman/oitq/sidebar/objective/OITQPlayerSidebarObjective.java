package net.danielrickman.oitq.sidebar.objective;

import lombok.RequiredArgsConstructor;
import net.danielrickman.api.scoreboard.IObjective;
import net.danielrickman.oitq.OneInTheQuiver;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.RenderType;

@RequiredArgsConstructor
public class OITQPlayerSidebarObjective implements IObjective {

    private final OneInTheQuiver oitq;

    @Override
    public String getName() {
        return "sidebar";
    }

    @Override
    public String getDisplayName() {
        return oitq.getBoldDisplayName();
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
