package dev.dancr.nexus.util

import org.bukkit.Sound
import org.bukkit.entity.Player

fun Player.playSoundAtHead(sound: Sound) = playSound(eyeLocation, sound, 1.0f, 1.0f)