# Kytale

Kytale is a library that provides many utilities for making using Kotlin with Hytale seamless.

While we wait for a Hytale server JAR, we have a few features on our roadmap that are being prototyped.

## Roadmap
- DSLs
    - UI DSL - Multiple prototypes are underway using some example UI code provided by user Ktar on X
    - Unknown - We're still waiting for more information from Hytale, but we'll add more as we can
- kotlinx.coroutines support - When Hytale releases, we'll evaluate if we need anything to better support coroutines
- kotlinx.serialization support - (de)serializers for Hytale types to provide integration with kotlinx.serialization

Kytale will be published on Maven Central under
- `io.github.hytalekt:kytale:version`
- `io.github.hytalekt:kytale-coroutines:version`
- `io.github.hytalekt:kytale-serialization:version`

This should make it so that if you don't need support for serialization/coroutines, you won't have to include it.

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for information on contributing to Kytale.

## License
This project is licensed under the MIT License. See [LICENSE.txt](../LICENSE.txt) for more information.