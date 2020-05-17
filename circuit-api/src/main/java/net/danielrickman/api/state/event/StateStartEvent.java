package net.danielrickman.api.state.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class StateStartEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
