package net.danielrickman.oitq.listener.game;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.role.PlayerRole;
import net.danielrickman.api.role.PlayerRoleChangeEvent;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.oitq.OneInTheQuiver;
import net.danielrickman.oitq.repository.OITQRepository;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;

public class OITQGamePlayerListener extends CircuitListener {

    private final OneInTheQuiver oitq;
    private final GlobalRepository global;
    private final OITQRepository stats;

    public OITQGamePlayerListener(CircuitPlugin plugin, OneInTheQuiver oitq, GlobalRepository global, OITQRepository stats) {
        super(plugin);
        this.oitq = oitq;
        this.global = global;
        this.stats = stats;
    }

    @EventHandler
    private void on(PlayerLoginEvent e) {
        e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server is not joinable at this time");
    }

    @EventHandler
    private void on(PlayerRoleChangeEvent e) {
        var spectator = e.getPlayer();
        if (e.getNewRole() == PlayerRole.SPECTATOR) {
            var aliveCount = PlayerUtil.getAlivePlayers(global).size();
            spectator.setGameMode(GameMode.SPECTATOR);
            spectator.sendTitle("", ChatColor.RED + "\u2620 Eliminated! \u2620", 0, 20, 20);
            spectator.playSound(spectator.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 1.0f);
            PlayerUtil.sendToAll(oitq.getChatPrefix() + ChatColor.RED + "%s " + ChatColor.WHITE + "has been eliminated! " + ChatColor.GRAY + "[%d remaining]", e.getPlayer().getName(), aliveCount);
            PlayerUtil.sendToAll(oitq.getChatPrefix() + "All remaining players have received " + ChatColor.YELLOW + "%d" + ChatColor.WHITE + " points for surviving.", OneInTheQuiver.POINTS_PER_SURVIVAL);
            PlayerUtil.forEach(player -> {
                stats.getSidebar(player.getUniqueId()).updatePlayersLeft(aliveCount);
                if (global.getRole(player.getUniqueId()) == PlayerRole.PLAYER) {
                    stats.addPoints(player.getUniqueId(), OneInTheQuiver.POINTS_PER_SURVIVAL);
                    global.addCoins(player.getUniqueId(), OneInTheQuiver.COINS_PER_SURVIVAL);
                }
            });
        }
    }
}
