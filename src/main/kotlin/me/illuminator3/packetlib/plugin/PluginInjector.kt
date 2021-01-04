package me.illuminator3.packetlib.plugin

import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter
import org.bukkit.event.Listener

interface PluginInjector
{
    fun registerListener(listener: Listener)
    fun registerCommand(name: String, command: CommandExecutor)
    fun registerTabCompleter(command: String, tabCompleter: TabCompleter)

    fun isEnabled(): Boolean
    fun getName(): String
}