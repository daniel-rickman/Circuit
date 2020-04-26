package net.danielrickman.api.listener;

import net.danielrickman.api.player.PlayerRepository;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.util.WorldSetting;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;

public class CoreListener extends CircuitListener {

    private final PlayerRepository players;

    public CoreListener(CircuitPlugin plugin, PlayerRepository players) {
        super(plugin);
        this.players = players;
    }

    @EventHandler(priority = EventPriority.LOW)
    private void on(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        players.add(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void on(AsyncPlayerChatEvent e) {
        e.setCancelled(!WorldSetting.CHAT.isAllowed());
        players.getPlayer(e.getPlayer())
                .ifPresentOrElse(
                        (cp) -> e.setFormat(cp.getRank().getNameColor() + "%s" + ChatColor.WHITE + ": %s"),
                        () -> e.setCancelled(true));
    }

    @EventHandler(priority = EventPriority.LOW)
    private void on(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        players.remove(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    private void on(BlockPlaceEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(!WorldSetting.PLACE.isAllowed());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    private void on(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(!WorldSetting.BREAK.isAllowed());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    private void on(EntityDamageEvent e) {
        if (e instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) e).getDamager().getType() == EntityType.PLAYER) {
                e.setCancelled(!WorldSetting.PVP.isAllowed());
                return;
            }
        }
        e.setCancelled(!WorldSetting.PVE.isAllowed());
    }

    @EventHandler(priority = EventPriority.LOW)
    private void on(FoodLevelChangeEvent e) {
        e.setCancelled(!WorldSetting.HUNGER.isAllowed());
    }

    @EventHandler(priority = EventPriority.LOW)
    private void on(WeatherChangeEvent e) {
        e.setCancelled(!WorldSetting.WEATHER.isAllowed());
    }

    @EventHandler(priority = EventPriority.LOW)
    private void on(PlayerDropItemEvent e) {
        e.setCancelled(!WorldSetting.DROP.isAllowed());
    }
}