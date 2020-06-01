package net.danielrickman.rainbowrun.task;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

@RequiredArgsConstructor
public class RainbowBlockBreakTask extends BukkitRunnable {

    private static final List<Material> BLOCK_SEQUENCE = List.of(Material.RED_WOOL, Material.ORANGE_WOOL, Material.YELLOW_WOOL, Material.GREEN_WOOL, Material.BLUE_WOOL, Material.MAGENTA_WOOL, Material.PURPLE_WOOL);

    private final Block block;
    private int i = 0;

    @Override
    public void run() {
        if (i == BLOCK_SEQUENCE.size()) {
            block.setType(Material.AIR);
            cancel();
        } else {
            block.setType(BLOCK_SEQUENCE.get(i));
            i++;
        }
    }
}
