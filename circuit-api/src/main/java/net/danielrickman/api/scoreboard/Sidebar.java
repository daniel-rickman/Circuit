package net.danielrickman.api.scoreboard;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;

public abstract class Sidebar {

    @Getter
    private final Scoreboard scoreboard;
    @Getter
    private final Player player;
    @Getter
    private final HashMap<DisplaySlot, Objective> objectives = new HashMap<>();
    private final HashMap<Integer, String> sidebarScores = new HashMap<>();
    @Getter
    private boolean isInitialised;

    public Sidebar(Player player) {
        this.player = player;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    /*
    Warning suppressed on line 40.
    #initialise() will always initialise a Sidebar of type T. T = Child class.
     */
    @SuppressWarnings("unchecked")
    public <T extends Sidebar> T initialise() {
        isInitialised = true;
        addObjectives();
        addTeams();
        update();
        player.setScoreboard(scoreboard);
        return (T) this;
    }

    public final void destroy() {
        scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    protected void setLine(int id, String line) {
        if (id < -16 || id > 16) {
            throw new IllegalArgumentException("Sidebar score ID must be between -16 and 16");
        }
        if (!isInitialised) {
            throw new IllegalStateException("Sidebar must be initialised with #initialise(), when setting the player's sidebar");
        }
        if (sidebarScores.get(id) != null) {
            scoreboard.resetScores(sidebarScores.get(id));
        }
        objectives.get(DisplaySlot.SIDEBAR).getScore(line).setScore(id);
        sidebarScores.put(id, line);
    }

    protected final void addObjective(IObjective objective) {
        var o = scoreboard.registerNewObjective(objective.getName(), "dummy", objective.getDisplayName());
        o.setDisplaySlot(objective.getSlot());
        objectives.put(o.getDisplaySlot(), o);
    }

    public abstract void addObjectives();

    public abstract void addTeams();

    public abstract void update();
}