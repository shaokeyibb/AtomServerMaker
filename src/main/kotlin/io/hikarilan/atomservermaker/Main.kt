package io.hikarilan.atomservermaker

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.hikarilan.atomservermaker.utils.DataHelper
import io.hikarilan.atomservermaker.views.FirstRunView
import io.hikarilan.atomservermaker.views.MainView

fun main() {

    ModelType.model.value = if (DataHelper.checkProfile()) ModelType.NORMAL else ModelType.FIRST_RUN

    application {
        Window(
            onCloseRequest = ::exitApplication,
            resizable = false,
            title = "AtomServerMaker 主程序"
        ) {
            MaterialTheme(
                colors = lightColors(
                    Color(40, 181, 244),
                    Color(114, 231, 255),
                    Color(124, 179, 66),
                    Color(174, 229, 113),
                    error = Color(244, 67, 54)
                )
            ) {
                if (ModelType.model.value == ModelType.FIRST_RUN) {
                    FirstRunView.run()
                } else if (ModelType.model.value == ModelType.NORMAL) {
                    DataHelper.windowSize.value = IntSize(1000, 800)
                    MainView.mainView()
                }
            }
        }
    }
}

