package me.illuminator3.packetlib.packet.event

import me.illuminator3.packetlib.packet.Packet
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable

data class PacketEvent constructor (val player: Player, val packet: Packet) : Cancellable
{
    private var cancelled = false

    override fun isCancelled(): Boolean = cancelled

    override fun setCancelled(bool: Boolean)
    {
        cancelled = bool
    }
}