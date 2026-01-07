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
}

// TODO: after upgrading Gradle to a version that uses embedded Kotlin 2.3.0, use Java 25
java {
    sourceCompatibility = JavaVersion.VERSION_24
    targetCompatibility = JavaVersion.VERSION_24

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
}

kotlin {
    jvmToolchain(24)
}
