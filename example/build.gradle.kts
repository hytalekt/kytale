plugins {
    id("buildlogic.hytale")
}

dependencies {
    implementation(project(":"))
    implementation(project(":coroutines"))
    implementation(project(":serialization"))

    testImplementation(libs.bundles.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
    failOnNoDiscoveredTests = false
}
