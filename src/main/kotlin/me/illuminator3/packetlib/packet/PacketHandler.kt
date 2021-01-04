package me.illuminator3.packetlib.packet

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import me.illuminator3.packetlib.extensions.thenElse
import me.illuminator3.packetlib.utils.Reflection
import org.bukkit.entity.Player
import kotlin.jvm.Throws

abstract class PacketHandler constructor (private val player: Player) : ChannelDuplexHandler()
{
    private val nmsPacket = Reflection.getClass("{nms}.Packet")

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, o: Any)
    {
        var cancelled = false

        if (nmsPacket.isAssignableFrom(o::class.java))
            cancelled = channelRead(player, Packet(o))

        cancelled.thenElse { super.channelRead(ctx, o) }
    }

    abstract fun channelRead(player: Player, packet: Packet): Boolean /* if the packet should be "cancelled" */
}