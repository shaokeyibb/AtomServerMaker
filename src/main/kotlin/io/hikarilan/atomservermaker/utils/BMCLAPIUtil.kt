package io.hikarilan.atomservermaker.utils

import com.google.gson.Gson
import com.google.gson.JsonArray
import io.hikarilan.atomservermaker.features.downloader.Downloader
import java.nio.file.Paths
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile

class BMCLAPIUtil(private val source: Src) {

    fun getOriginalForgeFileName(
        mcVersion: String,
        version: String,
        category: String,
        format: String
    ) = "forge-$mcVersion-$version-$category.$format"

    private fun getForgeList(mcversion: String): JsonArray =
        Gson().fromJson(IOUtil.doSimpleGet(source.link + "/forge/minecraft/$mcversion"), JsonArray::class.java)

    private fun getForgeBuildsList(mcVersion: String): List<String> =
        getForgeList(mcVersion).map { it.asJsonObject.getAsJsonPrimitive("version").asString }.sortedByDescending {
            it.replace('.', '0').toLong()
        }

    fun downloadForge(
        mcVersion: String,
        version: String? = null,
        category: String? = null,
        format: String? = null,
        saveDir: String,
        fileName: String? = null
    ): Downloader.FilePackage {
        val ver = version ?: getForgeBuildsList(mcVersion)[0]
        val cat = category ?: "installer"
        val form = format ?: "jar"
        return Downloader.FilePackage(
            IOUtil.downloadByHTTPConn(
                IOUtil.getRedirectUrl(
                    source.link + "/forge/download", mapOf(
                        Pair("mcversion", mcVersion),
                        Pair("version", ver),
                        Pair("category", cat),
                        Pair("format", form)
                    )
                ),
                saveDir,
                fileName ?: getOriginalForgeFileName(mcVersion, ver, cat, form),
                null
            ),
            getForgeList(mcVersion).first { it.asJsonObject.getAsJsonPrimitive("version").asString == ver }.asJsonObject.getAsJsonArray(
                "files"
            ).first {
                it.asJsonObject.getAsJsonPrimitive("format").asString == form && it.asJsonObject.getAsJsonPrimitive("category").asString == cat
            }.asJsonObject.getAsJsonPrimitive("hash").asString
        )
    }

    private fun getServer(versions: String): Downloader.NetworkFilePackage {
        val location = IOUtil.getRedirectUrl(source.link + "/version/$versions/server", null)
        val spilt = location.split("/").asReversed()
        return Downloader.NetworkFilePackage(spilt[0], spilt[1])
    }

    fun downloadServer(versions: String, saveDir: String, fileName: String? = null): Downloader.FilePackage {
        val location = IOUtil.getRedirectUrl(source.link + "/version/$versions/server", null)
        val spilt = location.split("/").asReversed()
        return Downloader.FilePackage(
            IOUtil.downloadByHTTPConn(
                location,
                saveDir,
                fileName ?: spilt[0],
                null
            ), spilt[1]
        )
    }


    enum class Src(val link: String) {
        BMCLAPI("https://bmclapi2.bangbang93.com"), MCBBS("https://download.mcbbs.net")
    }
}