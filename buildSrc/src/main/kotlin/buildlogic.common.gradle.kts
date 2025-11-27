plugins {
    kotlin("jvm")
}

group = "io.github.hytalekt"
version = "0.1.0-alpha.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

// TODO: switch to JVM 25 on Kotlin 2.3.0 release
java {
    withJavadocJar()
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_24
    targetCompatibility = JavaVersion.VERSION_24

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
}

kotlin {
    jvmToolchain(24)
}
