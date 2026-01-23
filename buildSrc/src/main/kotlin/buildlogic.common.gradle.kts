plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
}

group = "io.github.hytalekt"
version = "0.1.0-alpha.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

java {
    withJavadocJar()
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

kotlin {
    jvmToolchain(25)
}
