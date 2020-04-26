package net.danielrickman.api.player;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.danielrickman.api.rank.Rank;
import net.danielrickman.api.scoreboard.Sidebar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class CorePlayer {

    private final UUID uuid;
    private int coins;
    private Sidebar sidebar;

    public boolean isOperator() {
        return getPlayer().isOp();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public Rank getRank() {
        return (isOperator()) ? Rank.OP : Rank.DEFAULT;
    }

    public String getFormattedName() {
        return getRank().getNameColor() + getPlayer().getName();
    }
}
