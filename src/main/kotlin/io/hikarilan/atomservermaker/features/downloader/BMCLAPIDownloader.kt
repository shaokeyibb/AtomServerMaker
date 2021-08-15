package io.hikarilan.atomservermaker.features.downloader

import io.hikarilan.atomservermaker.utils.BMCLAPIUtil
import io.hikarilan.atomservermaker.utils.IOUtil
import io.hikarilan.atomservermaker.utils.MessageDigestHelper
import java.io.IOException

class BMCLAPIDownloader(
    private val src: BMCLAPIUtil.Src,
    downloadAdditionalResource: (BMCLAPIUtil) -> Array<Downloader.FilePackage>,
) : AdditionalDownloader<BMCLAPIUtil>(
    downloadAdditionalResource
) {
    override val name: String = "BMCLAPI by ${src.name}"
    override val description: String = "用于下载 Forge 资源库，Vanilla 服务端核心等资源"

    override fun available(): Boolean {
        try {
            IOUtil.doSimpleGet(src.link)
        } catch (e: IOException) {
            return false
        }
        return true
    }

    override val supportCheck: Boolean = true

    override fun check(files: Array<Downloader.FilePackage>): Array<Downloader.FilePackage>? {
        return files.filter { MessageDigestHelper.encodeToMD5(it.file) != it.digest || MessageDigestHelper.encodeToSHA1(it.file) != it.digest }.toTypedArray().run {
            if (size == 0) return null
            else this
        }
    }

    override val util: BMCLAPIUtil = BMCLAPIUtil(src)
}