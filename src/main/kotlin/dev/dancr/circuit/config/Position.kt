package dev.dancr.circuit.config

import org.bukkit.Location
import org.bukkit.World

data class Position(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float = 0.0f,
    val pitch: Float = 0.0f
) {


    //todo Not sure this is necessary
    public fun matches(location: Location) : Boolean = x == location.x && y == location.y && z == location.z && yaw == location.yaw && pitch == location.pitch

    public fun toBukkitLocation(world: World) : Location = Location(world, x, y, z, yaw, pitch)

}