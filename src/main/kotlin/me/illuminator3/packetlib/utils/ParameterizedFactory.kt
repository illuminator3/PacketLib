package me.illuminator3.packetlib.utils

interface ParameterizedFactory<T, P>
{
    fun generate(param: P): T
}