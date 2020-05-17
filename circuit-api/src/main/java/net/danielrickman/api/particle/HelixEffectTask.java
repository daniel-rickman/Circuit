package net.danielrickman.api.particle;

import com.destroystokyo.paper.ParticleBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class HelixEffectTask implements Runnable {

    private final Player player;
    private final Location location;
    private final int radius;
    private final Particle particle;
    private final Color color; //Ignored for some particle effects

    private double t = 0;

    @Override
    public void run() {
        t = t + Math.PI / 8;
        var x = radius * Math.cos(t);
        var y = t / 5;
        var z = radius * Math.sin(t);
        location.add(x, y, z);
        new ParticleBuilder(particle).location(location).color(color).receivers(player).spawn();
        location.subtract(x, y, z);
        if (t > 15) {
            t = 0;
        }
    }
}
