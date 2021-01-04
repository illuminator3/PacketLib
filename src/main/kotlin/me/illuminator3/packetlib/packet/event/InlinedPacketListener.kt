package me.illuminator3.packetlib.packet.event

@FunctionalInterface
interface InlinedPacketListener
{
    fun onPacket(e: PacketEvent)
}