package net.danielrickman.api.scoreboard;

import lombok.RequiredArgsConstructor;
import net.danielrickman.api.plugin.CircuitGame;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.RenderType;

@RequiredArgsConstructor
public class GameSidebarObjective implements IObjective {

    private final CircuitGame game;

    @Override
    public String getName() {
        return "GAME_SIDEBAR";
    }

    @Override
    public String getDisplayName() {
        return game.getBoldDisplayName();
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