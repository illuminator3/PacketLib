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