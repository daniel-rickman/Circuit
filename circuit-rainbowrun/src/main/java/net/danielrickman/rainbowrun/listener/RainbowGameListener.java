package net.danielrickman.rainbowrun.listener;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.role.PlayerRole;
import net.danielrickman.api.role.PlayerRoleChangeEvent;
import net.danielrickman.api.state.event.StateEndEvent;
import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.task.GameStartTitleTask;
import net.danielrickman.api.util.ItemBuilder;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.api.util.WorldSetting;
import net.danielrickman.rainbowrun.RRMapConfiguration;
import net.danielrickman.rainbowrun.RainbowRun;
import net.danielrickman.rainbowrun.repository.RainbowRepository;
import net.danielrickman.rainbowrun.sidebar.RainbowSidebar;
import net.danielrickman.rainbowrun.task.RainbowArmorColourTask;
import net.danielrickman.rainbowrun.task.RainbowBlockBreakTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;

import static net.danielrickman.rainbowrun.RainbowRun.BREAKABLE_MATERIALS;

public class RainbowGameListener extends CircuitListener {

    private final static Material LEAP_ITEM_MATERIAL = Material.FEATHER;

    private final GlobalRepository global;
    private final RainbowRun game;
    private final RainbowRepository stats;
    private final RRMapConfiguration mapConfiguration;

    private final HashMap<Location, Material> brokenBlocks = new HashMap<>();

    private BukkitTask armorTask;

    public RainbowGameListener(CircuitPlugin plugin, RainbowRun game) {
        super(plugin);
        this.game = game;
        this.global = getPlugin().getGlobalRepository();
        this.stats = game.getStats();
        this.mapConfiguration = game.getMapConfiguration();
    }

    @EventHandler
    private void on(EntityDamageByEntityEvent e) {
        e.setDamage(0);
    }

    @EventHandler
    private void on(InventoryOpenEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    private void on(PlayerInteractEvent e) {
        var item = e.getItem();
        var player = e.getPlayer();
        if (item != null && item.getType() == LEAP_ITEM_MATERIAL) {
            player.setVelocity(new Vector(0, 2, 0));
            player.getInventory().remove(item);
        }
    }

    @EventHandler
    private void on(PlayerMoveEvent e) {
        var player = e.getPlayer();
        if (player.getLocation().getY() <= mapConfiguration.getDeathY()) {
            global.setRole(player.getUniqueId(), PlayerRole.SPECTATOR);
        } else {
            if (mapConfiguration.getRegion().containsEntity(player)) {
                var block = game.getWorld().getBlockAt(player.getLocation().subtract(0, 1, 0));
                if (BREAKABLE_MATERIALS.contains(block.getType())) {
                    var uuid = player.getUniqueId();
                    new RainbowBlockBreakTask(block).runTaskTimer(game.getPlugin(), 0, 3);
                    stats.incrementBlocksDestroyed(uuid);
                    player.setLevel(stats.getBlocksDestroyed(uuid));
                    brokenBlocks.put(block.getLocation(), block.getType());
                }
            }
        }
    }

    @EventHandler
    private void on(PlayerRoleChangeEvent e) {
        var spectator = e.getPlayer();
        if (e.getNewRole() == PlayerRole.SPECTATOR) {
            Location spawnLocation = (mapConfiguration.getSpawnLocations().isEmpty()) ? game.getWorld().getSpawnLocation() :  mapConfiguration.getSpawnLocations().get(0).toWorldLocation(game.getWorld());
            spectator.teleport(spawnLocation);
            PlayerUtil.forEach(player -> {
                stats.getSidebar(player.getUniqueId()).updatePlayersLeft();
                if (global.getRole(player.getUniqueId()) == PlayerRole.PLAYER) {
                    global.addCoins(player.getUniqueId(), RainbowRun.COINS_PER_SURVIVAL);
                }
            });
        }
    }

    @EventHandler
    private void on(StateStartEvent e) {
        WorldSetting.PVP.setAllowed(true);
        armorTask = new RainbowArmorColourTask(global).runTaskTimer(getPlugin(), 0, 1);
        new GameStartTitleTask().run();
        PlayerUtil.forEach(player -> {
            var inventory = player.getInventory();
            inventory.setItem(EquipmentSlot.HEAD, ItemBuilder.ofType(Material.LEATHER_HELMET).build());
            inventory.setItem(EquipmentSlot.CHEST, ItemBuilder.ofType(Material.LEATHER_CHESTPLATE).build());
            inventory.setItem(EquipmentSlot.LEGS, ItemBuilder.ofType(Material.LEATHER_LEGGINGS).build());
            inventory.setItem(EquipmentSlot.FEET, ItemBuilder.ofType(Material.LEATHER_BOOTS).build());
            player.getInventory().setItem(0,
                    ItemBuilder
                            .ofType(Material.MUTTON)
                            .displayName(ChatColor.RED.toString() + ChatColor.BOLD + "Mutton Slapper")
                            .enchant(Enchantment.KNOCKBACK, 2)
                            .build());
            player.getInventory()
                    .addItem(ItemBuilder
                            .ofType(LEAP_ITEM_MATERIAL)
                            .displayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Leap " + ChatColor.GRAY + "(Right click)")
                            .build());
            stats.setSidebar(player.getUniqueId(), new RainbowSidebar(player, game).initialise());
        });
    }

    @EventHandler
    private void on(StateEndEvent e) {
        WorldSetting.PVP.setAllowed(false);
        armorTask.cancel();
        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> brokenBlocks.forEach((location, material) -> location.getBlock().setType(material)), 40);
    }
}
