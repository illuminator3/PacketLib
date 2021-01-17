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

/**
 * @author illuminator3
 */
package me.illuminator3.packetlib.extensions

inline fun Boolean.thenIf(action: () -> Unit): Boolean
{
    if (this) action()
    return this
}

inline fun Boolean.thenElse(action: () -> Unit): Boolean = !((!this).thenIf(action))

fun Boolean.ifThrow(ex: Exception): Boolean = thenIf { throw ex }
fun Boolean.elseThrow(ex: Exception): Boolean = thenElse { throw ex }

inline fun <T> Boolean.returnWhen(isTrue: () -> T, isFalse: () -> T): T
{
    if (this)
        return isTrue()
    return isFalse()
}

fun <T> T.then(action: () -> Unit): T
{
    action()

    return this
}

fun <E : Enum<E>> Enum<E>.isOther(other: Enum<E>): Boolean = this.ordinal == other.ordinal
fun <E : Enum<E>> Enum<E>.isNot(other: Enum<E>): Boolean = this.ordinal != other.ordinal