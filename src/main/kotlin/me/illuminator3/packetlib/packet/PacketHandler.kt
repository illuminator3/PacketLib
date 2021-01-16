/*
   Copyright 2021 illuminator3

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

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