package net.danielrickman.rainbowrun.sidebar;

import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.scoreboard.Sidebar;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.api.util.Timer;
import net.danielrickman.rainbowrun.RainbowRun;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RRPlayerSidebar extends Sidebar {

    private final RainbowRun rr;
    private final GlobalRepository global;

    public RRPlayerSidebar(Player player, RainbowRun rr, GlobalRepository global) {
        super(player);
        this.rr = rr;
        this.global = global;
    }

    @Override
    public void addObjectives() {
        addObjective(new RRPlayerSidebarObjective(rr));
    }

    @Override
    public void addTeams() {
    }

    @Override
    public void update() {
        setLine(4, ChatColor.BLACK.toString());
        setLine(3, "Timer: " + ChatColor.DARK_GRAY + "Waiting...");
        setLine(2, ChatColor.DARK_GREEN.toString());
        setLine(1, "Players left: " + ChatColor.RED + PlayerUtil.getAlivePlayers(global).size());
        setLine(0, ChatColor.DARK_BLUE.toString());
    }

    public void updateTimer(Timer timer) {
        setLine(3, "Timer: " + ChatColor.YELLOW + timer.getFormattedTimer());
    }

    public void updatePlayersLeft(int amount) {
        setLine(2, "Players left: " + ChatColor.RED + amount);
    }
}