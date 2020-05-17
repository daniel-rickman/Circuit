package net.danielrickman.bukkit.inventory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.danielrickman.api.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@RequiredArgsConstructor
public enum OperatorInventory {

    START_ITEM(3, ChatColor.RED + "Start" + ChatColor.GRAY + " (Use when ready)", Material.REDSTONE, 1),
    RELOAD_MAPS_ITEM(5, ChatColor.BLUE + "Reload Maps", Material.MAGMA_CREAM, 1);

    private final int slot;
    private final String displayName;
    private final Material material;
    private final int amount;

    public static void giveAll(Player p) {
        for (OperatorInventory item : OperatorInventory.values()) {
            p.getInventory().setItem(item.getSlot(), ItemBuilder.ofType(item.getMaterial()).displayName(item.getDisplayName()).amount(item.getAmount()).build());
        }
    }

    public boolean matches(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            return item.getItemMeta().getDisplayName().equals(this.displayName) && item.getType() == this.material;
        } else {
            return false;
        }
    }
}