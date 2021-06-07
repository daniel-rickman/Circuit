package dev.dancr.nexus.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

open class NexusEvent : Event() {

    companion object {
        public val handlers: HandlerList = HandlerList()

        @JvmStatic public fun getHandlerList(): HandlerList = handlers
    }

    override fun getHandlers(): HandlerList = NexusEvent.handlers

}