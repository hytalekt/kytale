plugins {
    kotlin("jvm") version "2.2.0"
    `java-gradle-plugin`
    `maven-publish`
}

group = "aster.amo"
version = "1.4.4"

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
        create("kytaleUi") {
            id = "aster.amo.kytale.ui"
            implementationClass = "aster.amo.kytale.gradle.KytaleUiPlugin"
            displayName = "Kytale UI Plugin"
            description = "Gradle plugin for compiling Kytale UI DSL definitions to .ui files"
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17) // Gradle compatible version
    }
}

kotlin {
    jvmToolchain(17)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "aster.amo"
            artifactId = "kytale-ui-gradle-plugin"
            version = project.version.toString()

            from(components["java"])

            pom {
                name.set("Kytale UI Gradle Plugin")
                description.set("Gradle plugin for compiling Kytale UI DSL definitions to .ui files")
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
