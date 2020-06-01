package net.danielrickman.api.task;

import lombok.AllArgsConstructor;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.api.util.Timer;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

@AllArgsConstructor
public class PreGameCountdownTask implements Runnable {

    private final Timer timer;

    @Override
    public void run() {
        var remaining = timer.getTimeLeft();
        if (remaining < 3) {
            PlayerUtil.forEach(player -> {
                player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 0.5f, 0f);
                player.sendTitle(ChatColor.YELLOW.toString() + ChatColor.BOLD + (remaining + 1), "Game starting in...", 0, 20, 0);
            });
        }
    }
}