package net.danielrickman.oitq.listener;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.map.MapLocation;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.role.PlayerRole;
import net.danielrickman.api.role.PlayerRoleChangeEvent;
import net.danielrickman.api.state.event.StateEndEvent;
import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.task.GameStartTitleTask;
import net.danielrickman.api.util.ItemBuilder;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.api.util.RandomUtil;
import net.danielrickman.api.util.WorldSetting;
import net.danielrickman.oitq.OneInTheQuiver;
import net.danielrickman.oitq.event.PlayerRespawnEvent;
import net.danielrickman.oitq.repository.OITQRepository;
import net.danielrickman.oitq.sidebar.OITQSidebar;
import net.danielrickman.oitq.task.OITQArrowParticleTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class OITQGameListener extends CircuitListener {

    private final OneInTheQuiver game;
    private final OITQRepository stats;
    private final GlobalRepository global;

    public OITQGameListener(CircuitPlugin plugin, OneInTheQuiver game, OITQRepository stats) {
        super(plugin);
        this.game = game;
        this.stats = stats;
        this.global = getPlugin().getGlobalRepository();
    }

    @EventHandler
    private void on(EntityDamageByEntityEvent e) {
        if (e.getEntityType() != EntityType.PLAYER) {
            return;
        }
        final Player victim = (Player) e.getEntity();
        if (victim.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            e.setCancelled(true);
        } else {
            var damageEntity = e.getDamager();
            Player attacker = null;
            if (damageEntity.getType() == EntityType.PLAYER) {
                attacker = ((Player) damageEntity);
            } else if (damageEntity.getType() == EntityType.ARROW) {
                var arrow = (Projectile) damageEntity;
                if (arrow.getShooter() instanceof Player) {
                    attacker = (Player) arrow.getShooter();
                }
                e.setDamage(Integer.MAX_VALUE);
            }
            if (victim.getHealth() - e.getFinalDamage() <= 0) {
                e.setCancelled(true);
                if (attacker != null) {
                    var uuid = attacker.getUniqueId();
                    attacker.setHealth(attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
                    stats.addPoints(uuid, OneInTheQuiver.POINTS_PER_KILL);
                    stats.addKill(uuid);
                    global.addCoins(uuid, OneInTheQuiver.COINS_PER_KILL);
                    attacker.sendTitle("", ChatColor.GREEN + "+1 Kill", 0, 20, 20);
                    attacker.playSound(attacker.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 0.5f, 0.5f);
                    if (!attacker.getInventory().contains(Material.ARROW)) {
                        attacker.getInventory().addItem(ItemBuilder.ofType(Material.ARROW).build());
                    }
                    PlayerUtil.sendToAll(game.getChatPrefix() + ChatColor.GREEN + "%s" + ChatColor.WHITE + " killed " + ChatColor.RED + "%s" + ChatColor.WHITE + ".", attacker.getName(), victim.getName());
                } else {
                    PlayerUtil.sendToAll(game.getChatPrefix() + ChatColor.RED + "%s " + ChatColor.WHITE + "died.", victim.getName());
                }
                //Victim handling
                stats.removeLife(victim.getUniqueId());
                var lives = stats.getLives(victim.getUniqueId());
                if (lives == 0) {
                    global.setRole(victim.getUniqueId(), PlayerRole.SPECTATOR);
                } else {
                    Bukkit.getPluginManager().callEvent(new PlayerRespawnEvent(victim));
                    PlayerUtil.sendMessage(victim, game.getChatPrefix() + "You now have " + ChatColor.YELLOW + "%s" + ChatColor.WHITE + " lives.", stats.getLives(victim.getUniqueId()));
                }
                PlayerUtil.forEach(player -> stats.updateRankings(player.getUniqueId()));
            }
        }
    }

    @EventHandler
    private void on(PlayerRespawnEvent e) {
        var mapLocation = (MapLocation) RandomUtil.randomFrom(game.getMapConfiguration().getSpawnLocations().toArray());
        var player = e.getPlayer();
        if (!player.getInventory().contains(Material.ARROW)) {
            player.getInventory().addItem(ItemBuilder.ofType(Material.ARROW).build());
        }
        player.teleport(mapLocation.toWorldLocation(game.getWorld()));
        player.setHealth(20.0);
        player.addPotionEffects(List.of(
                new PotionEffect(PotionEffectType.BLINDNESS, 30, 1, false, false),
                new PotionEffect(PotionEffectType.INVISIBILITY, 30, 1, false, false)
        ));
        player.sendTitle("", ChatColor.GOLD + "\u2620 Respawned! \u2620", 0, 20, 20);
        player.playSound(player.getEyeLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 0.5f, 1.0f);
    }

    @EventHandler
    private void on(ProjectileLaunchEvent e) {
        new OITQArrowParticleTask(e.getEntity()).runTaskTimer(getPlugin(), 0, 2);
    }

    @EventHandler
    private void on(StateStartEvent e) {
        WorldSetting.PVP.setAllowed(true);
        PlayerUtil.forEach(player -> {
            game.getStats().setSidebar(player.getUniqueId(), new OITQSidebar(player, game).initialise());
            player.getInventory().addItem(
                    ItemBuilder.ofType(Material.STONE_SWORD).setUnbreakable().build(),
                    ItemBuilder.ofType(Material.BOW).setUnbreakable().build(),
                    ItemBuilder.ofType(Material.ARROW).build()
            );
        });
        new GameStartTitleTask().run();
    }

    @EventHandler
    private void on(StateEndEvent e) {
        WorldSetting.PVP.setAllowed(false);
    }

    @EventHandler
    private void on(PlayerRoleChangeEvent e) {
        if (e.getNewRole() == PlayerRole.SPECTATOR) {
            PlayerUtil.sendToAll(game.getChatPrefix() + "All remaining players have received " + ChatColor.YELLOW + "%d" + ChatColor.WHITE + " points for surviving.", OneInTheQuiver.POINTS_PER_SURVIVAL);
            PlayerUtil.forEach(player -> {
                stats.getSidebar(player.getUniqueId()).updatePlayersLeft();
                if (global.getRole(player.getUniqueId()) == PlayerRole.PLAYER) {
                    stats.addPoints(player.getUniqueId(), OneInTheQuiver.POINTS_PER_SURVIVAL);
                    global.addCoins(player.getUniqueId(), OneInTheQuiver.COINS_PER_SURVIVAL);
                }
            });
        }
    }
    @EventHandler
    public void on(ProjectileHitEvent e) {
        e.getEntity().remove();
    }
}
