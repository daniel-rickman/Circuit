package net.danielrickman.bukkit.listener;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.util.CircuitPrefix;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.bukkit.Circuit;
import net.danielrickman.bukkit.inventory.OperatorInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class MapListener extends CircuitListener {

    private final Circuit circuit;

    public MapListener(Circuit circuit) {
        super(circuit);
        this.circuit = circuit;
    }

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (OperatorInventory.RELOAD_MAPS_ITEM.matches(e.getItem())) {
            PlayerUtil.sendToAll(CircuitPrefix.ADMIN.getPrefix() + "Choosing new maps. There will be a slight lag spike as the maps are loaded in.");
            circuit.getMapLoader().loadGameMaps();
        }
    }
}