package me.illuminator3.packetlib.utils

interface BiParameterizedFactory<T, P1, P2>
{
    fun generate(param1: P1, param2: P2): T
}