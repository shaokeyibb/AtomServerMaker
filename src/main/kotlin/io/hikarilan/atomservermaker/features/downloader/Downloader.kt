package io.hikarilan.atomservermaker.features.downloader

import io.hikarilan.atomservermaker.features.deployer.Deployer
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

abstract class Downloader {

    abstract val name: String

    abstract fun available(): Boolean

    abstract val supportCheck: Boolean
    abstract fun check(files: Array<FilePackage>): Array<FilePackage>?

    abstract val needVersionList: Boolean
    abstract val supportVersionList: Boolean
    abstract fun versionList(): List<String>

    abstract val needBuildList: Boolean
    abstract val supportBuildList: Boolean
    abstract fun buildList(versions: String): List<String>

    abstract val supportAutoFileName: Boolean
    abstract fun autoFileName(versions: String, builds: String): String

    abstract fun download(deployer: Deployer): Array<FilePackage>

    data class FilePackage(val file: File, val digest: String) {
        fun toNetworkFilePackage() = NetworkFilePackage(file.name, digest)
    }

    data class NetworkFilePackage(val fileName: String, val digest: String) {
        fun toFilePackage(savePath: Path) =
            FilePackage(Paths.get(savePath.toRealPath().toString(), fileName).toFile(), digest)
    }
}