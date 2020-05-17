package net.danielrickman.oitq.listener.postgame;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.rank.Rank;
import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.oitq.OneInTheQuiver;
import net.danielrickman.oitq.repository.OITQProfile;
import net.danielrickman.oitq.repository.OITQRepository;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

import java.util.*;

public class OITQPostGameTransitionListener extends CircuitListener {

    private final OneInTheQuiver oitq;
    private final OITQRepository stats;

    public OITQPostGameTransitionListener(CircuitPlugin plugin, OneInTheQuiver oitq, OITQRepository stats) {
        super(plugin);
        this.oitq = oitq;
        this.stats = stats;
    }

    @EventHandler
    private void on(StateStartEvent e) {
        var winners = getWinners();
        StringBuilder formattedWinnerList = new StringBuilder();
        if (winners.size() > 0) {
            winners.forEach(entry -> {
                formattedWinnerList
                        .append(Rank.getFormattedName(Bukkit.getPlayer(entry.getKey())))
                        .append((winners.size() > winners.indexOf(entry) + 1) ? ChatColor.WHITE + "," : "");
            });
        } else {
            formattedWinnerList.append(ChatColor.RED + "Nobody!");
        }
        PlayerUtil.forEach(player -> {
            player.playSound(player.getEyeLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.5f, 1.0f);
            PlayerUtil.sendMessage(player, oitq.getChatPrefix() + ChatColor.GREEN + "Game Over!");
            PlayerUtil.sendMessage(player, oitq.getChatPrefix() + "The winner" + ((winners.size() > 1) ? "s are: " : " is: %s"), formattedWinnerList);
            PlayerUtil.sendMessage(player, oitq.getChatPrefix() + ChatColor.GRAY + ChatColor.ITALIC + "You will return to the lobby shortly...");
            stats.getSidebar(player.getUniqueId()).destroy();
        });
    }

    private List<Map.Entry<UUID, OITQProfile>> getWinners() {
        List<Map.Entry<UUID, OITQProfile>> winners = new ArrayList<>();
        stats
                .getPlayerMap()
                .entrySet()
                .stream()
                .sorted(Comparator.comparingInt(entry -> entry.getValue().getPoints()))
                .forEach(entry -> {
                    if (winners.isEmpty()) {
                        winners.add(entry);
                    } else {
                        final var currentWinnerPoints = winners.get(0).getValue().getPoints();
                        final var potentialWinnerPoints = entry.getValue().getPoints();
                        if (currentWinnerPoints <= potentialWinnerPoints) {
                            if (currentWinnerPoints < potentialWinnerPoints) {
                                winners.clear();
                            }
                            winners.add(entry);
                        }
                    }
                });
        return winners;
    }
}