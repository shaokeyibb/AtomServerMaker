package io.hikarilan.atomservermaker

import androidx.compose.runtime.mutableStateOf

enum class ModelType {
    FIRST_RUN,
    NORMAL;

    companion object {
        val model = mutableStateOf(NORMAL)
    }
}