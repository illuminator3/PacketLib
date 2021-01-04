package me.illuminator3.packetlib.utils

class Hash<T>(private val code: Int)
{
    companion object {
        @JvmStatic
        fun <T> hash(obj: T): Hash<T> = Hash(obj.hashCode())

        @JvmStatic
        fun <T> compare(h1: Hash<T>, h2: Hash<T>): Boolean = h1.code == h2.code
    }
}