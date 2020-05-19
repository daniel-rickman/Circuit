package net.danielrickman.rainbowrun.listener.game;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.state.event.StateEndEvent;
import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.util.ItemBuilder;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.api.util.WorldSetting;
import net.danielrickman.rainbowrun.RainbowRun;
import net.danielrickman.rainbowrun.sidebar.RRPlayerSidebar;
import net.danielrickman.rainbowrun.task.RRArmorColourTask;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitTask;

public class RRGameTransitionListener extends CircuitListener {

    private final RainbowRun rr;
    private final GlobalRepository global;
    private BukkitTask armorTask;

    public RRGameTransitionListener(CircuitPlugin plugin, RainbowRun rr, GlobalRepository global) {
        super(plugin);
        this.rr = rr;
        this.global = global;
    }

    @EventHandler
    private void on(StateStartEvent e) {
        WorldSetting.PVP.setAllowed(true);
        armorTask = new RRArmorColourTask(global).runTaskTimer(getPlugin(), 0, 1);
        PlayerUtil.forEach(player -> {
            player.sendTitle(ChatColor.GREEN.toString() + ChatColor.BOLD + "START", "", 10, 20, 10);
            player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0f, 1.0f);
            rr.getStats().setSidebar(player.getUniqueId(), new RRPlayerSidebar(player, rr, global).initialise());
            var inventory = player.getInventory();
            inventory.setItem(EquipmentSlot.HEAD, ItemBuilder.ofType(Material.LEATHER_HELMET).build());
            inventory.setItem(EquipmentSlot.CHEST, ItemBuilder.ofType(Material.LEATHER_CHESTPLATE).build());
            inventory.setItem(EquipmentSlot.LEGS, ItemBuilder.ofType(Material.LEATHER_LEGGINGS).build());
            inventory.setItem(EquipmentSlot.FEET, ItemBuilder.ofType(Material.LEATHER_BOOTS).build());
            player.getInventory().setItem(0,
                    ItemBuilder
                    .ofType(Material.MUTTON)
                    .displayName(ChatColor.RED.toString() + ChatColor.BOLD + "Mutton Slapper")
                    .enchant(Enchantment.KNOCKBACK)
                    .build());
            player.getInventory()
                    .addItem(ItemBuilder
                            .ofType(Material.FEATHER)
                            .displayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Leap " + ChatColor.GRAY + "(Right click)")
                            .amount(1)
                            .build());
        });
    }

    @EventHandler
    private void on(StateEndEvent e) {
        WorldSetting.PVP.setAllowed(false);
        armorTask.cancel();
    }
}
