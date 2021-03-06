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

package me.illuminator3.packetlib.utils

/**
 * @author illuminator3
 */
class Hash<T>(private val code: Int)
{
    companion object {
        @JvmStatic
        fun <T> hash(obj: T): Hash<T> = Hash(obj.hashCode())

        @JvmStatic
        fun <T> compare(h1: Hash<T>, h2: Hash<T>): Boolean = h1.code == h2.code
    }
}