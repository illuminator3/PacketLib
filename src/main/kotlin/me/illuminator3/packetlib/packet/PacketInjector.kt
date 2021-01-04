package me.illuminator3.packetlib.packet

import me.illuminator3.packetlib.core.PacketLib
import org.bukkit.entity.Player

interface PacketInjector
{
    fun inject(lib: PacketLib, player: Player)
    fun uninject(lib: PacketLib, player: Player)
}