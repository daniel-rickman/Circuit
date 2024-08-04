package dev.dancr.circuit.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

open class CircuitEvent : Event() {

    companion object {
        public val handlers: HandlerList = HandlerList()

        @JvmStatic public fun getHandlerList(): HandlerList = handlers
    }

    override fun getHandlers(): HandlerList = CircuitEvent.handlers
}