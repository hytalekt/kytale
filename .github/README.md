# Kytale

Kytale provides extension functions, DSLs, and other utilities for developing Hytale server plugins using Kotlin. The goal of the project is to stay close to Hytale's API but to make it feel more native to Kotlin.

[![Documentation](https://img.shields.io/badge/docs-kdoc-blue)](https://hytalekt.github.io/kytale/)
[![License](https://img.shields.io/badge/license-MIT-green)](LICENSE)

## Module Overview

### Main Module (`io.github.hytalekt:kytale`)

The core `kytale` module provides:

- **DSLs**
  - Command DSL
  - Codec DSL
- **Message Utilities**
  - Raw styled messages with `text()`
  - Internationalization with `i18n()`
  - Message composition and formatting helpers
- **Extensions**
  - Logger extensions (`atLevelOrNull(Level)`, `at<level>OrNull()`)
  - Vector extensions
    - Destructuring (`componentN()`)
    - Operator functions

### Additional Modules

#### Serialization (`io.github.hytalekt:kytale-serialization`)

Provides `kotlinx-serialization` support for Hytale types:

- **Vector types**: Vector3d, Vector3i, Vector3f, Vector2d, Vector2i, Vector3l, Vector2l, Vector4d, Vec2f, Vec3f, Vec4f
- **Transform types**: Transform, Location
- **Math types**: Quatf (quaternion), Mat4f (4x4 matrix)
- **Range types**: IntRange, FloatRange
- **Shape types**: Box, Box2D, Ellipsoid, Cylinder
- **Component types**: WorldGenId, NetworkId, UUIDComponent
- **Player data types**: PlayerDeathPositionData, PlayerRespawnPointData
- **Version types**: Semver

There are two ways to use these serializers
1. Using the annotated typealiases (Recommended)
   - For each type (e.g. Vector3d, Vector3i), we provide a typealias (e.g. KVector3d, KVector3i) that is annotated with `@Serializable(with = <serializer>)`
   - An object of the original type can still be passed in parameters that use one of those typealiases, so for example a data class that has a KVector3d field can be constructed using a Vector3d, since it's just a typealias.
2. Using the contextual SerializerModule
   - `KytaleSerializersModule` provides all the serializers for the types contextually
   - This is not recommended because of the runtime costs introduced

#### Coroutines (`io.github.hytalekt:kytale-coroutines`)

Currently, there aren't many utilities for coroutines as we explore Hytale's internals more, but we have a coroutine executor extension for the command DSL.

```kotlin
command("async", "Async command") {
    executorCoroutine {
        delay(1000)
        context.sendMessage(text("Done!"))
    }
}
```

## Quick Start

### Installation

#### Gradle (Kotlin DSL)

```kotlin
plugins {
    id("com.gradleup.shadow") version "9.3.1" // required to shade 
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.hytalekt:kytale:VERSION")

    // Optional modules
    implementation("io.github.hytalekt:kytale-serialization:VERSION")
    implementation("io.github.hytalekt:kytale-coroutines:VERSION")
}

tasks.shadowJar {
    // Relocate Kytale
    relocate("io.github.hytalekt.kytale", "your.plugin.package.kytale")

    // Include dependencies
    configurations = listOf(project.configurations.runtimeClasspath.get())

    archiveClassifier.set("")
}

tasks.build {
    dependsOn("shadowJar")
}
```

#### Gradle (Groovy)

```kotlin
plugins {
    id 'com.gradleup.shadow' version '9.3.1'
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'io.github.hytalekt:kytale:VERSION'

    // Optional modules
    implementation 'io.github.hytalekt:kytale-serialization:VERSION'
    implementation 'io.github.hytalekt:kytale-coroutines:VERSION'
}

shadowJar {
    // Relocate Kytale to avoid conflicts
    relocate 'io.github.hytalekt.kytale', 'your.plugin.package.kytale'

    configurations = [project.configurations.runtimeClasspath]

    archiveClassifier = ''
}

build.dependsOn shadowJar
```

## Documentation

Full API documentation is available at: **[https://hytalekt.github.io/kytale/](https://hytalekt.github.io/kytale/)**

## Roadmap

- **Kytale Hytale Plugin**: A shared Hytale plugin that provides Kytale as a common dependency for multiple plugins is coming soon. This will eliminate the need for shading once installed on your server.

## License

Kytale is licensed under the [MIT License](LICENSE).

## Contributing

Contributions are welcome! Read [CONTRIBUTING.md](CONTRIBUTING.md) for more information.
