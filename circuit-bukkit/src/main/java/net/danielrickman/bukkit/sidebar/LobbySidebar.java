package net.danielrickman.bukkit.sidebar;

import com.google.common.base.Preconditions;
import net.citizensnpcs.api.ai.tree.Precondition;
import net.danielrickman.bukkit.Circuit;
import net.danielrickman.api.player.CorePlayer;
import net.danielrickman.api.rank.Rank;
import net.danielrickman.api.scoreboard.Sidebar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

public class LobbySidebar extends Sidebar {

    private final Circuit circuit;

    public LobbySidebar(Circuit circuit, CorePlayer cp) {
        super(cp);
        this.circuit = circuit;
    }

    @Override
    public void addObjectives() {
        addObjective(new LobbySidebarObjective());
    }

    @Override
    public void addTeams() {
        Team team;
        for (Rank rank : Rank.values()) {
            team = getScoreboard().registerNewTeam(rank.getName());
            team.setColor(rank.getNameColor());
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }
    }

    @Override
    public void update() {
        setLine(5, ChatColor.BLACK.toString());
        setLine(4, ChatColor.WHITE + "Players: " + ChatColor.YELLOW + circuit.getPlayerRepository().getCount() + ChatColor.WHITE + "/" + ChatColor.YELLOW + Bukkit.getServer().getMaxPlayers());
        setLine(3, ChatColor.WHITE + "Rank: " + ChatColor.YELLOW + getCorePlayer().getRank().getName());
        setLine(2, ChatColor.DARK_BLUE.toString());
        setLine(1, ChatColor.WHITE + "Coins: " + ChatColor.YELLOW + getCorePlayer().getCoins());
        setLine(0, ChatColor.DARK_GREEN.toString());
        circuit.getPlayerRepository().forEach(cp -> {
            Objects.requireNonNull(getScoreboard().getTeam(cp.getRank().getName())).addEntry(cp.getPlayer().getName());
        });
    }
}