package net.danielrickman.oitq.sidebar;

import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.scoreboard.GameSidebarObjective;
import net.danielrickman.api.scoreboard.Sidebar;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.api.util.Timer;
import net.danielrickman.oitq.OneInTheQuiver;
import net.danielrickman.oitq.repository.OITQProfile;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class OITQSidebar extends Sidebar {

    private final OneInTheQuiver game;
    private final GlobalRepository global;
    private Team team;

    public OITQSidebar(Player player, OneInTheQuiver game) {
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
        team = getScoreboard().registerNewTeam("PLAYER");
        team.setCanSeeFriendlyInvisibles(true);
        team.setColor(ChatColor.RED);
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        PlayerUtil.forEach(player -> team.addEntry(player.getName()));
    }

    @Override
    public void update() {
        setLine(8, ChatColor.BLACK.toString());
        setLine(7, "Timer: " + ChatColor.DARK_GRAY + "Waiting...");
        setLine(6, "Players left: " + ChatColor.YELLOW + PlayerUtil.getAlivePlayers(global).size());
        setLine(5, ChatColor.DARK_BLUE.toString());
        setLine(4, "Lives left: " + ChatColor.RED + OneInTheQuiver.LIVES);
        setLine(3, "Your Kills: " + ChatColor.YELLOW + "0");
        setLine(2, "Your Points: " + ChatColor.YELLOW + "0");
        setLine(1, ChatColor.DARK_GREEN.toString());
        setLine(0, ChatColor.WHITE.toString() + ChatColor.BOLD + "Ranking:");
        setLine(-1, ChatColor.GREEN + "#1 " + ChatColor.DARK_GRAY + "Waiting...");
        setLine(-2, ChatColor.GREEN + "#2 " + ChatColor.DARK_GRAY + "Waiting...");
        setLine(-3, ChatColor.GREEN + "#3 " + ChatColor.DARK_GRAY + "Waiting...");
        setLine(-4, ChatColor.DARK_RED.toString() + ChatColor.DARK_RED.toString());
    }

    public void updateTimer(Timer timer) {
        this.setLine(7, "Timer: " + ChatColor.YELLOW + timer.getFormattedTimer());
    }

    public void updatePlayersLeft() {
        setLine(6, "Players left: " + ChatColor.YELLOW + PlayerUtil.getAlivePlayers(global).size());
    }

    public void updateLivesLeft(int amount) {
        setLine(4, "Lives left: " + ChatColor.RED + ((amount > 0) ? amount : "DEAD"));
    }

    public void updateKills(int amount) {
        setLine(3, "Your Kills: " + ChatColor.YELLOW + amount);
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