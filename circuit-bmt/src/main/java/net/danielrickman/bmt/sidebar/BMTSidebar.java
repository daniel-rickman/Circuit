package net.danielrickman.bmt.sidebar;

import net.danielrickman.api.rank.Rank;
import net.danielrickman.api.scoreboard.GameSidebarObjective;
import net.danielrickman.api.scoreboard.Sidebar;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.api.util.Timer;
import net.danielrickman.bmt.BuildMyThing;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class BMTSidebar extends Sidebar {

    private final BuildMyThing game;
    private Team team;

    public BMTSidebar(BuildMyThing game, Player player) {
        super(player);
        this.game = game;
    }

    @Override
    public void addObjectives() {
        addObjective(new GameSidebarObjective(game));
    }

    @Override
    public void addTeams() {
        team = getScoreboard().registerNewTeam("ghost");
        team.setCanSeeFriendlyInvisibles(true);
    }

    @Override
    public void update() {
        setLine(6, ChatColor.BLACK.toString());
        setLine(5, "Time Left: " + ChatColor.DARK_GRAY + "Waiting...");
        setLine(4, "Builder: " + ChatColor.DARK_GRAY + "Waiting...");
        setLine(3, ChatColor.DARK_BLUE.toString());
        setLine(2, "Points: " + ChatColor.DARK_AQUA + "0");
        setLine(1, ChatColor.DARK_GREEN.toString());
        setLine(0, ChatColor.WHITE.toString() + ChatColor.BOLD + "Ranking:");
        setLine(-1, ChatColor.GREEN + "#1 " + ChatColor.DARK_GRAY + "Waiting...");
        setLine(-2, ChatColor.GREEN + "#2 " + ChatColor.DARK_GRAY + "Waiting...");
        setLine(-3, ChatColor.GREEN + "#3 " + ChatColor.DARK_GRAY + "Waiting...");
        setLine(-4, ChatColor.DARK_AQUA.toString());
        PlayerUtil.forEach(player -> team.addEntry(player.getName()));
    }

    public void updateTimer(Timer timer) {
        setLine(5, "Time Left: " + ChatColor.YELLOW + timer.getFormattedTimer());
    }

    public void setBuilder(Player player) {
        setLine(4, "Builder: " + Rank.getFormattedName(player));
    }

    public void updatePoints(int amount) {
        setLine(2, "Your Points: " + ChatColor.YELLOW + amount);
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