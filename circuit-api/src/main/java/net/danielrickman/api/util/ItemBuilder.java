package net.danielrickman.api.util;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ItemBuilder {

    private final Material material;
    private String displayName = " ";
    private List<String> lore = new ArrayList<>();
    private boolean isEnchanted = false;
    private int amount = 1;

    public ItemBuilder displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder lore(String line) {
        this.lore.add(line);
        return this;
    }

    public ItemBuilder enchant() {
        this.isEnchanted = true;
        return this;
    }

    public ItemStack build() {
        var itemStack = new ItemStack(material).asQuantity(amount);
        var itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        if (isEnchanted) {
            itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
