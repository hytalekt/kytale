plugins {
    id("buildlogic.common")
    kotlin("plugin.serialization")
}

dependencies {
    compileOnly(libs.hytale)
    api(libs.kotlinx.serialization.core)

    testImplementation(libs.kotlinx.serialization.json)
    testImplementation(libs.hytale)
    testImplementation(libs.bundles.test)
}

dokka {
    moduleName = "kytale-serialization"
}

tasks.withType<Test> {
    useJUnitPlatform()
    failOnNoDiscoveredTests = false
}
