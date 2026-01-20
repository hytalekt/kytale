plugins {
    id("buildlogic.common")
    kotlin("plugin.serialization")
    `maven-publish`
}

dependencies {
    api(project(":"))

    compileOnly(files("../libs/HytaleServer.jar"))

    compileOnly(libs.jetbrains.annotations)
    compileOnly(libs.jspecify)

    testImplementation(files("../libs/HytaleServer.jar"))
    testImplementation(libs.bundles.test)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
    failOnNoDiscoveredTests = false
}

tasks.withType<Jar> {
    archiveBaseName.set("hexweave")
    manifest {
        attributes["Specification-Title"] = "Hexweave"
        attributes["Specification-Version"] = version
        attributes["Implementation-Title"] = "Hexweave"
        attributes["Implementation-Version"] = version.toString()
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.hytalekt"
            artifactId = "kytale-hexweave"
            version = project.version.toString()

            from(components["java"])

            pom {
                name.set("Kytale Hexweave")
                description.set("Helper layer for Kytale - player events, commands, tasks, and ECS systems")
                url.set("https://github.com/hytalekt/kytale")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("hytalekt")
                        name.set("HytaleKT")
                    }
                    developer {
                        id.set("amoaster")
                        name.set("AmoAster")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/hytalekt/kytale.git")
                    developerConnection.set("scm:git:ssh://github.com/hytalekt/kytale.git")
                    url.set("https://github.com/hytalekt/kytale")
                }
            }
        }
    }

    repositories {
        mavenLocal()
    }
}
