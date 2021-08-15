package io.hikarilan.atomservermaker.utils

import com.google.gson.Gson
import com.google.gson.JsonObject
import io.hikarilan.atomservermaker.features.downloader.Downloader
import java.nio.file.Paths

class PaperMCUtil(project: Project) {

    private val original: String = "https://papermc.io/api/v2/projects/" + project.realName

    private fun getDetails(versions: String? = null, builds: String? = null): JsonObject {
        return if (versions == null) {
            Gson().fromJson(IOUtil.doSimpleGet(original), JsonObject::class.java)
        } else if (builds == null) {
            Gson().fromJson(IOUtil.doSimpleGet("$original/versions/$versions"), JsonObject::class.java)
        } else {
            Gson().fromJson(IOUtil.doSimpleGet("$original/versions/$versions/builds/$builds"), JsonObject::class.java)
        }
    }

    fun getFile(versions: String, builds: String): Downloader.NetworkFilePackage =
        getDetails(versions, builds)
            .getAsJsonObject("downloads")
            .getAsJsonObject("application")
            .run {
                Downloader.NetworkFilePackage(
                    getAsJsonPrimitive("name").asString, getAsJsonPrimitive("sha256").asString
                )
            }


    fun getAllMinecraftVersions(): List<String> =
        getDetails().getAsJsonArray("versions").map { it.asString }.toList().reversed()

    fun getBuilds(version: String): List<String> {
        return try {
            getDetails(version).getAsJsonArray("builds").map { it.asString }.toList().reversed()
        } catch (e: Exception) {
            listOf()
        }
    }

    fun download(versions: String, builds: String, saveDir: String): Downloader.FilePackage {
        return getFile(versions, builds).run {
            IOUtil.downloadByNIO2("$original/versions/$versions/builds/$builds/downloads/$fileName", saveDir, fileName)
            this.toFilePackage(Paths.get(saveDir))
        }
    }

    enum class Project(val realName: String) {
        PAPER("paper")
    }
}
