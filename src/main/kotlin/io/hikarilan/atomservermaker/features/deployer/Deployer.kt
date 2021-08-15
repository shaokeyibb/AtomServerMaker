package io.hikarilan.atomservermaker.features.deployer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import io.hikarilan.atomservermaker.beans.Server
import io.hikarilan.atomservermaker.features.downloader.AdditionalDownloader
import io.hikarilan.atomservermaker.features.downloader.Downloader
import java.nio.file.Path

abstract class Deployer {
    abstract val server: Server

    abstract var name:MutableState<String>
    var versions = mutableStateOf("")
    var builds = mutableStateOf("-1")
    abstract var savePath: Path
    var fileName = mutableStateOf("server.jar")

    abstract val downloader: Array<Downloader>
    abstract val additionalDownloader: Array<AdditionalDownloader<*>>?

    companion object {
        val list = listOf(
            PaperDeployer()
        )
    }
}