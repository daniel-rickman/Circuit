package net.danielrickman.rainbowrun.listener.postgame;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.rank.Rank;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.rainbowrun.RainbowRun;
import net.danielrickman.rainbowrun.repository.RRRepository;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

public class RRPostGameTransitionListener extends CircuitListener {

    private final GlobalRepository global;
    private final RainbowRun rr;
    private final RRRepository stats;

    public RRPostGameTransitionListener(CircuitPlugin plugin, GlobalRepository global, RainbowRun rr, RRRepository stats) {
        super(plugin);
        this.global = global;
        this.rr = rr;
        this.stats = stats;
    }

    @EventHandler
    private void on(StateStartEvent e) {
        var winners = PlayerUtil.getAlivePlayers(global);
        StringBuilder formattedWinnerList = new StringBuilder();
        if (winners.size() > 0) {
            winners.forEach(player -> {
                formattedWinnerList
                        .append(Rank.getFormattedName(Bukkit.getPlayer(player.getUniqueId())))
                        .append((winners.size() > winners.indexOf(player) + 1) ? ChatColor.WHITE + "," : "");
            });
        } else {
            formattedWinnerList.append(ChatColor.RED + "Nobody!");
        }
        PlayerUtil.forEach(player -> {
            player.playSound(player.getEyeLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.5f, 1.0f);
            PlayerUtil.sendMessage(player, rr.getChatPrefix() + ChatColor.GREEN + "Game Over!");
            PlayerUtil.sendMessage(player, rr.getChatPrefix() + "The winner" + ((winners.size() > 1) ? "s are: " : " is: %s"), formattedWinnerList);
            global.addCoins(player.getUniqueId(), 20);
            PlayerUtil.sendMessage(player, rr.getChatPrefix() + ChatColor.GRAY + ChatColor.ITALIC + "You will return to the lobby shortly...");
            stats.getSidebar(player.getUniqueId()).destroy();
        });
    }
}
