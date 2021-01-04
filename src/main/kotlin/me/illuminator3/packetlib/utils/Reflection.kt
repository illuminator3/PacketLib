package me.illuminator3.packetlib.utils

import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import java.lang.reflect.Field
import java.lang.reflect.Method

object Reflection
{
    @JvmStatic
    private val ver: String = Bukkit.getServer().javaClass.`package`.name.replace(".", ",").split(",")[3]

    @JvmStatic
    private val getHandle: Method = getClass("{cb}.entity.CraftPlayer").getMethod("getHandle")

    @JvmStatic
    fun getClass(className: String): Class<*>
    {
        return Class.forName(
                className
                        .replace("{nms}", "net.minecraft.server.$ver")
                        .replace("{cb}", "org.bukkit.craftbukkit.$ver")
        )
    }

    @JvmStatic
    fun getNMSPlayer(player: Player): Any /* The NMS player */
    {
        return getHandle.invoke(player)
    }

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T> getFieldValue(field: Field, obj: Any): T
    {
        return field.get(obj) as T
    }

    @JvmStatic
    fun getField(cls: Class<*>, fieldName: String): Field
    {
        val field = cls.getDeclaredField(fieldName)

        field.isAccessible = true

        return field
    }

    @JvmStatic
    fun getMethod(cls: Class<*>, methodName: String, vararg params: Class<*>): Method
    {
        val method = cls.getDeclaredMethod(methodName, *params)

        method.isAccessible = true

        return method
    }
}