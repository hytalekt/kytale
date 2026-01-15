plugins {
    id("buildlogic.common")
}

dependencies {
    compileOnly(files("../libs/HytaleServer.jar"))
    compileOnly(libs.kotlinx.coroutines.core)
    compileOnly(project(":"))

    testImplementation(libs.bundles.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
    failOnNoDiscoveredTests = false
}
