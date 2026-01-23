plugins {
    id("buildlogic.hytale")
    kotlin("plugin.serialization")
}

dependencies {
    compileOnly(files("../libs/HytaleServer.jar"))
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.bundles.test)
}

dokka {
    moduleName = "kytale-serialization"
}

tasks.withType<Test> {
    useJUnitPlatform()
    failOnNoDiscoveredTests = false
}
