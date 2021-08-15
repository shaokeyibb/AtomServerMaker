package io.hikarilan.atomservermaker.beans.servers

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import io.hikarilan.atomservermaker.beans.Server

class Paper : Server() {
    override val name: String = "Paper"
    override val type: Int = 0
    override val pluginAPI: List<PluginAPI> = listOf(PluginAPI.BUKKIT_API)
    override val modAPI: List<ModAPI>? = null

    @Composable
    override fun getLogo() {
        Image(
            useResource("paper-logo.png") { loadImageBitmap(it) },
            contentDescription = "Paper Logo",
            modifier = Modifier.size(100.dp)
        )
    }
}