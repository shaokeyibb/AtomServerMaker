package io.hikarilan.atomservermaker.beans

import androidx.compose.runtime.Composable
import io.hikarilan.atomservermaker.beans.servers.Paper
import io.hikarilan.atomservermaker.features.deployer.Deployer

abstract class Server {

    abstract val name: String

    abstract val type: Int

    abstract val pluginAPI: List<PluginAPI>?

    abstract val modAPI: List<ModAPI>?

    @Composable
    abstract fun getLogo()

    enum class PluginAPI(val friendlyName: String) {

        BUKKIT_API(friendlyName = "Bukkit API"),
        SPONGE_API(friendlyName = "Sponge API"),
        NUKKIT_API("Nukkit API"),
        OTHER(friendlyName = "其他 API")

    }

    enum class ModAPI(val friendlyName: String) {

        FORGE_API(friendlyName = "Forge Mod Loader"),
        FABRIC_API(friendlyName = "Fabric Mod Loader"),
        OTHER(friendlyName = "其他加载器")

    }

}