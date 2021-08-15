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

class NukkitX : Server() {
    override val name: String = "NukkitX"
    override val type: Int = 1
    override val pluginAPI: List<PluginAPI> = listOf(PluginAPI.NUKKIT_API)
    override val modAPI: List<ModAPI>? = null

    @Composable
    override fun getLogo() {
        Image(useResource("nukkitx-logo.png") { loadImageBitmap(it) }, "NukkitX Logo", modifier = Modifier.size(100.dp))
    }
}