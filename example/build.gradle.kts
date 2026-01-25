plugins {
    id("buildlogic.common")
}

dependencies {
    implementation(libs.hytale)
    implementation(libs.kotlinx.serialization.json)
    implementation(project(":"))
    implementation(project(":coroutines"))
    implementation(project(":serialization"))

    testImplementation(libs.bundles.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
    failOnNoDiscoveredTests = false
}
