package io.hikarilan.atomservermaker.beans.servers

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import io.hikarilan.atomservermaker.beans.Server
import io.hikarilan.atomservermaker.features.deployer.Deployer
import io.hikarilan.atomservermaker.features.downloader.AdditionalDownloader
import io.hikarilan.atomservermaker.features.downloader.Downloader

class Spigot : Server() {
    override val name: String = "Spigot"
    override val type: Int = 0
    override val pluginAPI: List<PluginAPI>? = listOf(PluginAPI.BUKKIT_API)
    override val modAPI: List<ModAPI>? = null

    @Composable
    override fun getLogo() {
        Image(useResource("spigot-logo.png") { loadImageBitmap(it) }, "Spigot Logo", modifier = Modifier.size(100.dp))
    }
}