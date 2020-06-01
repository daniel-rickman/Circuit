package net.danielrickman.api.task;

import net.danielrickman.api.util.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class GameStartTitleTask implements Runnable {

    @Override
    public void run() {
        PlayerUtil.forEach(player -> {
            player.sendTitle(ChatColor.GREEN.toString() + ChatColor.BOLD + "START", "", 10, 20, 10);
            player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0f, 1.0f);
        });
    }
}
