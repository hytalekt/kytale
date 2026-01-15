plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0"
    id("hytale-mod") version "0.+"
    `maven-publish`
}

group = "aster.amo.hexweave"
version = "1.4.4"
val javaVersion = 25

repositories {
    mavenCentral()
    maven("https://maven.hytale-modding.info/releases") {
        name = "HytaleModdingReleases"
    }
}

dependencies {
    api(project(":"))

    compileOnly(libs.jetbrains.annotations)
    compileOnly(libs.jspecify)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }

    withSourcesJar()
}

kotlin {
    jvmToolchain(javaVersion)
}

hytale { }

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
            groupId = "aster.amo"
            artifactId = "hexweave"
            version = project.version.toString()

            from(components["java"])

            pom {
                name.set("Hexweave")
                description.set("Helper layer for Kytale - player events, commands, tasks, and ECS systems")
                url.set("https://github.com/AmoAster/Kytale")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
            }
        }
    }

    repositories {
        mavenLocal()
        maven {
            name = "PokeSkies"
            url = uri("https://maven.pokeskies.com/releases")
            credentials {
                username = project.findProperty("pokeskiesUsername") as String? ?: System.getenv("POKESKIES_USERNAME")
                password = project.findProperty("pokeskiesPassword") as String? ?: System.getenv("POKESKIES_PASSWORD")
            }
            authentication {
                create("basic", BasicAuthentication::class.java)
            }
        }
    }
}
