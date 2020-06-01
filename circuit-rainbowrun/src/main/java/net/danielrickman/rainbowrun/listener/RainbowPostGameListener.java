package net.danielrickman.rainbowrun.listener;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.api.util.WorldSetting;
import net.danielrickman.rainbowrun.RainbowRun;
import org.bukkit.event.EventHandler;

public class RainbowPostGameListener extends CircuitListener {

    private final RainbowRun game;
    private final GlobalRepository global;

    public RainbowPostGameListener(CircuitPlugin plugin, RainbowRun game) {
        super(plugin);
        this.game = game;
        this.global = getPlugin().getGlobalRepository();
    }

    @EventHandler
    private void on(StateStartEvent e) {
        WorldSetting.PVP.setAllowed(false);
        PlayerUtil.forEach(player -> {
            if (PlayerUtil.getAlivePlayers(global).contains(player)) {
                global.addCoins(player.getUniqueId(), 20);
            }
            game.getStats().getSidebar(player.getUniqueId()).destroy();
        });
    }
}
