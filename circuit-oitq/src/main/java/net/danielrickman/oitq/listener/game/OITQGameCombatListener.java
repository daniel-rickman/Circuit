package net.danielrickman.oitq.listener.game;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.map.MapLocation;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.util.ItemBuilder;
import net.danielrickman.api.role.PlayerRole;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.api.util.RandomUtil;
import net.danielrickman.oitq.OneInTheQuiver;
import net.danielrickman.oitq.configuration.OITQMapConfiguration;
import net.danielrickman.oitq.event.OITQPlayerRespawnEvent;
import net.danielrickman.oitq.repository.OITQRepository;
import net.danielrickman.oitq.task.OITQArrowParticleTask;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class OITQGameCombatListener extends CircuitListener {

    private final OneInTheQuiver oitq;
    private final GlobalRepository global;
    private final OITQRepository stats;

    public OITQGameCombatListener(CircuitPlugin plugin, OneInTheQuiver oitq, GlobalRepository global, OITQRepository stats) {
        super(plugin);
        this.oitq = oitq;
        this.global = global;
        this.stats = stats;
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
                    PlayerUtil.sendToAll(oitq.getChatPrefix() + ChatColor.GREEN + "%s" + ChatColor.WHITE + " killed " + ChatColor.RED + "%s" + ChatColor.WHITE + ".", attacker.getName(), victim.getName());
                } else {
                    PlayerUtil.sendToAll(oitq.getChatPrefix() + ChatColor.RED + "%s " + ChatColor.WHITE + "died.", victim.getName());
                }
                //Victim handling
                stats.removeLife(victim.getUniqueId());
                var lives = stats.getLives(victim.getUniqueId());
                if (lives == 0) {
                    global.setRole(victim.getUniqueId(), PlayerRole.SPECTATOR);
                } else {
                    Bukkit.getPluginManager().callEvent(new OITQPlayerRespawnEvent(victim));
                    PlayerUtil.sendMessage(victim, oitq.getChatPrefix() + "You now have " + ChatColor.YELLOW + "%s" + ChatColor.WHITE + " lives.", stats.getLives(victim.getUniqueId()));
                }
                PlayerUtil.forEach(player -> stats.updateRankings(player.getUniqueId()));
            }
        }
    }

    @EventHandler
    private void on(OITQPlayerRespawnEvent e) {
        OITQMapConfiguration mapConfiguration = oitq.getMapConfiguration();
        var mapLocation = (MapLocation) RandomUtil.randomFrom(mapConfiguration.getSpawnLocations().toArray());
        var player = e.getPlayer();
        if (!player.getInventory().contains(Material.ARROW)) {
            player.getInventory().addItem(ItemBuilder.ofType(Material.ARROW).build());
        }
        player.teleport(mapLocation.toWorldLocation(oitq.getWorld()));
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
}