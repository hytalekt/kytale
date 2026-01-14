plugins {
    id("buildlogic.common")
}

dependencies {
    implementation(project(":"))
    implementation(files("../libs/HytaleServer.jar"))
}
