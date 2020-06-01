package net.danielrickman.api.listener;

import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.rank.Rank;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.repository.profile.GlobalProfile;
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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class GlobalListener extends CircuitListener {

    private final GlobalRepository global;

    public GlobalListener(CircuitPlugin plugin, GlobalRepository global) {
        super(plugin);
        this.global = global;
    }

    @EventHandler(priority = EventPriority.LOW)
    private void on(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        global.add(e.getPlayer(), new GlobalProfile());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void on(AsyncPlayerChatEvent e) {
        e.setCancelled(!WorldSetting.CHAT.isAllowed());
        e.setFormat(Rank.get(e.getPlayer()).getNameColor() + "%s" + ChatColor.WHITE + ": %s");
    }

    @EventHandler(priority = EventPriority.LOW)
    private void on(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        global.remove(e.getPlayer());
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