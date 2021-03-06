package io.hikarilan.atomservermaker.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import io.hikarilan.atomservermaker.beans.ServerInstance
import io.hikarilan.atomservermaker.features.deployer.Deployer
import io.hikarilan.atomservermaker.features.downloader.AdditionalDownloader
import io.hikarilan.atomservermaker.features.downloader.Downloader
import io.hikarilan.atomservermaker.utils.DataHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.DigestException

object NewServerInstanceView {

    private val preferGame = mutableStateOf(DataHelper.profile.gameType)

    private val deployServer = mutableStateOf<Deployer?>(null)

    val showSelector = mutableStateOf(true)

    @Composable
    fun init() {
        if (showSelector.value)
            newServerInstanceView()
        else {
            deployServerView()
        }
    }

    @OptIn(
        ExperimentalUnitApi::class, kotlinx.coroutines.DelicateCoroutinesApi::class,
        ExperimentalFoundationApi::class
    )
    @Composable
    private fun deployServerView() {
        val selectedDownloader =
            remember { mutableStateOf<Downloader?>(null) }
        remember { selectedDownloader.value = null }

        val selectedAdditionalDownloader =
            remember { mutableStateOf<AdditionalDownloader<*>?>(null) }
        remember { selectedDownloader.value = null }

        val page = remember { mutableStateOf(1) }
        val finished = remember { mutableStateOf(false) }

        remember {
            selectedDownloader.value = null
            selectedAdditionalDownloader.value = null
        }

        Scaffold(topBar = {
            TopAppBar(title = {
                Text("?????? ${deployServer.value!!.server.name}")
            }, actions = {
                Row(Modifier.clickable(enabled = page.value < 5) {
                    showSelector.value = true
                }) {
                    Text("??????")
                    Spacer(Modifier.size(3.dp))
                    Icon(Icons.Filled.ArrowBack, "??????")
                }
            }, navigationIcon = {
                deployServer.value!!.server.getLogo()
            })
        }, bottomBar = {
            BottomAppBar {
                Spacer(Modifier.weight(1f, true))
                Text("???????????????${page.value}")
                Spacer(Modifier.weight(1f, true))
                Button(enabled = when (page.value) {
                    1 -> selectedDownloader.value != null
                    2 -> {
                        if (selectedDownloader.value!!.needVersionList) {
                            deployServer.value!!.versions.value != ""
                        } else {
                            true
                        } &&
                                if (selectedDownloader.value!!.needBuildList) {
                                    deployServer.value!!.builds.value != "-1"
                                } else {
                                    true
                                }
                    }
                    3 -> deployServer.value!!.fileName.value.isNotBlank() && deployServer.value!!.name.value.isNotBlank() && deployServer.value!!.name.value in DataHelper.profile.serverInstances.map { it.customName }
                        .toList()
                    4 -> true
                    5 -> finished.value
                    else -> false
                }, onClick = {
                    if (finished.value) {
                        MainView.showNewServerInstanceWindow.value = false
                    }
                    page.value = ++page.value
                    // Cleanup
                    when (page.value) {
                        2 -> {
                            deployServer.value!!.versions.value = ""
                            deployServer.value!!.builds.value = "-1"
                        }
                    }
                }) {
                    if (!finished.value) {
                        Icon(Icons.Filled.Send, "??????")
                        Text("??????")
                    } else {
                        Icon(Icons.Filled.Done, "??????")
                        Text("??????")
                    }
                }
            }
        }) {
            when (page.value) {
                1 -> {
                    Column {
                        val availableDownloader = remember { mutableStateListOf<Downloader?>(null) }
                        val availableAdditionalDownloader =
                            remember { mutableStateListOf<AdditionalDownloader<*>?>(null) }

                        if (availableDownloader.any { it == null } || availableAdditionalDownloader.any { it == null }) {
                            remember {
                                GlobalScope.launch {
                                    availableDownloader.also {
                                        it.addAll(deployServer.value!!.downloader.filter { it.available() }
                                            .distinctBy { it.name })
                                        it.remove(null)
                                    }
                                    availableAdditionalDownloader.also {
                                        deployServer.value!!.additionalDownloader?.filter { it.available() }
                                            ?.distinctBy { it.name }
                                            ?.let { it1 -> it.addAll(it1) }
                                        it.remove(null)
                                    }
                                    selectedDownloader.value = availableDownloader[0]
                                }
                            }
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Top
                            ) {

                                Text("???????????????", fontSize = TextUnit(32.0f, type = TextUnitType.Sp))
                                Spacer(Modifier.fillMaxSize(0.3f))

                                Text("????????????????????????????????????????????????")
                                Spacer(Modifier.size(7.dp))
                                LazyRow {
                                    items(items = availableDownloader) {
                                        RadioButton(
                                            selectedDownloader.value == it,
                                            onClick = { selectedDownloader.value = it }
                                        )
                                        Text(it!!.name)
                                        Spacer(Modifier.size(3.dp))
                                    }
                                }
                                Spacer(Modifier.size(20.dp))
                                if (deployServer.value!!.additionalDownloader != null) {
                                    Text("???????????????????????????????????????????????????????????????")
                                    Spacer(Modifier.size(7.dp))
                                    LazyRow {
                                        item {
                                            RadioButton(
                                                selectedAdditionalDownloader.value == null,
                                                onClick = { selectedAdditionalDownloader.value = null }
                                            )
                                            Text("???????????????????????????")
                                            Spacer(Modifier.size(3.dp))
                                        }
                                        items(items = availableAdditionalDownloader) {
                                            RadioButton(
                                                selectedAdditionalDownloader.value == it,
                                                onClick = { selectedAdditionalDownloader.value = it }
                                            )
                                            Text(it!!.name)
                                            Spacer(Modifier.size(3.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    Column(modifier = Modifier.fillMaxSize()) {

                        val versions = remember { mutableStateListOf<String>() }
                        val builds = remember { mutableStateListOf<String>() }

                        if (selectedDownloader.value!!.needVersionList) {
                            if (selectedDownloader.value!!.supportVersionList) {

                                Text("????????????????????????????????????")
                                Spacer(Modifier.size(7.dp))

                                if (versions.isEmpty()) {
                                    CircularProgressIndicator()
                                    remember {
                                        GlobalScope.launch {
                                            delay(1000L)
                                            versions.addAll(selectedDownloader.value!!.versionList())
                                            deployServer.value!!.versions.value = versions[0]
                                        }
                                    }
                                } else {
                                    LazyVerticalGrid(
                                        cells = GridCells.Fixed(5),
                                        modifier = Modifier.scrollable(
                                            state = rememberScrollState(),
                                            orientation = Orientation.Vertical
                                        ).fillMaxHeight(0.3f)
                                    ) {
                                        items(versions) {
                                            Row {
                                                RadioButton(
                                                    deployServer.value!!.versions.value == it,
                                                    onClick = {
                                                        if (deployServer.value!!.versions.value != it) {
                                                            deployServer.value!!.builds.value = "-1"
                                                            builds.clear()
                                                        }
                                                        deployServer.value!!.versions.value = it
                                                    }
                                                )
                                                Text(it)
                                            }
                                        }
                                    }
                                }
                            } else {
                                Text("???????????????????????????????????????????????????1.12.2???")
                                OutlinedTextField(
                                    value = deployServer.value!!.versions.value,
                                    onValueChange = {
                                        deployServer.value!!.versions.value = it
                                    },
                                    maxLines = 1,
                                    label = {
                                        Text("??????????????????????????????")
                                    },
                                    isError = deployServer.value!!.versions.value.replace('.', '0')
                                        .toLongOrNull() == null

                                )
                            }

                        } else {
                            Text("?????????????????????????????????????????????????????????")
                        }

                        Spacer(Modifier.size(10.dp))
                        Divider()
                        Spacer(Modifier.size(10.dp))

                        if (selectedDownloader.value!!.needBuildList) {
                            if (selectedDownloader.value!!.supportBuildList) {

                                Text("??????????????????????????????????????????")
                                Spacer(Modifier.size(7.dp))

                                if (deployServer.value!!.versions.value != "" && builds.isEmpty()) {
                                    CircularProgressIndicator()
                                    remember {
                                        GlobalScope.launch {
                                            delay(1000L)
                                            builds.addAll(selectedDownloader.value!!.buildList(deployServer.value!!.versions.value))
                                        }
                                    }
                                } else {
                                    LazyVerticalGrid(
                                        cells = GridCells.Fixed(5),
                                        modifier = Modifier.scrollable(
                                            state = rememberScrollState(),
                                            orientation = Orientation.Vertical
                                        ).fillMaxHeight(0.5f)
                                    ) {
                                        items(builds) {
                                            Row {
                                                RadioButton(
                                                    deployServer.value!!.builds.value == it,
                                                    onClick = {
                                                        deployServer.value!!.builds.value = it
                                                    }
                                                )
                                                Text(it)
                                            }
                                        }
                                    }
                                }
                            } else {
                                Text("?????????????????????????????????????????????????????????103???")
                                OutlinedTextField(
                                    value = deployServer.value!!.builds.value,
                                    onValueChange = {
                                        deployServer.value!!.builds.value = it
                                    },
                                    maxLines = 1,
                                    label = {
                                        Text("????????????????????????????????????")
                                    },
                                    isError = deployServer.value!!.builds.value.toLongOrNull() == null

                                )
                            }
                        } else {
                            Text("?????????????????????????????????????????????????????????")
                        }

                        Spacer(Modifier.size(10.dp))
                        Divider()
                        Spacer(Modifier.size(10.dp))


                        if (!selectedDownloader.value!!.supportVersionList || !selectedDownloader.value!!.supportBuildList) {
                            Text(
                                "????????????????????????????????? ${
                                    if (!selectedDownloader.value!!.supportVersionList) "????????????" else ""
                                } ${
                                    if (!selectedDownloader.value!!.supportBuildList) "??????????????????" else ""
                                }??????????????????????????????????????????", color = MaterialTheme.colors.error
                            )
                        }
                    }
                }
                3 -> {
                    //Name && FileName
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("???????????????????????????????????????")
                        OutlinedTextField(
                            value = deployServer.value!!.name.value,
                            onValueChange = { s: String -> deployServer.value!!.name.value = s },
                            isError = deployServer.value!!.name.value.isBlank(), maxLines = 1,
                            label = {
                                Text("???????????????????????????")
                            }
                        )

                        Spacer(Modifier.size(50.dp))
                        Divider()
                        Spacer(Modifier.size(50.dp))

                        Text("??????????????????????????????")
                        if (selectedDownloader.value!!.supportAutoFileName) {
                            deployServer.value!!.fileName.value = selectedDownloader.value!!.autoFileName(
                                deployServer.value!!.versions.value,
                                deployServer.value!!.builds.value
                            )
                        }
                        OutlinedTextField(
                            value = deployServer.value!!.fileName.value,
                            onValueChange = { s: String -> deployServer.value!!.fileName.value = s },
                            isError = deployServer.value!!.fileName.value.isBlank(), maxLines = 1,
                            label = {
                                Text("????????????????????????????????????")
                            }
                        )
                    }
                }
                4 -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("???????????????")
                        Spacer(Modifier.size(20.dp))
                        Text("?????? ????????? ???????????????")
                    }
                }
                5 -> {
                    val progressAvailable = remember { mutableStateOf(true) }
                    val status = remember { mutableStateOf("") }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (progressAvailable.value)
                            CircularProgressIndicator(modifier = Modifier.fillMaxSize(0.5f))
                        Text(status.value)
                    }

                    fun download0(): Array<Downloader.FilePackage> {
                        return selectedDownloader.value!!.download(deployServer.value!!)
                    }

                    fun download1(): Array<Downloader.FilePackage> {
                        return selectedAdditionalDownloader.value!!.download()
                    }

                    remember {
                        GlobalScope.launch {
                            delay(1000L)
                            try {
                                status.value = "?????????????????????..."
                                val d0 = download0()

                                if (selectedDownloader.value!!.supportCheck) {
                                    status.value = "?????????????????????..."
                                    val c0 = selectedDownloader.value!!.check(d0)
                                    if (c0 != null) {
                                        throw DigestException(c0.joinToString { it.file.name })
                                    }
                                }

                                if (selectedAdditionalDownloader.value != null) {
                                    status.value = "?????????????????????..."
                                    val d1 = download1()

                                    if (selectedAdditionalDownloader.value!!.supportCheck) {
                                        status.value = "?????????????????????..."
                                        val c1 = selectedAdditionalDownloader.value!!.check(d1)
                                        if (c1 != null) {
                                            throw DigestException(c1.joinToString { it.file.name })
                                        }
                                    }
                                }

                                status.value = "????????????"
                                progressAvailable.value = false
                                finished.value = true

                                //TODO: ADD TO DEPLOY LIST
                                DataHelper.profile.serverInstances.add(
                                    ServerInstance(
                                        customName = deployServer.value!!.name.value,
                                        server = deployServer.value!!.server,
                                        javaPath = "java",
                                        runPath = deployServer.value!!.savePath,
                                        corePath = deployServer.value!!.savePath.resolve(deployServer.value!!.fileName.value)
                                    )
                                )

                            } catch (e: DigestException) {
                                status.value = e.message + " ??????????????????????????????????????????"
                                progressAvailable.value = false
                            } catch (e: Exception) {
                                status.value = "????????????"
                                progressAvailable.value = false
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun newServerInstanceView() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.size(5.dp))
            Row {
                Row(Modifier.clickable { preferGame.value = 0 }) {
                    RadioButton(preferGame.value == 0, onClick = null)
                    Text("Java ????????????")
                }
                Spacer(Modifier.size(10.dp))
                Row(Modifier.clickable { preferGame.value = 1 }) {
                    RadioButton(preferGame.value == 1, onClick = null)
                    Text("Bedrock ????????????")
                }
            }
            Spacer(Modifier.size(10.dp))

            if (preferGame.value == 0) {
                serverListView(0)
            } else if (preferGame.value == 1) {
                serverListView(1)
            }
        }
    }

    @OptIn(
        ExperimentalFoundationApi::class, kotlinx.coroutines.DelicateCoroutinesApi::class,
        ExperimentalUnitApi::class
    )
    @Composable
    private fun serverListView(type: Int) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize()
                .scrollable(state = rememberScrollState(), orientation = Orientation.Vertical)
        ) {
            items(Deployer.list.stream().filter { it.server.type == type }.toList()) {
                val canDeploy = remember { mutableStateOf<Boolean?>(null) }
                val canSpeed = remember { mutableStateOf<Boolean?>(null) }

                Card(Modifier.clickable(enabled = (canDeploy.value == true && canSpeed.value != null)) {
                    deployServer.value = it
                    showSelector.value = false
                }) {
                    Row {
                        it.server.getLogo()
                        Column {
                            if (canDeploy.value == null || canSpeed.value == null) {
                                remember {
                                    GlobalScope.launch {
                                        canDeploy.value = !it.downloader.any { !it.available() }
                                        canSpeed.value =
                                            (it.additionalDownloader != null) && it.additionalDownloader.any { it.available() }
                                    }
                                }
                                LinearProgressIndicator()
                            }
                            Text(it.server.name, fontSize = TextUnit(24.0f, TextUnitType.Sp))
                            if (it.server.pluginAPI == null) {
                                Text("- ????????????????????? API")
                            } else {
                                Text("- ???????????? ${it.server.pluginAPI!!.map { it.friendlyName }.toList()} ?????????")
                            }
                            if (it.server.modAPI == null) {
                                Text("- ??????????????????????????????")
                            } else {
                                Text("- ???????????? ${it.server.modAPI!!.map { it.friendlyName }.toList()} ?????????")
                            }
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Top
                            ) {
                                Spacer(Modifier.size(3.dp))
                                Row {
                                    if (canDeploy.value == false) {
                                        Text("???????????????", color = MaterialTheme.colors.error)
                                    } else if (canDeploy.value == true) {
                                        Text("????????????", color = MaterialTheme.colors.primary)
                                    }
                                    Spacer(Modifier.size(5.dp))
                                    if (canDeploy.value == true)
                                        if (canSpeed.value == true) {
                                            Text("????????????????????????", color = MaterialTheme.colors.secondary)
                                        } else if (canSpeed.value == false) {
                                            Text("???????????????????????????", color = MaterialTheme.colors.error)
                                        }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}