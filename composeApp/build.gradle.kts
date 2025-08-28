import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvm("desktop")
    jvmToolchain(21)
    
    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation("io.github.vooft:compose-treeview-core:0.2.1")
            implementation(libs.ktor.serialization.kotlinx.jsn)
            implementation(libs.ktor.client.content.negotiation)
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.9.1")
            implementation("org.jetbrains.compose.material:material-icons-extended:1.5.11")
            implementation("org.jetbrains.compose.material:material-icons-extended-desktop:1.5.1")
            implementation(libs.exposed.core)
            implementation(libs.exposed.jdbc)
            implementation(libs.h2)
            implementation("org.xerial:sqlite-jdbc:3.49.1.0")

        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "org.dimi3.postalboy.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.dimi3.postalboy"
            packageVersion = "1.0.0"
        }

        buildTypes.release.proguard {
            isEnabled.set(false)
        }
    }
}