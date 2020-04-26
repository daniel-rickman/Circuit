package net.danielrickman.api.util;

import lombok.Getter;
import net.danielrickman.api.plugin.CircuitPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class Timer {

    private final CircuitPlugin plugin;
    @Getter
    private final int duration;
    private final Runnable onTimerTick;
    private final Runnable onStop;
    @Getter
    private BukkitTask task;
    @Getter
    private int elapsed = 0;

    public Timer(CircuitPlugin plugin, int duration, Runnable onTimerTick, Runnable onStop) {
        this.plugin = plugin;
        this.duration = duration;
        this.onTimerTick = onTimerTick;
        this.onStop = onStop;
    }

    public void start()
    {
        task = Bukkit.getScheduler().runTaskTimer(plugin, this::onTick, 0, 20);
    }

    public void onTick() {
        elapsed++;
        onTimerTick.run();
        if (elapsed >= duration) {
            stop();
        }
    }

    public void stop() {
        onStop.run();
        task.cancel();
    }
}