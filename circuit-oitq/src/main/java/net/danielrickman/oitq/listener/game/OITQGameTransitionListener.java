package net.danielrickman.oitq.listener.game;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.state.event.StateEndEvent;
import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.util.ItemBuilder;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.api.util.WorldSetting;
import net.danielrickman.oitq.OneInTheQuiver;
import net.danielrickman.oitq.sidebar.OITQPlayerSidebar;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

public class OITQGameTransitionListener extends CircuitListener {

    private final OneInTheQuiver oitq;
    private final GlobalRepository global;

    public OITQGameTransitionListener(CircuitPlugin plugin, OneInTheQuiver oitq, GlobalRepository global) {
        super(plugin);
        this.oitq = oitq;
        this.global = global;
    }

    @EventHandler
    private void on(StateStartEvent e) {
        WorldSetting.PVP.setAllowed(true);
        PlayerUtil.forEach(player -> {
            player.sendTitle(ChatColor.GREEN.toString() + ChatColor.BOLD + "START", "", 10, 20, 10);
            player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0f, 1.0f);
            oitq.getStats().setSidebar(player.getUniqueId(), new OITQPlayerSidebar(player, oitq, global).initialise());
            player.getInventory().addItem(
                    ItemBuilder.ofType(Material.STONE_SWORD).setUnbreakable().build(),
                    ItemBuilder.ofType(Material.BOW).setUnbreakable().build(),
                    ItemBuilder.ofType(Material.ARROW).build()
            );
        });
    }

    @EventHandler
    private void on(StateEndEvent e) {
        WorldSetting.PVP.setAllowed(false);
    }
}
