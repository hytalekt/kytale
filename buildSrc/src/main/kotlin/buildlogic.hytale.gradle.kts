plugins {
    id("buildlogic.common")
}

val isGitHubActions = providers.environmentVariable("GITHUB_ACTIONS").isPresent

repositories {
    if (isGitHubActions) {
        maven("https://stubs.oglass.dev/repository")
    }
}

// TODO: replace with proper Hytale dependency when Hypixel publishes it
dependencies {
    if (isGitHubActions) {
        compileOnly("io.github.hytalekt.stubs:hytale-stubs:0.1.0-alpha.0")
    } else {
        compileOnly(files(rootDir.resolve("libs/HytaleServer.jar")))
    }
}
