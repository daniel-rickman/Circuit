package net.danielrickman.api.rank;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@Getter
public enum Rank {

    DEFAULT("Default", ChatColor.YELLOW),
    OP("OP", ChatColor.RED);

    private final String name;
    private final ChatColor nameColor;

    public static Rank get(Player player) {
        return (player.isOp()) ? Rank.OP : Rank.DEFAULT;
    }

    public static String getFormattedName(Player player) {
        return get(player).getNameColor() + player.getName();
    }
}