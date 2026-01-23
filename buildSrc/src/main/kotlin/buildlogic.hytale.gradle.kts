plugins {
    id("buildlogic.common")
}

val hytaleDependency =
    (
        project.findProperty("hytale_path") as String? ?: run {
            val os =
                org.gradle.internal.os.OperatingSystem
                    .current()

            val patchline = project.findProperty("hytale_patchline") as String? ?: "release"

            when {
                os.isWindows -> {
                    "${System.getProperty("user.home")}/AppData/Roaming/Hytale"
                }

                os.isMacOsX -> {
                    "${System.getProperty("user.home")}/Library/Application Support/Hytale"
                }

                os.isLinux -> {
                    "${System.getProperty("user.home")}/.local/share/Hytale"
                }

                else -> {
                    null
                }
            }?.plus("/install/$patchline/package/game/latest/Server/HytaleServer.jar")
        }
    )?.let {
        val jar = File(it)
        if (!jar.exists()) {
            logger.warn("Local Hytale JAR not found! Falling back to stubs")
        }
        files(jar)
    } ?: "io.github.hytalekt.stubs:hytale-stubs:0.1.0-alpha.0"

repositories {
    maven("https://stubs.oglass.dev/repository")
}

dependencies {
    compileOnly(hytaleDependency)
}
