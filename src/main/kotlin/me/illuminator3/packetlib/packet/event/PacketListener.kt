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

package me.illuminator3.packetlib.packet.event

import me.illuminator3.packetlib.packet.Packet

/**
 * @author illuminator3
 */
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