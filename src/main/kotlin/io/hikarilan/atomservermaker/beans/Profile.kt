package io.hikarilan.atomservermaker.beans

data class Profile(
    var name: String = "default profile",
    var gameType: Int = -1,
    val serverInstances: MutableList<ServerInstance> = mutableListOf()
)