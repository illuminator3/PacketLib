package me.illuminator3.packetlib.extensions

import me.illuminator3.packetlib.utils.Reflection
import org.bukkit.entity.Player

fun Player.asNMS(): Any /* NMS player */ = Reflection.getNMSPlayer(this)