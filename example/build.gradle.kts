plugins {
    id("buildlogic.common")
}

dependencies {
    compileOnly(files("../libs/HytaleServer.jar"))
    implementation(project(":"))
    implementation(project(":coroutines"))
    implementation(project(":serialization"))

    testImplementation(libs.bundles.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
    failOnNoDiscoveredTests = false
}
