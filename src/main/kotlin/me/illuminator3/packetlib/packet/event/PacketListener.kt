package me.illuminator3.packetlib.packet.event

import me.illuminator3.packetlib.packet.Packet

abstract class PacketListener
{
    private lateinit var lastPacket: Packet
    private lateinit var lastEvent: PacketEvent

    fun onPacket0(e: PacketEvent): Boolean
    {
        lastPacket = e.packet
        lastEvent = e

        onPacket(e)

        return e.isCancelled
    }

    abstract fun onPacket(e: PacketEvent)

    fun <T> field(name: String): T = lastPacket.getFieldValue(name)
    fun <T> method(name: String, vararg params: Any): T = lastPacket.invokeMethod(name, *params)

    fun cancel()
    {
        lastEvent.isCancelled = true
    }
}