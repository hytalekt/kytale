plugins {
    id("buildlogic.common")
    kotlin("plugin.serialization")
}

dependencies {
    compileOnly(libs.hytale.stubs)

    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.bundles.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
    failOnNoDiscoveredTests = false
}
