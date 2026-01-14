# Kytale Example Plugin

This is a comprehensive example plugin demonstrating all features of the Kytale DSL framework.

## 📦 What's Included

This example plugin showcases:

- ✅ **Command System** - Fluent command registration with arguments, permissions, and subcommands
- ✅ **Event Handling** - Type-safe event listeners with priorities and filters
- ✅ **NPC Management** - Creating and managing NPCs with AI, interactions, and equipment
- ✅ **Item System** - Creating custom items with lore, metadata, and special properties
- ✅ **Inventory Management** - Configuring and manipulating player inventories
- ✅ **World Manipulation** - Block operations and world configuration
- ✅ **Interaction Chains** - Complex interaction sequences with forking and delays
- ✅ **UI Building** - Creating custom UIs with the Kytale UI DSL
- ✅ **Scheduled Tasks** - Delayed and repeating tasks with the scheduler DSL

## 📁 Project Structure

```
example/
├── src/main/kotlin/io/github/hytalekt/kytale/example/
│   ├── ExamplePlugin.kt              # Main plugin class
│   ├── commands/
│   │   └── Commands.kt               # Command examples
│   ├── events/
│   │   └── Events.kt                 # Event listener examples
│   └── features/
│       ├── CustomNpcManager.kt       # NPC creation examples
│       ├── MagicItemsManager.kt      # Custom item examples
│       ├── WorldManipulation.kt      # World/block operation examples
│       ├── InteractionExamples.kt    # Interaction chain examples
│       ├── InventoryExamples.kt      # Inventory management examples
│       ├── ScheduledTaskExamples.kt  # Scheduled task examples
│       └── UiExamples.kt             # UI building examples
└── build.gradle.kts                  # Build configuration
```

## 🚀 Getting Started

### Prerequisites

- JDK 25 or higher
- Hytale Server JAR (`HytaleServer.jar` in `../libs/`)

### Building

```bash
./gradlew :example:build
```

### Running

1. Build the example plugin
2. Copy the generated JAR to your Hytale server's plugins folder
3. Start the Hytale server
4. The plugin will register all example commands and event listeners

## 📖 Example Usage

### Commands

```
/give <item> [amount]           - Give yourself an item
/heal                           - Heal yourself
/admin reload                   - Reload the plugin
/admin spawn <type>             - Spawn an NPC
/teleport <x> <y> <z>          - Teleport to coordinates
/calculate [iterations]         - Perform heavy calculation (async)
```

### Event Handling

The plugin automatically registers listeners for:
- Player join/quit events
- Block break/place events
- Entity damage events
- Chat events
- Inventory interactions
- Player movement

### Creating NPCs

```kotlin
// Spawn a merchant NPC
npcManager.spawnMerchantNpc(100.0, 64.0, 100.0)

// Spawn a guard NPC that patrols
npcManager.spawnGuardNpc(150.0, 64.0, 150.0)

// Spawn a quest giver
npcManager.spawnQuestGiverNpc(200.0, 64.0, 200.0)
```

### Creating Custom Items

```kotlin
// Create a legendary sword
val sword = itemsManager.createLegendarySword()

// Create a magic staff
val staff = itemsManager.createMagicStaff()

// Create healing potions
val potions = itemsManager.createHealingPotions()
```

### World Manipulation

```kotlin
// Build a simple house
WorldManipulation.buildSimpleHouse(world, 0, 64, 0)

// Create a parkour course
WorldManipulation.createParkourCourse(world, 100, 70, 100)

// Create a magic zone
WorldManipulation.createMagicZone(world, 200, 64, 200, radius = 10)
```

### Interaction Chains

```kotlin
// Mining interaction with progressive damage
val mining = InteractionExamples.createMiningInteraction()

// Spell casting with charging
val spell = InteractionExamples.createSpellCastInteraction()

// Fishing with catches
val fishing = InteractionExamples.createFishingInteraction()
```

### Scheduled Tasks

```kotlin
// Schedule a delayed message
ScheduledTaskExamples.scheduleDelayedMessage()

// Create a repeating announcement
val task = ScheduledTaskExamples.scheduleRepeatingAnnouncement()

// Start auto-save system
val autoSave = ScheduledTaskExamples.startAutoSave()
```

## 🎯 Key Features Demonstrated

### 1. Command DSL

Fluent command registration with full type safety:

```kotlin
val command = command("give") {
    description("Give yourself an item")
    permission("example.give")

    argument("item") {
        type(ArgumentTypes.ITEM)
        required()
        suggestions("hytale:sword", "hytale:pickaxe")
    }

    execute { context ->
        val item = context.get<String>("item")
        // Handle command
    }
}
```

### 2. Event System

Type-safe event listeners with filters and priorities:

```kotlin
events {
    listen<BlockBreakEvent> {
        priority = EventPriority.HIGH
        filter { it.blockType == "hytale:diamond_ore" }

        handle { event ->
            event.player.sendMessage("You found diamonds!")
        }
    }
}
```

### 3. NPC Builder

Comprehensive NPC creation with AI and interactions:

```kotlin
val merchant = npc(world) {
    typeId("hytale:merchant")
    position(x, y, z)

    interaction {
        onActivate { player ->
            // Open shop
        }
    }

    ai {
        wander(radius = 10.0)
        faceNearestPlayer(distance = 5.0)
    }
}
```

### 4. Item Creation

Rich item creation with metadata and lore:

```kotlin
val sword = itemStack("hytale:iron_sword") {
    displayName("§6Legendary Blade")
    lore {
        +"A blade forged in dragon fire"
        +"§c+15 Attack Damage"
    }
    metadata {
        "damage_bonus" to 15
        "enchantments" to listOf("sharpness:5")
    }
}
```

### 5. UI Building

Declarative UI construction:

```kotlin
class ShopUI(playerRef: PlayerRef) : KytaleUI<ShopEventData>(...) {
    override fun UIBuilder.buildUI() {
        "#Title" set "Magic Shop"

        onActivate("#BuyButton") {
            // Handle purchase
        }

        dropdown("#Quality") {
            entry("Low", "low")
            entry("High", "high")
        }
    }
}
```

## ⚠️ Important Notes

### Implementation Status

**All examples are SCAFFOLDS** - they demonstrate the DSL API but are not fully implemented:

- ✅ DSL structure and API design is complete
- ✅ Documentation and examples are comprehensive
- ❌ Actual Hytale API integration is marked with `TODO` comments
- ❌ Most functions need implementation

### Next Steps

To make these examples functional:

1. Replace `TODO` comments with actual Hytale API calls
2. Implement the builder `build()` methods
3. Connect to Hytale's event system
4. Test each feature with a real Hytale server

## 📚 Further Reading

- [Kytale DSL Overview](../SCAFFOLD_OVERVIEW.md) - Complete API documentation
- Main Kytale Library - Core DSL implementations

## 🤝 Contributing

Feel free to expand these examples or add new ones! The scaffold provides a solid foundation for demonstrating Kytale's capabilities.

---

**Note:** This is an example/demonstration project. The actual implementation will require access to a running Hytale server and proper API integration.
