package net.danielrickman.api.util;

import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

@RequiredArgsConstructor
public enum CircuitPrefix {

    CIRCUIT(ChatColor.GOLD + "Circuit"),
    COINS(ChatColor.YELLOW + "Coins"),
    VOTE(ChatColor.LIGHT_PURPLE + "Vote"),
    ADMIN(ChatColor.RED + "Admin");

    private final String prefix;

    public String getPrefix() {
        return prefix + ChatColor.DARK_GRAY + " \u00BB " + ChatColor.WHITE;
    }
}
