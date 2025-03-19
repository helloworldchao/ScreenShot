import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.text.SimpleDateFormat
import java.util.*

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "tech.helloworldchao"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
}

val definedPackageVersion = "2.0.0"
val updateTime: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "screenshot"
            packageVersion = definedPackageVersion
        }
    }
}

// 创建生成版本类的任务
tasks.register("generateVersionClass") {
    val outputDir = project.layout.buildDirectory.dir("generated/sources/version/kotlin")
    outputs.dir(outputDir)

    doLast {
        val versionFile = File(outputDir.get().asFile, "Version.kt")
        versionFile.parentFile.mkdirs()
        versionFile.writeText(
            """
            object Version {
                const val PACKAGE_VERSION = "$definedPackageVersion"
                const val UPDATE_TIME = "$updateTime"
            }
            """.trimIndent()
        )
    }
}

// 将生成任务关联到编译流程
kotlin.sourceSets.getByName("main").kotlin.srcDir(tasks.named("generateVersionClass"))
