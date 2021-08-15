import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.21"
    id("org.jetbrains.compose") version "1.0.0-alpha3"
}

group = "io.hikarilan"
version = "1.0.0"

repositories {
    maven("https://maven.aliyun.com/repository/central")
    maven("https://maven.aliyun.com/repository/google")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    mavenCentral()
    maven("https://maven.google.com/")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("com.google.code.gson:gson:2.8.7")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "16"
}

compose.desktop {
    application {
        mainClass = "io.hikarilan.atomservermaker.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "AtomServerMaker"
            packageVersion = "1.0.0"
        }
    }
}