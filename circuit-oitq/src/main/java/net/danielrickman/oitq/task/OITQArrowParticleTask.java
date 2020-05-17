package net.danielrickman.oitq.task;

import com.destroystokyo.paper.ParticleBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class OITQArrowParticleTask extends BukkitRunnable {

    private final Projectile projectile;

    @Override
    public void run() {
        if (projectile.isDead()) {
            cancel();
            return;
        }
        new ParticleBuilder(Particle.REDSTONE)
                .location(projectile.getLocation())
                .color(Color.RED)
                .count(1)
                .receivers(20)
                .spawn(); //Show to all players in radius of 20
    }
}
