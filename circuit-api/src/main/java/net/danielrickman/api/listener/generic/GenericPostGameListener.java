package net.danielrickman.api.listener.generic;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitGame;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.plugin.GameObjective;
import net.danielrickman.api.rank.Rank;
import net.danielrickman.api.repository.Repository;
import net.danielrickman.api.repository.profile.GameProfile;
import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.util.CollectionUtil;
import net.danielrickman.api.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.*;

public class GenericPostGameListener extends CircuitListener {

    private final CircuitGame game;
    private final Repository<? extends GameProfile> stats;

    public GenericPostGameListener(CircuitPlugin plugin, CircuitGame game, Repository<? extends GameProfile> stats) {
        super(plugin);
        this.game = game;
        this.stats = stats;
    }

    @EventHandler
    private void on(StateStartEvent e) {
        List<Player> winners = new ArrayList<>();
        if (game.getGameObjective() == GameObjective.LAST_MAN_STANDING) {
            winners = PlayerUtil.getAlivePlayers(getPlugin().getGlobalRepository());
        } else {
            HashMap<UUID, Integer> pointsMap = new HashMap<>();
            stats.getPlayerMap().forEach((uuid, profile) -> pointsMap.put(uuid, profile.getPoints()));
            for (Map.Entry<UUID, Integer> entry : CollectionUtil.getTopEntriesByValue(pointsMap)) {
                var player = Bukkit.getPlayer(entry.getKey());
                if (player != null) {{
                    winners.add(Bukkit.getPlayer(entry.getKey()));
                }}
            }
        }
        StringBuilder formattedWinnerList = new StringBuilder();
        if (winners.size() > 0) {
            for (Player winner : winners) {
                formattedWinnerList
                        .append(Rank.getFormattedName(winner))
                        .append((winners.size() > winners.indexOf(winner) + 1) ? ChatColor.WHITE + "," : "");
            }
        } else {
            formattedWinnerList.append(ChatColor.RED + "Nobody!");
        }
        final var winnersCount = winners.size();
        PlayerUtil.forEach(player -> {
            player.playSound(player.getEyeLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.5f, 1.0f);
            PlayerUtil.sendMessage(player, game.getChatPrefix() + ChatColor.GREEN + "Game Over!");
            PlayerUtil.sendMessage(player, game.getChatPrefix() + "The winner" + ((winnersCount > 1) ? "s are: " : " is: %s"), formattedWinnerList);
            PlayerUtil.sendMessage(player, game.getChatPrefix() + ChatColor.GRAY + ChatColor.ITALIC + "You will return to the lobby shortly...");
        });
    }
}
