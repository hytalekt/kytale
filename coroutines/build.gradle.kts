plugins {
    id("buildlogic.common")
}

dependencies {
    compileOnly(libs.hytale.stubs)
    compileOnly(libs.kotlinx.coroutines.core)

    testImplementation(libs.bundles.test)
    testImplementation(libs.kotlinx.coroutines.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
    failOnNoDiscoveredTests = false
}
