package io.hikarilan.atomservermaker.beans

import java.nio.file.Path

data class ServerInstance(
    var customName: String,
    val server: Server,
    var javaPath: String,
    var runPath: Path,
    var corePath: Path
)
