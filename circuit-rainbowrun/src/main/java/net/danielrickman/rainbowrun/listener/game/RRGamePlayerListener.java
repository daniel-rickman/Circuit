package net.danielrickman.rainbowrun.listener.game;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.role.PlayerRole;
import net.danielrickman.api.role.PlayerRoleChangeEvent;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.rainbowrun.configuration.RRMapConfiguration;
import net.danielrickman.rainbowrun.RainbowRun;
import net.danielrickman.rainbowrun.repository.RRRepository;
import net.danielrickman.rainbowrun.task.RRBlockBreakTask;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class RRGamePlayerListener extends CircuitListener {

    private final static List<Material> BREAKABLE_MATERIALS = List.of(Material.WHITE_WOOL, Material.GLOWSTONE);

    private final RainbowRun rr;
    private final GlobalRepository global;
    private final RRRepository stats;

    public RRGamePlayerListener(CircuitPlugin plugin, RainbowRun rr, GlobalRepository global, RRRepository stats) {
        super(plugin);
        this.rr = rr;
        this.global = global;
        this.stats = stats;
    }

    @EventHandler
    private void on(PlayerLoginEvent e) {
        e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server is not joinable at this time");
    }

    @EventHandler
    private void on(EntityDamageByEntityEvent e) {
        e.setDamage(0);
    }

    @EventHandler
    private void on(PlayerMoveEvent e) {
        RRMapConfiguration mapConfiguration = rr.getMapConfiguration();
        var player = e.getPlayer();
        if (player.getLocation().getY() <= mapConfiguration.getDeathY()) {
            global.setRole(player.getUniqueId(), PlayerRole.SPECTATOR);
        } else {
            if (mapConfiguration.getRegion().containsEntity(player)) {
                var block = rr.getWorld().getBlockAt(player.getLocation().subtract(0, 1, 0));
                if (BREAKABLE_MATERIALS.contains(block.getType())) {
                    var uuid = player.getUniqueId();
                    new RRBlockBreakTask(block).runTaskTimer(rr.getPlugin(), 0, 3);
                    stats.incrementBlocksDestroyed(uuid);
                    player.setLevel(stats.getBlocksDestroyed(uuid));
                }
            }
        }
    }

    @EventHandler
    private void on(PlayerInteractEvent e) {
        var item = e.getItem();
        var player = e.getPlayer();
        if (item != null && item.getType() == Material.FEATHER) {
            player.setVelocity(new Vector(0, 2, 0));
            player.getInventory().remove(item);
        }
    }

    @EventHandler
    private void on(PlayerRoleChangeEvent e) {
        var spectator = e.getPlayer();
        if (e.getNewRole() == PlayerRole.SPECTATOR) {
            RRMapConfiguration mapConfiguration = rr.getMapConfiguration();
            var aliveCount = PlayerUtil.getAlivePlayers(global).size();

            spectator.setGameMode(GameMode.SPECTATOR);
            spectator.teleport(mapConfiguration.getSpawnLocation().toWorldLocation(rr.getWorld()));
            spectator.sendTitle("", ChatColor.RED + "\u2620 Eliminated! \u2620", 0, 40, 20);
            spectator.playSound(spectator.getEyeLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 0.5f, 1.0f);
            PlayerUtil.sendToAll(rr.getChatPrefix() + ChatColor.RED + "%s " + ChatColor.WHITE + "has been eliminated! " + ChatColor.GRAY + "[%d remaining]", e.getPlayer().getName(), aliveCount);
            PlayerUtil.forEach(player -> {
                stats.getSidebar(player.getUniqueId()).updatePlayersLeft(aliveCount);
                if (global.getRole(player.getUniqueId()) == PlayerRole.PLAYER) {
                    global.addCoins(player.getUniqueId(), RainbowRun.COINS_PER_SURVIVAL);
                }
            });
        }
    }

    @EventHandler
    private void on(InventoryOpenEvent e) {
        e.setCancelled(true);
    }
}
