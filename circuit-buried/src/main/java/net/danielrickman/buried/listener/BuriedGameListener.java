package net.danielrickman.buried.listener;

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
import net.danielrickman.api.util.Timer;
import net.danielrickman.api.util.WorldSetting;
import net.danielrickman.buried.Buried;
import net.danielrickman.buried.repository.BuriedRepository;
import net.danielrickman.buried.sidebar.BuriedSidebar;
import net.danielrickman.buried.task.BuriedFallingBlockTask;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuriedGameListener extends CircuitListener {

    private final Buried game;
    private final Timer timer;
    private final GlobalRepository global;
    private final BuriedRepository stats;

    private final List<Location> fallenBlocks = new ArrayList<>();

    public BuriedGameListener(CircuitPlugin plugin, Buried game, Timer timer) {
        super(plugin);
        this.game = game;
        this.timer = timer;
        this.global = getPlugin().getGlobalRepository();
        this.stats = game.getStats();
    }

    @EventHandler
    private void on(BlockBreakEvent e) {
        e.setDropItems(false);
        if (e.getBlock().getType() == Material.GOLD_ORE) {
            var player = e.getPlayer();
            e.getBlock().setType(Material.COBBLESTONE);
            player.playSound(player.getEyeLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1.0f);
            player.sendTitle("", ChatColor.YELLOW + String.format("+%d points!", Buried.POINTS_PER_GOLD_BLOCK), 0, 20, 20);
            stats.addPoints(player.getUniqueId(), Buried.POINTS_PER_GOLD_BLOCK);
            stats.updateRankings(player.getUniqueId());
        }
    }

    /*
   Listens for player getting squashed by a falling block
    */
    @EventHandler
    private void on(EntityChangeBlockEvent e) {
        var blockLocation = e.getBlock().getLocation().toBlockLocation();
        PlayerUtil.getAlivePlayers(global).forEach(player -> {
            var playerLocation = player.getLocation().toBlockLocation();
            if (blockLocation.getBlockX() == playerLocation.getBlockX() && blockLocation.getBlockY() == playerLocation.getBlockY() && blockLocation.getBlockZ() == playerLocation.getBlockZ()) {
                global.setRole(player.getUniqueId(), PlayerRole.SPECTATOR);
            }
        });
        fallenBlocks.add(blockLocation);
    }

    @EventHandler
    private void on(EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.PLAYER) {
            if (e.getCause() != EntityDamageEvent.DamageCause.LAVA) {
                global.setRole(e.getEntity().getUniqueId(), PlayerRole.SPECTATOR);
            } else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void on(PlayerRoleChangeEvent e) {
        if (e.getNewRole() == PlayerRole.SPECTATOR) {
            PlayerUtil.sendToAll(game.getChatPrefix() + "All remaining players have received " + ChatColor.YELLOW + "%d" + ChatColor.WHITE + " points for surviving.", Buried.POINTS_PER_SURVIVAL);
            PlayerUtil.forEach(player -> {
                var uuid = player.getUniqueId();
                stats.getSidebar(uuid).updatePlayersLeft();
                stats.updateRankings(uuid);
                if (global.getRole(uuid) == PlayerRole.PLAYER) {
                    stats.addPoints(uuid, Buried.POINTS_PER_SURVIVAL);
                    global.addCoins(uuid, Buried.COINS_PER_SURVIVAL);
                }
            });
        }
    }

    @EventHandler
    private void on(StateEndEvent e) {
        WorldSetting.PVP.setAllowed(false);
        WorldSetting.PVE.setAllowed(false);
        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
                fallenBlocks.forEach(location -> location.getBlock().setType(Material.AIR));
        }, 40);
    }

    @EventHandler
    private void on(StateStartEvent e) {
        WorldSetting.PVP.setAllowed(true);
        WorldSetting.PVE.setAllowed(true);
        PlayerUtil.forEach(player -> {
            player.setGameMode(GameMode.SURVIVAL);
            game.getStats().setSidebar(player.getUniqueId(), new BuriedSidebar(player, game).initialise());
            player.getInventory().addItem(
                    ItemBuilder
                            .ofType(Material.IRON_PICKAXE)
                            .displayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Trusty Pickaxe")
                            .enchant(Enchantment.DIG_SPEED, 1)
                            .setUnbreakable()
                            .build()
            );
        });
        new GameStartTitleTask().run();
        new BuriedFallingBlockTask(getPlugin(), game.getWorld(), game.getMapConfiguration(), timer).runTask(getPlugin());
    }
}