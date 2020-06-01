package net.danielrickman.buried.task;

import lombok.RequiredArgsConstructor;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.util.RandomUtil;
import net.danielrickman.api.util.Timer;
import net.danielrickman.buried.Buried;
import net.danielrickman.buried.BuriedMapConfiguration;
import net.danielrickman.buried.state.BuriedGameState;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class BuriedFallingBlockTask extends BukkitRunnable {

    private final static List<Material> BLOCK_TYPES = Arrays.asList(Material.SAND, Material.GRAVEL, Material.GOLD_ORE, Material.MAGMA_BLOCK);
    private final static int BLOCKS_DROPPED_PER_TASK = 20;

    private final CircuitPlugin plugin;
    private final World world;
    private final BuriedMapConfiguration mapConfiguration;
    private final Timer timer;
    private int speed = Buried.STARTING_BLOCK_FALL_RATE; //In ticks

    @Override
    public void run() {
        if (timer.getTimeLeft() == 0 || !(plugin.getCurrentState() instanceof BuriedGameState)) {
            cancel();
            return;
        }
        if (speed > 1 && timer.getTimeLeft() % 30 == 0) { //Every 30 seconds
            speed -= 1; //Speed up falling by 5 ticks
        }
        for (int i = 0; i < BLOCKS_DROPPED_PER_TASK; i++) {
            var location = RandomUtil.randomFrom(mapConfiguration.getDropRegion().getAllLocations(world));
            do {
                location = location.add(0, 1, 0);
            } while (world.getBlockAt(location).getType() != Material.AIR);
            FallingBlock fallingBlock = world.spawnFallingBlock(location.toBlockLocation(), RandomUtil.randomFrom(BLOCK_TYPES).createBlockData());
            fallingBlock.setDropItem(false);
        }

        new BuriedFallingBlockTask(plugin, world, mapConfiguration, timer).runTaskLater(plugin, speed);
    }
}