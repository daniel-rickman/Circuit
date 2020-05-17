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
    private int timeLeft;

    public Timer(CircuitPlugin plugin, int duration, Runnable onTimerTick, Runnable onStop) {
        this.plugin = plugin;
        this.duration = duration;
        this.onTimerTick = onTimerTick;
        this.onStop = onStop;
        this.timeLeft = duration;
    }

    public void start() {
        task = Bukkit.getScheduler().runTaskTimer(plugin, this::onTick, 0, 20);
    }

    public void onTick() {
        if (timeLeft > 0) {
            timeLeft--;
            onTimerTick.run();
        } else if (timeLeft == 0) {
            onStop.run();
        }
    }

    public void stop() {
        task.cancel();
    }

    public String getFormattedTimer() {
        var seconds = getTimeLeft() % 60;
        return (getTimeLeft() / 60) + ":" + ((seconds < 10) ? "0" : "") + seconds;
    }
}