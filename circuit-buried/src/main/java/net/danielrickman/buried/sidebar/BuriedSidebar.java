package net.danielrickman.buried.sidebar;

import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.scoreboard.GameSidebarObjective;
import net.danielrickman.api.scoreboard.Sidebar;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.api.util.Timer;
import net.danielrickman.buried.Buried;
import net.danielrickman.buried.repository.BuriedProfile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class BuriedSidebar extends Sidebar {

    private final Buried game;
    private final GlobalRepository global;

    public BuriedSidebar(Player player, Buried game) {
        super(player);
        this.game = game;
        this.global = game.getPlugin().getGlobalRepository();
    }

    @Override
    public void addObjectives() {
        addObjective(new GameSidebarObjective(game));
    }

    @Override
    public void addTeams() {
    }

    @Override
    public void update() {
        setLine(4, ChatColor.BLACK.toString());
        setLine(3, "Timer: " + ChatColor.DARK_GRAY + "Waiting...");
        setLine(2, "Players Left: " + ChatColor.YELLOW + PlayerUtil.getAlivePlayers(global).size());
        setLine(1, ChatColor.DARK_BLUE.toString());
        setLine(0, ChatColor.WHITE.toString() + ChatColor.BOLD + "Ranking:");
        setLine(-1, ChatColor.GREEN + "#1 " + ChatColor.DARK_GRAY + "Waiting...");
        setLine(-2, ChatColor.GREEN + "#2 " + ChatColor.DARK_GRAY + "Waiting...");
        setLine(-3, ChatColor.GREEN + "#3 " + ChatColor.DARK_GRAY + "Waiting...");
        setLine(-4, ChatColor.DARK_GREEN.toString());
    }

    public void updateTimer(Timer timer) {
        setLine(3, "Timer: " + ChatColor.YELLOW + timer.getFormattedTimer());
    }

    public void updatePlayersLeft() {
        setLine(2, "Players Left: " + ChatColor.YELLOW + PlayerUtil.getAlivePlayers(global).size());
    }

    public void updateRankings(List<Map.Entry<UUID, Integer>> topPlayers) {
        var i = 1;
        if (!topPlayers.isEmpty()) {
            for (Map.Entry<UUID, Integer> entry : topPlayers) {
                setLine(i * -1, ChatColor.GREEN + "#" + i + ChatColor.WHITE + " " + Objects.requireNonNull(Bukkit.getPlayer(entry.getKey())).getName() + ChatColor.AQUA + " [" + entry.getValue() + "]");
                i++;
            }
        }
    }
}