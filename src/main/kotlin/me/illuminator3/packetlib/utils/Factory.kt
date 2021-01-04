package me.illuminator3.packetlib.utils

interface Factory<T>
{
    fun generate(): T
}