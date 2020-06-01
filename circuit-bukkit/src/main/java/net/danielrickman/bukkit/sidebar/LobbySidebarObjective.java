package net.danielrickman.bukkit.sidebar;

import net.danielrickman.api.scoreboard.IObjective;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.RenderType;

public class LobbySidebarObjective implements IObjective {
    @Override
    public String getName() {
        return "sidebar";
    }

    @Override
    public String getDisplayName() {
        return ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "CIRCUIT";
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
