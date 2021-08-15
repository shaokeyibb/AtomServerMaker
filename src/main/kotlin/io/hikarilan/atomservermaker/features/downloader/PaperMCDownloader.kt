package io.hikarilan.atomservermaker.features.downloader

import io.hikarilan.atomservermaker.features.deployer.Deployer
import io.hikarilan.atomservermaker.utils.IOUtil
import io.hikarilan.atomservermaker.utils.MessageDigestHelper
import io.hikarilan.atomservermaker.utils.PaperMCUtil
import java.io.IOException
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.exists

class PaperMCDownloader(private val project: String) : Downloader() {

    private val util: PaperMCUtil = PaperMCUtil(PaperMCUtil.Project.valueOf(project.uppercase()))

    override val name: String = "PaperMC"

    override fun available(): Boolean {
        try {
            IOUtil.doSimpleGet("https://papermc.io/api/v2/projects/${project.lowercase()}")
        } catch (e: IOException) {
            return false
        }
        return true
    }

    override val supportCheck: Boolean = true

    override fun check(files: Array<FilePackage>): Array<FilePackage>? {
        return files.filter { MessageDigestHelper.encodeToSHA256(it.file) != it.digest }.toTypedArray().run {
            if (size == 0) return null
            else this
        }
    }

    override val needVersionList: Boolean = true
    override val supportVersionList: Boolean = true
    override fun versionList(): List<String> = util.getAllMinecraftVersions()

    override val needBuildList: Boolean = true
    override val supportBuildList: Boolean = true
    override fun buildList(versions: String): List<String> = util.getBuilds(versions)

    override val supportAutoFileName: Boolean = true
    override fun autoFileName(versions: String, builds: String): String = util.getFile(versions, builds).fileName

    override fun download(deployer: Deployer): Array<FilePackage> {
        deployer.savePath.createDirectories()
        deployer.savePath.resolve(deployer.fileName.value).also {
            if (!it.exists())
                it.createFile()
        }
        return arrayOf(
            util.download(
                deployer.versions.value,
                deployer.builds.value,
                deployer.savePath.toRealPath().toString()
            )
        )
    }
}