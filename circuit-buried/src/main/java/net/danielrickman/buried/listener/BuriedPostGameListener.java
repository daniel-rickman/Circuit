package net.danielrickman.buried.listener;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.rank.Rank;
import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.buried.Buried;
import net.danielrickman.buried.repository.BuriedProfile;
import net.danielrickman.buried.repository.BuriedRepository;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

import java.util.*;

public class BuriedPostGameListener extends CircuitListener {

    private final BuriedRepository stats;

    public BuriedPostGameListener(CircuitPlugin plugin, BuriedRepository stats) {
        super(plugin);
        this.stats = stats;
    }

    @EventHandler
    private void on(StateStartEvent e) {
        PlayerUtil.forEach(player -> stats.getSidebar(player.getUniqueId()).destroy());
    }
}
