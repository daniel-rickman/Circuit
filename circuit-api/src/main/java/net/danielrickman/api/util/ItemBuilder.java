package net.danielrickman.api.util;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
public class ItemBuilder {

    private final Material material;
    private String displayName = " ";
    private final List<String> lore = new ArrayList<>();
    private boolean isUnbreakable = false;
    private final HashMap<Enchantment, Integer> enchantmentMap = new HashMap<>();
    private int amount = 1;

    public static ItemBuilder ofType(Material material) {
        return new ItemBuilder(material);
    }

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

    public ItemBuilder enchant(Enchantment enchantment, Integer level) {
        enchantmentMap.put(enchantment, level);
        return this;
    }

    public ItemBuilder setUnbreakable() {
        isUnbreakable = true;
        return this;
    }

    public ItemStack build() {
        var itemStack = new ItemStack(material).asQuantity(amount);
        var itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        itemMeta.setUnbreakable(isUnbreakable);
        if (!enchantmentMap.isEmpty()) {
            enchantmentMap.forEach((key, value) -> itemMeta.addEnchant(key, value, true));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}