# Kytale

---

A Kotlin DSL wrapper for the Hytale server API, providing type-safe builders and idiomatic Kotlin extensions for plugin development.

### Adding as a dependency (Gradle)

Currently, Kytale is not published to Maven Central. To use it locally, you can publish to your local Maven repository:

```bash
./gradlew publishToMavenLocal
```

Then add the dependency to your `build.gradle.kts`:

```kotlin
repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("io.github.hytalekt:kytale:<version>")
    compileOnly(files("path/to/HytaleServer.jar"))
}
```

## Feature Overview

### Kytale

```kotlin
implementation("io.github.hytalekt:kytale:<version>")
```

- Hytale Extensions
    - Entity extensions (`Entity#teleport`, component helpers)
    - Player extensions (inventory helpers, messaging shortcuts)
    - Logger extensions (type-safe logging with Flogger API)
    - Codec extensions (builder helpers for serialization)
- DSLs
    - Command (`command`) - Type-safe command builder with arguments and subcommands
    - Entity (`entity`, `npc`) - Entity and NPC creation with AI, interactions, and equipment
    - Item (`itemStack`) - ItemStack builder with lore and metadata
    - Inventory (`inventory`) - Inventory creation and management
    - Event (`events`, `listen`) - Event listener registration with priorities and filters
    - Block (`setBlock`) - Block operations with fluent settings
    - World (`world`) - World creation and configuration
    - Interaction (`interaction`) - NPC and entity interaction handlers
    - Scheduler (`schedule`, `scheduleRepeating`) - Task scheduling with delays
    - UI (`ui`) - Custom UI page creation with Noesis integration
- Plugin Base
    - Simplified plugin lifecycle
    - Access to server managers and registries

### DSL Examples

#### Commands
```kotlin
val giveCommand = command("give") {
    description("Give yourself an item")
    permission("example.give")

    argument("item") {
        type(ArgumentTypes.ITEM)
        required()
    }

    argument("amount") {
        type(ArgumentTypes.INTEGER)
        optional(1)
    }

    execute {
        val itemId = get<String>("item")
        val amount = getOptional<Int>("amount") ?: 1
        // Give item to player
    }
}
```

#### NPCs
```kotlin
val merchant = npc(world) {
    typeId("hytale:village_merchant")
    position(x, y, z)
    name("Friendly Merchant")

    interaction {
        onActivate { player ->
            player.sendMessage("Welcome to my shop!")
        }
        distance(5.0)
    }

    ai {
        wander(radius = 10.0, speed = 1.0)
        faceNearestPlayer(distance = 5.0)
    }

    equipment {
        mainHand("hytale:merchant_staff")
        chestplate("hytale:merchant_robe")
    }
}
```

#### Items
```kotlin
val legendaryItem = itemStack("hytale:iron_sword") {
    amount(1)
    durability(1000)
    displayName("§6§lLegendary Blade")

    lore {
        +"§7A blade forged in dragon fire"
        +"§c+15 Attack Damage"
        +"§9+10% Critical Strike Chance"
    }

    metadata {
        "damage_bonus" to 15
        "crit_chance" to 0.10
        "is_legendary" to true
    }
}
```

#### Events
```kotlin
events {
    onPlayerJoin { event ->
        println("Player joined!")
    }

    listen<BlockBreakEvent> {
        priority = EventPriority.HIGH
        ignoreCancelled = true

        filter { event ->
            // Only handle diamond ore
            event.block.id == "hytale:diamond_ore"
        }

        handle { event ->
            println("Player broke diamond ore!")
        }
    }
}
```

### For more information, view [the example](example/src/main/kotlin)
