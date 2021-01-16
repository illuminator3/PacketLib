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

package me.illuminator3.packetlib.core

import io.netty.channel.Channel
import me.illuminator3.packetlib.extensions.*
import me.illuminator3.packetlib.plugin.PluginInjector
import me.illuminator3.packetlib.packet.Packet
import me.illuminator3.packetlib.packet.PacketHandler
import me.illuminator3.packetlib.packet.PacketInjector
import me.illuminator3.packetlib.packet.event.InlinedPacketListener
import me.illuminator3.packetlib.packet.event.PacketEvent
import me.illuminator3.packetlib.packet.event.PacketListener
import me.illuminator3.packetlib.utils.BiParameterizedFactory
import me.illuminator3.packetlib.utils.Hash
import me.illuminator3.packetlib.utils.ParameterizedFactory
import me.illuminator3.packetlib.utils.Reflection
import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.NotNull
import java.lang.reflect.Field
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.timerTask
import kotlin.jvm.Throws

class PacketLib private constructor (private val packetInjector: PacketInjector, private val handlerFactory: BiParameterizedFactory<PacketHandler, PacketLib, Player>, private val pluginInjector: PluginInjector, private val libListenerFactory: ParameterizedFactory<Listener, PacketLib>)
{
    companion object {
        @JvmStatic
        fun generateLibrary(packetInjector: PacketInjector, handlerFactory: BiParameterizedFactory<PacketHandler, PacketLib, Player>, pluginInjector: PluginInjector, libListenerFactory: ParameterizedFactory<Listener, PacketLib>) =
            PacketLib(packetInjector, handlerFactory, pluginInjector, libListenerFactory)

        @JvmStatic
        @Throws(PacketException::class)
        @NotNull
        fun getLibrary(plugin: JavaPlugin) =
            plugin.isEnabled
                .elseThrow(PacketException("Plugin not initialized"))
                .then {
                    println("[PacketLib] =================== @" + plugin.name + " ===================")
                    println("[PacketLib] Generating ...")
                }
                .returnWhen({
                    PacketLib(generatePacketInjector(), generateHandlerFactory(), generatePluginInjector(plugin), generateLibListenerFactory())
                }, {
                    null
                })


        private fun generatePacketInjector(): PacketInjector =
            object : PacketInjector {
                private val playerConnectionEntityPlayer: Field = Reflection.getField(Reflection.getClass("{nms}.EntityPlayer"), "playerConnection")
                private val playerConnectionClass: Class<*> = Reflection.getClass("{nms}.PlayerConnection")
                private val playerConnectionNetworkManager: Field = Reflection.getField(playerConnectionClass, "networkManager")
                private val networkManager: Class<*> = Reflection.getClass("{nms}.NetworkManager")
                private val channel: Field = Reflection.getField(networkManager, "channel")

                override fun inject(lib: PacketLib, player: Player)
                {
                    val ch = getChannel(getNetworkManager(player.asNMS()))

                    ch.pipeline().get("injector")?:run {
                        val handler = lib.handlerFactory.generate(lib, player)

                        ch.pipeline().addBefore("packet_handler", "injector", handler)
                    }
                }

                override fun uninject(lib: PacketLib, player: Player)
                {
                    val ch = getChannel(getNetworkManager(player.asNMS()))

                    ch.pipeline().get("injector")?.run {
                        ch.pipeline().remove("injector")
                    }
                }

                fun getNetworkManager(player: Any /* The NMS player */): Any /* The network manager */ = Reflection.getFieldValue(playerConnectionNetworkManager, Reflection.getFieldValue(playerConnectionEntityPlayer, player))
                fun getChannel(networkManager: Any /* The network manager */): Channel = Reflection.getFieldValue(channel, networkManager)
            }.then {
                println("[PacketLib] ... packet injector")
            }

        private fun generateHandlerFactory(): BiParameterizedFactory<PacketHandler, PacketLib, Player> =
            object : BiParameterizedFactory<PacketHandler, PacketLib, Player> {
                override fun generate(param1: PacketLib, param2: Player): PacketHandler =
                    object : PacketHandler(param2) {
                        override fun channelRead(player: Player, packet: Packet): Boolean =
                            param1.listeners.stream().map {
                                it.onPacket0(PacketEvent(player, packet))
                            }.anyMatch { it }
                    }
            }.then {
                println("[PacketLib] ... handler factory")
            }

        private fun generatePluginInjector(plugin: JavaPlugin): PluginInjector =
            object : PluginInjector {
                override fun registerListener(listener: Listener) =
                    plugin.server.pluginManager.registerEvents(listener, plugin)

                override fun registerCommand(name: String, command: CommandExecutor)
                {
                    plugin.getCommand(name)?.run {
                        executor = command
                    }
                }

                override fun registerTabCompleter(command: String, tabCompleter: TabCompleter)
                {
                    plugin.getCommand(command)?.run {
                        this.tabCompleter = tabCompleter
                    }
                }

                override fun isEnabled(): Boolean = plugin.isEnabled
                override fun getName(): String = plugin.name
            }.then {
                println("[PacketLib] ... plugin injector")
            }

        private fun generateLibListenerFactory(): ParameterizedFactory<Listener, PacketLib> =
            object : ParameterizedFactory<Listener, PacketLib> {
                override fun generate(param: PacketLib): Listener =
                    object : Listener {
                        @EventHandler
                        fun onJoin(e: PlayerJoinEvent) = param.packetInjector.inject(param, e.player)

                        @EventHandler
                        fun onQuit(e: PlayerQuitEvent) = param.packetInjector.uninject(param, e.player)
                    }
            }.then {
                println("[PacketLib] ... library listener factory")
            }

        private var listenerRegistered = false
    }

    init {
        listenerRegistered.thenElse {
            Timer().scheduleAtFixedRate(timerTask {
                pluginInjector.isEnabled().thenIf {
                    println("[PacketLib] Generating library listener")
                    println("[PacketLib] Registering listener")
                    println("[PacketLib] =================== @" + pluginInjector.getName() + " ===================")

                    pluginInjector.registerListener(libListenerFactory.generate(this@PacketLib))

                    listenerRegistered = true

                    cancel()
                }
            }, 0L, 20L)
        }.thenIf {
            println("[PacketLib] =================== @" + pluginInjector.getName() + " ===================")
        }
    }

    private val listeners: MutableList<PacketListener> = ArrayList()
    private val cachedInlinedListeners: MutableMap<Hash<InlinedPacketListener>, PacketListener> = HashMap()

    fun getPacketInjector(): PacketInjector = packetInjector
    fun getHandlerFactory(): BiParameterizedFactory<PacketHandler, PacketLib, Player> = handlerFactory
    fun getPluginInjector(): PluginInjector = pluginInjector
    fun getLibListenerFactory(): ParameterizedFactory<Listener, PacketLib> = libListenerFactory

    fun getListeners(): List<PacketListener> = listeners

    fun registerListener(listener: InlinedPacketListener)
    {
        val clistener =
            object : PacketListener() {
                override fun onPacket(e: PacketEvent) =
                    listener.onPacket(e)
            }

        cachedInlinedListeners[Hash.hash(listener)] = clistener

        registerListener(clistener)
    }

    fun registerListener(listener: PacketListener)
    {
        listeners += listener
    }

    fun unregisterListener(listener: InlinedPacketListener) =
        unregisterListener(cachedInlinedListeners.remove(Hash.hash(listener))!!)

    fun unregisterListener(listener: PacketListener)
    {
        listeners -= listener
    }

    fun clearListeners() = listeners.clear()
}

class PacketException(s: String) : RuntimeException(s)