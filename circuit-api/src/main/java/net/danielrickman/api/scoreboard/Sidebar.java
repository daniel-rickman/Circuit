package net.danielrickman.api.scoreboard;

import lombok.Getter;
import net.danielrickman.api.player.CorePlayer;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;

public abstract class Sidebar {

    @Getter
    private final Scoreboard scoreboard;
    @Getter
    private final CorePlayer corePlayer;
    @Getter
    private HashMap<DisplaySlot, Objective> objectives = new HashMap<>();
    private HashMap<Integer, String> sidebarScores = new HashMap<>();

    public Sidebar(CorePlayer corePlayer) {
        this.corePlayer = corePlayer;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    public Sidebar initialise() {
        addObjectives();
        addTeams();
        update();
        corePlayer.getPlayer().setScoreboard(scoreboard);
        return this;
    }

    protected void setLine(int id, String line) {
        if (id < -16 || id > 16) {
            throw new IllegalArgumentException("Sidebar score ID must be between -16 and 16");
        }
        if (sidebarScores.get(id) != null) {
            scoreboard.resetScores(sidebarScores.get(id));
        }
        objectives.get(DisplaySlot.SIDEBAR).getScore(line).setScore(id);
        sidebarScores.put(id, line);
    }

    public abstract void addObjectives();
    public abstract void addTeams();
    public abstract void update();

    public void addObjective(IObjective objective) {
        var o = scoreboard.registerNewObjective(objective.getName(), "dummy", objective.getDisplayName());
        o.setDisplaySlot(objective.getSlot());
        objectives.put(o.getDisplaySlot(), o);
    }
}