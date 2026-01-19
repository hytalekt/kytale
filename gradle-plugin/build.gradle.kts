plugins {
    kotlin("jvm") version "2.1.20" // Supports Java 24+
    `java-gradle-plugin`
    `maven-publish`
}

group = "io.github.hytalekt"
version = "0.1.0-alpha.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
}

gradlePlugin {
    plugins {
        create("kytale") {
            id = "io.github.hytalekt.kytale"
            implementationClass = "io.github.hytalekt.kytale.gradle.KytalePlugin"
            displayName = "Kytale Plugin"
            description = "Gradle plugin for Hytale mod development - provides runServer and installPlugin tasks"
        }
        create("kytaleUi") {
            id = "io.github.hytalekt.kytale.ui"
            implementationClass = "io.github.hytalekt.kytale.gradle.KytaleUiPlugin"
            displayName = "Kytale UI Plugin"
            description = "Gradle plugin for compiling Kytale UI DSL definitions to .ui files"
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21) // Gradle 9.x compatible version
    }
}

kotlin {
    jvmToolchain(21)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.hytalekt"
            artifactId = "kytale-ui-gradle-plugin"
            version = project.version.toString()

            from(components["java"])

            pom {
                name.set("Kytale UI Gradle Plugin")
                description.set("Gradle plugin for compiling Kytale UI DSL definitions to .ui files")
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
