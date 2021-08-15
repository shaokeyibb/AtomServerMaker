package io.hikarilan.atomservermaker.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.hikarilan.atomservermaker.ModelType
import io.hikarilan.atomservermaker.utils.DataHelper

object FirstRunView {

    private val step = mutableStateOf(0)
    private const val max = 3
    private val complete = mutableStateMapOf(0 to true)
    private val next = mutableStateOf("下一步")

    private val name = mutableStateOf("")

    // 0 of Java, 1 of Bedrock
    private val selectedGame = mutableStateOf(-1)

    @Composable
    fun run() {
        Scaffold(topBar = {
            TopAppBar(title = {
                Text("AtomServerMaker - 第一次运行")
            })
        }, bottomBar = {
            BottomAppBar() {
                Button(onClick = {
                    step.value = --step.value
                    if (step.value == max) {
                        next.value = "完成"
                    } else {
                        next.value = "下一步"
                    }
                }, enabled = step.value != 0) {
                    Text("上一步")
                }
                Spacer(Modifier.weight(1f, true))
                Text("当前步骤：${step.value}")
                Spacer(Modifier.weight(1f, true))

                Button(
                    onClick = {
                        if (step.value == max) {
                            DataHelper.profile.name = name.value
                            DataHelper.profile.gameType = selectedGame.value
                            DataHelper.saveProfile()
                            ModelType.model.value = ModelType.NORMAL
                            return@Button
                        }
                        step.value = ++step.value
                        if (step.value == max) {
                            next.value = "完成"
                        } else {
                            next.value = "下一步"
                        }
                    },
                    enabled = complete[step.value] ?: false
                ) {
                    Text(next.value)
                }
            }
        })
        {
            when (step.value) {
                0 -> runModel0()
                1 -> runModel1()
                2 -> runModel2()
                3 -> runModel3()
            }
        }

    }

    @Composable
    private fun runModel0() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("欢迎使用 AtomServerMaker")
            Spacer(Modifier.size(10.dp))
            Text("初次使用，让我们进行一些设置")
        }
    }

    @Composable
    private fun runModel1() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("请告诉我们应该如何称呼您")
            Spacer(Modifier.size(25.dp))
            OutlinedTextField(
                value = name.value,
                onValueChange = {
                    name.value = it
                    complete[1] = name.value.isNotBlank()
                },
                label = {
                    Text("您的昵称")
                }, maxLines = 1, isError = name.value.isBlank()
            )
        }
    }

    @Composable
    private fun runModel2() {
        complete[2] = selectedGame.value != -1
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("请告诉我们您更偏爱于哪种 Minecraft 服务器类型")
            Spacer(Modifier.size(20.dp))
            Row {
                RadioButton(selectedGame.value == 0, onClick = {
                    selectedGame.value = 0
                })
                Text("Java 版服务器")
                Spacer(Modifier.size(25.dp))
                RadioButton(selectedGame.value == 1, onClick = {
                    selectedGame.value = 1
                })
                Text("Bedrock 版服务器")
            }
        }
    }

    @Composable
    private fun runModel3() {
        complete[3] = true
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("设置已完成，感谢您的耐心")
            Spacer(Modifier.size(10.dp))
            Text("以上设置均可在 “我的档案” 界面中随时更改")
        }
    }
}