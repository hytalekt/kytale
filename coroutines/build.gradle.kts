plugins {
    id("buildlogic.common")
    `maven-publish`
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

publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            artifactId = "kytale-coroutines"

            pom {
                name = "Kytale Coroutines"
                description = "kotlinx.coroutines extensions for Kytale"
                url = "https://github.com/hytalekt/kytale"

                licenses {
                    license {
                        name = "MIT License"
                        url = "http://www.opensource.org/licenses/mit-license.php"
                    }
                }

                developers {
                    developer {
                        id = "oglass"
                        name = "oglass"
                        email = "him@oglass.dev"
                    }
                }

                issueManagement {
                    system = "GitHub"
                    url = "https://github.com/hytalekt/kytale/issues"
                }

                scm {
                    connection = "scm:git:git:github.com/hytalekt/kytale.git"
                    developerConnection = "scm:git:https://github.com/hytalekt/kytale.git"
                    url = "https://github.com/hytalekt/kytale"
                }
            }
        }

        repositories {
            maven {
                url =
                    rootProject.layout.buildDirectory
                        .dir("staging-deploy")
                        .get()
                        .asFile
                        .toURI()
            }
        }
    }
}

dokka {
    moduleName = "kytale-coroutines"
}

tasks.withType<Test> {
    useJUnitPlatform()
    failOnNoDiscoveredTests = false
    systemProperty("java.util.logging.manager", "com.hypixel.hytale.logger.backend.HytaleLogManager")
}
