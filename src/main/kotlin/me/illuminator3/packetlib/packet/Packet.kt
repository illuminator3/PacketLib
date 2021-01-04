package me.illuminator3.packetlib.packet

import me.illuminator3.packetlib.utils.Reflection
import java.lang.reflect.Field
import java.lang.reflect.Method

data class Packet internal constructor (private val original: Any /* NMS Packet */)
{
    fun getField(name: String): Field = Reflection.getField(original::class.java, name)

    @Suppress("UNCHECKED_CAST")
    fun <T> getFieldValue(field: String): T = getField(field).get(original) as T

    fun getMethod(name: String): Method = Reflection.getMethod(original::class.java, name)

    @Suppress("UNCHECKED_CAST")
    fun <T> invokeMethod(method: String, vararg args: Any): T = getMethod(method).invoke(original, *args /* kOtLiN hAS pOiNtERs?! */) as T
}