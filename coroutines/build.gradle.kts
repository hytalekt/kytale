plugins {
    id("buildlogic.common")
}

dependencies {
    compileOnly(libs.hytale)
    compileOnly(libs.kotlinx.coroutines.core)
    compileOnly(project(":"))

    testImplementation(libs.hytale)
    testImplementation(libs.bundles.test)
    testImplementation(libs.kotlinx.coroutines.core)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    testImplementation(project(":"))
}

dokka {
    moduleName = "kytale-coroutines"
}

tasks.withType<Test> {
    useJUnitPlatform()
    failOnNoDiscoveredTests = false
    systemProperty("java.util.logging.manager", "com.hypixel.hytale.logger.backend.HytaleLogManager")
}
