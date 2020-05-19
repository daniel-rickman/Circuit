package net.danielrickman.rainbowrun.task;

import lombok.RequiredArgsConstructor;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.util.PlayerUtil;
import org.bukkit.Color;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

@RequiredArgsConstructor
public class RRArmorColourTask extends BukkitRunnable {

    private final GlobalRepository global;
    private int i = 0;

    @Override
    public void run() {
        final double frequency = 0.3;
        if (i > 360) {
            i = 0;
        }
        int r = (int) (Math.sin(frequency * i + 0) * 127 + 128);
        int g = (int) (Math.sin(frequency * i + 2 * Math.PI / 3) * 127 + 128);
        int b = (int) (Math.sin(frequency * i + 4 * Math.PI / 3) * 127 + 128);

        PlayerUtil.getAlivePlayers(global).forEach(player -> Arrays.stream(player.getInventory().getArmorContents()).forEach(armor -> {
            if (armor != null && armor.hasItemMeta() && armor.getItemMeta() instanceof LeatherArmorMeta) {
                LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
                meta.setColor(Color.fromRGB(r, g, b));
                armor.setItemMeta(meta);
            }
        }));
        i++;
    }
}