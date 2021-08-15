package io.hikarilan.atomservermaker.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import io.hikarilan.atomservermaker.utils.DataHelper

object MainView {

    private val meClickable = mutableStateOf(true)

    val showNewServerInstanceWindow = mutableStateOf(false)

    @Composable
    fun mainView() {
        Scaffold(topBar = {
            TopAppBar(title = {
                Text("AtomServerMaker - 主页面")
            }, actions = {
                Row(Modifier.clickable(enabled = meClickable.value) {
                    /*
                    meClickable.value = false
                    Window(title = "我的档案", events = WindowEvents(onClose = {
                        meClickable.value = true
                    })) {

                    }
                    */
                }) {
                    Icon(Icons.Filled.AccountBox, "我的档案")
                    Spacer(Modifier.size(3.dp))
                    Text(DataHelper.profile.name)
                }
            })
        }) {
            serversView()
        }
    }

    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
    @Composable
    fun serversView() {
        if (showNewServerInstanceWindow.value) {
            Window(onCloseRequest = {
                NewServerInstanceView.showSelector.value = true
                showNewServerInstanceWindow.value = false
            }, resizable = false, title = "新建服务器实例向导") {
                MaterialTheme(
                    colors = lightColors(
                        Color(40, 181, 244),
                        Color(114, 231, 255),
                        Color(124, 179, 66),
                        Color(174, 229, 113),
                        error = Color(244, 67, 54)
                    )
                ) {
                    NewServerInstanceView.init()
                }
            }
        }
        Column {
            Spacer(Modifier.size(3.dp))
            LazyVerticalGrid(
                cells = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize()
                    .scrollable(state = rememberScrollState(), orientation = Orientation.Vertical)
            ) {
                item {
                    Card(onClick = {
                        showNewServerInstanceWindow.value = true
                    }
                    ) {
                        Row {
                            Icon(Icons.Filled.Add, "新增一个服务器", Modifier.size(100.dp))
                            Column(
                                modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("点击以新增一个服务器实例")
                            }
                        }
                    }
                }

                items(items = DataHelper.profile.serverInstances) {
                    Card(Modifier.fillMaxWidth()) {
                        Row {
                            it.server.getLogo()
                            Column(
                                modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(it.customName)
                            }
                        }
                    }
                }
            }
        }
    }
}