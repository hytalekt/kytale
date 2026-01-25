plugins {
    id("buildlogic.common")
    kotlin("plugin.serialization")
    `maven-publish`
}

dependencies {
    compileOnly(libs.hytale)
    api(libs.kotlinx.serialization.core)

    testImplementation(libs.kotlinx.serialization.json)
    testImplementation(libs.hytale)
    testImplementation(libs.bundles.test)
}

publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            artifactId = "kytale-serialization"

            pom {
                name = "Kytale Serialization"
                description = "kotlinx.serialization serializers/utilities for Hytale"
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
    moduleName = "kytale-serialization"
}

tasks.withType<Test> {
    useJUnitPlatform()
    failOnNoDiscoveredTests = false
}
