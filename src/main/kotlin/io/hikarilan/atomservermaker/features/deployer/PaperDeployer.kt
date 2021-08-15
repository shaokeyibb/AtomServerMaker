package io.hikarilan.atomservermaker.features.deployer

import androidx.compose.runtime.mutableStateOf
import io.hikarilan.atomservermaker.beans.Server
import io.hikarilan.atomservermaker.beans.servers.Paper
import io.hikarilan.atomservermaker.features.downloader.AdditionalDownloader
import io.hikarilan.atomservermaker.features.downloader.BMCLAPIDownloader
import io.hikarilan.atomservermaker.features.downloader.Downloader
import io.hikarilan.atomservermaker.features.downloader.PaperMCDownloader
import io.hikarilan.atomservermaker.utils.BMCLAPIUtil
import io.hikarilan.atomservermaker.utils.DataHelper
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.exists

class PaperDeployer : Deployer() {
    override val server: Server = Paper()
    override var name = mutableStateOf("${DataHelper.profile.name} 的 ${server.name} 服务器")
    override var savePath: Path = Paths.get("servers", name.value)
    override val downloader: Array<Downloader> = arrayOf(PaperMCDownloader("Paper"))
    override val additionalDownloader: Array<AdditionalDownloader<*>> = arrayOf(
        BMCLAPIDownloader(BMCLAPIUtil.Src.MCBBS) {
            this.savePath.resolve("cache").createDirectories()
            this.savePath.resolve("cache").resolve(this.fileName.value).also {
                if (!it.exists())
                    it.createFile()
            }
            arrayOf(
                it.downloadServer(
                    this.versions.value,
                    this.savePath.resolve("cache").toRealPath().toString(),
                    "mojang_${this.versions.value}.jar"
                )
            )
        },
        BMCLAPIDownloader(BMCLAPIUtil.Src.BMCLAPI) {
            this.savePath.resolve("cache").createDirectories()
            this.savePath.resolve("cache").resolve(this.fileName.value).also {
                if (!it.exists())
                    it.createFile()
            }
            arrayOf(
                it.downloadServer(
                    this.versions.value,
                    this.savePath.resolve("cache").toRealPath().toString(),
                    "mojang_${this.versions.value}.jar"
                )
            )
        }
    )
}