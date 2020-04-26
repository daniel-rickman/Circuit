package net.danielrickman.api.rank;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

@RequiredArgsConstructor
@Getter
public enum Rank {

    DEFAULT("Default", ChatColor.YELLOW),
    OP("OP", ChatColor.RED);

    private final String name;
    private final ChatColor nameColor;
}