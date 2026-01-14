# Kytale DSL Scaffold Overview

This document provides an overview of the Kotlin DSL scaffolds created for the Kytale framework.

## Created Scaffolds

### 1. Entity & NPC System (`entity/`)

#### `EntityDsl.kt`
- `EntityBuilder` - General entity creation with component support
- `LivingEntityBuilder` - Living entities with inventory and stats
- DSL functions: `entity()`, `livingEntity()`

#### `NpcDsl.kt`
- `NpcBuilder` - NPC creation with type IDs
- `NpcInteractionBuilder` - Configure NPC interactions (activate, attack)
- `NpcAiBuilder` - Configure NPC AI (wander, patrol, follow, flee)
- `NpcEquipmentBuilder` - Configure NPC equipment (armor, tools)
- DSL function: `npc()`

**Example:**
```kotlin
val merchant = npc(world) {
    typeId("hytale:village_merchant")
    position(100.0, 64.0, 100.0)
    name("Friendly Merchant")
    interaction {
        onActivate { player ->
            // Open shop UI
        }
    }
    ai {
        wander(radius = 10.0)
        faceNearestPlayer(distance = 5.0)
    }
}
```

---

### 2. Command System (`command/`)

#### `CommandDsl.kt`
- `CommandBuilder` - Main command builder with arguments and subcommands
- `ArgumentBuilder` - Fluent argument configuration (required, optional, flags)
- `CommandContext` - Type-safe command argument access
- `ArgumentTypes` - Standard argument type constants
- DSL function: `command()`

#### `SpecializedCommands.kt`
- `PlayerCommandBuilder` - Player-only commands
- `AsyncCommandBuilder` - Asynchronous commands
- `TargetPlayerCommandBuilder` - Commands that target other players
- DSL functions: `playerCommand()`, `asyncCommand()`, `targetPlayerCommand()`

**Example:**
```kotlin
val teleportCommand = command("teleport", "tp") {
    description("Teleport to a location or player")
    permission("myplugin.teleport")

    argument("target") {
        type(ArgumentTypes.PLAYER)
        required()
    }

    execute { context ->
        val target = context.get<Player>("target")
        // Handle teleportation
    }

    subcommand("here") {
        description("Teleport a player to your location")
        execute { /* ... */ }
    }
}
```

---

### 3. Item & Inventory System (`item/`)

#### `ItemDsl.kt`
- `ItemStackBuilder` - Create and configure ItemStacks
- `LoreBuilder` - Configure item lore with operator overloading
- `MetadataBuilder` - Configure item metadata with infix notation
- DSL functions: `itemStack()`

#### `InventoryDsl.kt`
- `InventoryBuilder` - Configure inventory sections
- `HotbarBuilder`, `StorageBuilder`, `ArmorBuilder`, etc. - Section-specific builders
- `TransactionBuilder` - Batch inventory operations
- Extension: `Inventory.configure()`, `Inventory.transaction()`

**Example:**
```kotlin
val sword = itemStack("hytale:iron_sword") {
    amount(1)
    durability(100)
    displayName("Legendary Sword")
    lore {
        +"A powerful blade"
        +"Forged in dragon fire"
    }
    metadata {
        "damage_bonus" to 5
        "enchantments" to listOf("sharpness", "fire_aspect")
    }
}
```

---

### 4. Event System (`event/`)

#### `EventDsl.kt`
- `EventRegistrationBuilder` - Register multiple event listeners
- `EventListenerBuilder` - Configure individual listeners with priority and filters
- `EventPriority` - Priority levels for event handling
- DSL function: `events()`
- Extensions for `CancellableEcsEvent`

#### `CommonEvents.kt`
- Convenience functions for common events:
  - `onBlockBreak()`, `onBlockPlace()`
  - `onPlayerJoin()`, `onPlayerQuit()`, `onPlayerChat()`
  - `onEntityDamage()`, `onEntityDeath()`
  - `onPlayerInteract()`, `onPlayerMove()`
  - `onInventoryClick()`

**Example:**
```kotlin
events {
    listen<PlayerJoinEvent> {
        priority = EventPriority.NORMAL
        handle { event ->
            event.player.sendMessage("Welcome!")
        }
    }

    on<BlockBreakEvent> { event ->
        if (event.blockType == "hytale:diamond_ore") {
            event.player.sendMessage("You found diamonds!")
        }
    }
}
```

---

### 5. World & Block System (`world/`)

#### `BlockDsl.kt`
- `SetBlockSettingsBuilder` - Configure block placement with flags
- `PlaceBlockSettingsBuilder` - Configure block placement settings
- `BreakBlockSettingsBuilder` - Configure block breaking
- `BlockRotation` - Enum for block rotations
- Extensions: `World.setBlock()`, `World.placeBlock()`, `World.breakBlock()`

