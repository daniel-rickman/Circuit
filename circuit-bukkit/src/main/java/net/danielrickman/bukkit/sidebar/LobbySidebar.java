package net.danielrickman.bukkit.sidebar;

import net.danielrickman.api.rank.Rank;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.scoreboard.Sidebar;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.bukkit.Circuit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class LobbySidebar extends Sidebar {

    private final Circuit circuit;
    private final GlobalRepository global;

    public LobbySidebar(Player player, Circuit circuit, GlobalRepository global) {
        super(player);
        this.circuit = circuit;
        this.global = global;
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

    /*
    Warning suppressed for Line 55.
    There will always be a team on the scoreboard for each rank so an NPE will not be thrown (See #addTeams() above)
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public void update() {
        setLine(5, ChatColor.BLACK.toString());
        setLine(4, ChatColor.WHITE + "Players: " + ChatColor.YELLOW + Bukkit.getOnlinePlayers().size() + ChatColor.WHITE + "/" + ChatColor.YELLOW + Bukkit.getServer().getMaxPlayers());
        setLine(3, ChatColor.WHITE + "Rank: " + ChatColor.YELLOW + Rank.get(getPlayer()).getName());
        setLine(2, ChatColor.DARK_BLUE.toString());
        setLine(1, ChatColor.WHITE + "Coins: " + ChatColor.YELLOW + global.getCoins(getPlayer().getUniqueId()));
        setLine(0, ChatColor.DARK_GREEN.toString());
        PlayerUtil.forEach(player -> {
            getScoreboard().getTeam(Rank.get(player).getName()).addEntry(player.getName());
        });
    }
}