plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(libs.plugin.kotlin)

    // temporary fix for `alias` not resolving in build scripts
    // TODO: after upgrading Gradle to a version that uses Kotlin 2.3.0, use alias
    implementation(libs.plugin.shadow)
    implementation(libs.plugin.dokka)
    implementation(libs.plugin.jreleaser)
    implementation(libs.plugin.kotlinx.serialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

kotlin {
    jvmToolchain(25)
}
