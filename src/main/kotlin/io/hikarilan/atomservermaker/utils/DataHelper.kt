package io.hikarilan.atomservermaker.utils

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.IntSize
import com.google.gson.GsonBuilder
import io.hikarilan.atomservermaker.beans.Profile
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.reader

object DataHelper {

    var windowSize = mutableStateOf(IntSize(800, 600))

    val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    var profile = Profile()

    private val profilePath = Paths.get("profile.json")

    fun checkProfile(): Boolean {
        if (profilePath.exists()) {
            loadProfile()
            return true
        } else {
            return false
        }
    }

    fun loadProfile() {
        profile = gson.fromJson(profilePath.reader(), Profile::class.java)
    }

    fun saveProfile() {
        Files.write(profilePath, gson.toJson(profile).lines())
    }
}