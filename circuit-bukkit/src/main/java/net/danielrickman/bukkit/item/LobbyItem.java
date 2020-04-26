package net.danielrickman.bukkit.item;

import lombok.RequiredArgsConstructor;
import net.danielrickman.api.player.CorePlayer;
import net.danielrickman.api.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public enum LobbyItem {

    START_ITEM(4, ChatColor.RED + "Start" + ChatColor.GRAY + " (Use when ready)", Material.REDSTONE, 1);

    private final int slot;
    private final String displayName;
    private final Material material;
    private final int amount;

    public void give(CorePlayer cp) {
        cp.getPlayer().getInventory().setItem(slot, new ItemBuilder(material).displayName(displayName).amount(amount).build());
    }

    public void give(Player p) {
        p.getInventory().setItem(slot, new ItemBuilder(material).displayName(displayName).amount(amount).build());
    }
}
