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