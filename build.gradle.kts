import org.jreleaser.model.Active

plugins {
    id("buildlogic.common")
    id("org.jreleaser")
    id("org.jetbrains.dokka")
    `maven-publish`
}

dependencies {
    compileOnly(libs.hytale)

    testImplementation(libs.hytale)
    testImplementation(libs.bundles.test)

    dokka(project(":"))
    dokka(project(":serialization"))
    dokka(project(":coroutines"))
}

tasks.withType<Test> {
    useJUnitPlatform()
    failOnNoDiscoveredTests = false
    systemProperty("java.util.logging.manager", "com.hypixel.hytale.logger.backend.HytaleLogManager")
}

java { withSourcesJar() }

publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            artifactId = "kytale"

            pom {
                name = "Kytale"
                description = "A Kotlin framework for Hytale server plugins"
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

tasks.register("prepareJReleaser") {
    val jreleaserDir = layout.buildDirectory.dir("jreleaser")
    outputs.dir(jreleaserDir)
    doLast {
        jreleaserDir.get().asFile.mkdirs()
    }
}

tasks.assemble {
    dependsOn("prepareJReleaser")
}

jreleaser {
    release {
        github {
            repoOwner = "hytalekt"
            name = "kytale"
            releaseName = "Kytale {{tagName}}"
            overwrite = true

            releaseNotes.enabled = true
            changelog.enabled = false

            prerelease {
                enabled = true
                pattern = ".*-(beta|alpha|rc).*"
            }
        }
    }

    signing {
        active = Active.ALWAYS
        armored = true
    }

    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    active = Active.ALWAYS
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository("build/staging-deploy")
                }
            }
        }
    }
}
