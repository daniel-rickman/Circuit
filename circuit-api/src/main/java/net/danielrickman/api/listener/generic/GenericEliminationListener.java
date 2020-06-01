package net.danielrickman.api.listener.generic;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitGame;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.role.PlayerRole;
import net.danielrickman.api.role.PlayerRoleChangeEvent;
import net.danielrickman.api.util.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

public class GenericEliminationListener extends CircuitListener {

    private final CircuitGame game;

    public GenericEliminationListener(CircuitPlugin plugin, CircuitGame game) {
        super(plugin);
        this.game = game;
    }

    @EventHandler
    private void on(PlayerRoleChangeEvent e) {
        var spectator = e.getPlayer();
        if (e.getNewRole() == PlayerRole.SPECTATOR) {
            var aliveCount = PlayerUtil.getAlivePlayers(getPlugin().getGlobalRepository()).size();
            spectator.setGameMode(GameMode.SPECTATOR);
            spectator.sendTitle("", ChatColor.RED + "\u2620 Eliminated! \u2620", 0, 20, 20);
            spectator.playSound(spectator.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 1.0f);
            PlayerUtil.sendToAll(game.getChatPrefix() + ChatColor.RED + "%s " + ChatColor.WHITE + "has been eliminated! " + ChatColor.GRAY + "[%d remaining]", e.getPlayer().getName(), aliveCount);
        }
    }
}