#### `WorldDsl.kt`
- `WorldConfigBuilder` - Configure world creation
- `WorldGenerationBuilder` - Configure world generation settings
- `GameRulesBuilder` - Configure game rules with infix notation
- `Weather` - Weather types enum
- DSL function: `createWorld()`
- Extensions: `World.time`, `World.setWeather()`, etc.

**Example:**
```kotlin
world.setBlock(x, y, z, "hytale:stone") {
    noNotify()
    physics()
    performBlockUpdate()
}

val newWorld = createWorld("my_world") {
    seed(12345L)
    spawnPoint(0.0, 64.0, 0.0)
    gameRules {
        "pvp" to true
        "mobSpawning" to true
    }
}
```

---

### 6. Interaction System (`interaction/`)

#### `InteractionDsl.kt`
- `InteractionChainBuilder` - Build interaction chains with forking
- `ForkBuilder` - Conditional branches in interactions
- `InteractionAction` - Sealed class for different action types
- DSL function: `interaction()`

#### `InteractionConfiguration.kt`
- `InteractionConfigBuilder` - Configure object interactions
- `InteractionBuilder` - Individual interaction configuration
- `InteractionType` - Types of interactions (activate, attack, use)
- `InteractionPriority` - Priority levels
- DSL function: `interactions()`

**Example:**
```kotlin
val miningInteraction = interaction {
    cooldown(1000)

    action {
        playAnimation("mining")
    }

    delay(500)

    fork {
        condition { blockHealth <= 0 }
        action {
            breakBlock(targetBlock)
        }
    }
}
```

---

### 7. Extension Utilities (`extension/` & `util/`)

#### `PlayerExtensions.kt`
- Message sending: `sendMessage()`, `showTitle()`, `showActionBar()`
- Player actions: `kick()`, `teleport()`, `giveItem()`, `playSound()`
- Properties: `displayName`, `health`, `hunger`, `level`
- Game mode: `setGameMode()`

#### `EntityExtensions.kt`
- Position access: `position`, `x`, `y`, `z`
- Entity actions: `teleport()`, `remove()`, `distanceTo()`
- Physics: `applyVelocity()`, `knockback()`
- Utilities: `getNearbyEntities()`, `lookAt()`

#### `MessageExtensions.kt`
- `MessageBuilder` - DSL for formatted messages
- `TextComponentBuilder` - Text formatting (color, bold, italic, etc.)
- `Color` - Color enum
- DSL function: `message()`
- Extension: `String.toMessage()`

#### `CodecExtensions.kt`
- `BuilderCodecDsl` - Kotlin-friendly BuilderCodec API
- DSL function: `builderCodec()`

#### `SchedulerDsl.kt`
- `ScheduleBuilder` - Schedule delayed and repeating tasks
- `ScheduledTask` - Cancellable task handle
- DSL function: `schedule()`
- Shortcuts: `runLater()`, `runRepeating()`, `runAsync()`

**Example:**
```kotlin
player.sendMessage("Hello!")
player.teleport(100.0, 64.0, 100.0)

val msg = message {
    text("Hello ")
    text("World") {
        color(Color.RED)
        bold()
    }
}

schedule {
    delay(20) // 1 second
    repeat(100) // Every 5 seconds
    run {
        player.sendMessage("Repeating task")
    }
}
```

---

## Implementation Status

**All scaffolds are INCOMPLETE and require implementation.**

Each file contains:
- ✅ Complete DSL structure and API design
- ✅ Comprehensive documentation with examples
- ❌ Actual Hytale API integration (marked with `TODO`)
- ❌ Implementation of builders and functions

## Next Steps

To implement these scaffolds:

1. **Understand Hytale APIs** - Study the decompiled Hytale source in `libs/hytale-src/`
2. **Implement builders** - Replace `TODO()` with actual Hytale API calls
3. **Test each module** - Create test cases for each DSL
4. **Add missing features** - Expand based on additional Hytale APIs discovered
5. **Documentation** - Add KDoc comments and usage examples

## Architecture Patterns

These scaffolds follow several key patterns:

- **DSL Markers** - Each major DSL has a `@DslMarker` annotation to prevent scope confusion
- **Builder Pattern** - Builders accumulate configuration before final build
- **Extension Functions** - Kotlin extensions make Java APIs more idiomatic
- **Infix Notation** - Used for natural syntax (e.g., `"key" to value`)
- **Operator Overloading** - Used where it improves readability (e.g., `+"lore line"`)
- **Type Safety** - Inline reified generics for type-safe event handling
- **Fluent API** - Method chaining for readable configuration

## Inspiration

These scaffolds are inspired by:
- **KotStom** - Kotlin DSLs for Minestom
- **Hytale's Architecture** - Component-based entities, BuilderCodec pattern
- **Kotlin Best Practices** - Idiomatic Kotlin API design

---

**Created:** 2026-01-13
**Status:** Scaffold Complete, Implementation Pending
