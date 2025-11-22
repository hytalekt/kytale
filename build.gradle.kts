import org.jreleaser.model.Active

plugins {
    id("buildlogic.common")
    `maven-publish`
    id("org.jreleaser")
}

dependencies {
    testImplementation(libs.bundles.test)
}

tasks.withType<Test> { useJUnitPlatform() }

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

tasks.assemble {
    doFirst {
        mkdir(
            rootProject.layout.buildDirectory
                .dir("jreleaser"),
        )
    }
}

jreleaser {
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
