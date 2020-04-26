package net.danielrickman.bukkit.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OperatorAttemptStartEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}