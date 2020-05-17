package net.danielrickman.api.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
public class PlayerRoleChangeEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @Getter
    private final Player player;
    @Getter
    private final PlayerRole newRole;

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}