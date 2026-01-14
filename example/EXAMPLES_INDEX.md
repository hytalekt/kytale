# Kytale Example Plugin - Code Index

Quick reference guide to all examples in this plugin.

## 📋 Command Examples

**File:** `commands/Commands.kt`

### Basic Commands
- `giveCommand` - Give items with amount parameter
- `healCommand` - Player-only heal command
- `teleportCommand` - Teleport with coordinates and player targeting

### Advanced Commands
- `adminCommand` - Command with multiple subcommands (reload, debug, spawn)
- `calculateCommand` - Async command for heavy operations

**Key Features Demonstrated:**
- Required and optional arguments
- Type-specific arguments (ITEM, INTEGER, DOUBLE, PLAYER)
- Tab completion suggestions
- Permission checks
- Subcommand hierarchies
- Async execution

---

## 🎯 Event Examples

**File:** `events/Events.kt`

### Event Types Covered
- Player events: Join, quit, chat, move
- Block events: Break, place
- Entity events: Damage, death
- Inventory events: Click
- World events: Load, unload

**Key Features Demonstrated:**
- Event priorities (LOWEST to MONITOR)
- Event filtering with predicates
- Event cancellation
- Ignore cancelled events
- Multiple listeners for same event
- Shorthand event registration

---

## 🤖 NPC Examples

**File:** `features/CustomNpcManager.kt`

### NPC Types
1. **Merchant NPC** - Wandering merchant with shop interaction
2. **Guard NPC** - Patrol guard with equipment
3. **Quest Giver** - Stationary NPC with quest interactions
4. **Hostile NPC** - Enemy that follows and attacks players

**Key Features Demonstrated:**
- NPC type IDs and positioning
- Custom names and nameplates
- Interaction handlers (activate, attack)
- AI behaviors:
  - Wandering with radius
  - Facing nearest player
  - Patrolling waypoints
  - Following targets
  - Fleeing from danger
- Equipment configuration (weapons, armor)

---

## 🎒 Item Examples

**File:** `features/MagicItemsManager.kt`

### Custom Items
1. **Legendary Sword** - High-damage weapon with enchantments
2. **Magic Staff** - Spell-casting item with mana bonuses
3. **Healing Potions** - Consumable health restoration
4. **Teleport Scroll** - Rechargeable teleportation item
5. **Quest Artifact** - Soulbound quest item
6. **Magic Armor** - Stat-boosting armor piece
7. **Enchanted Feast** - Food with temporary buffs
8. **Dwarven Excavator** - Area-mining pickaxe

**Key Features Demonstrated:**
- Display names with color codes
- Multi-line lore with unary plus operator
- Metadata storage:
  - Numeric values
  - Boolean flags
  - String arrays
  - Nested objects
- Item amounts and durability
- Quest item properties
- Effect application

---

## 🌍 World Examples

**File:** `features/WorldManipulation.kt`

### World Operations
1. **createArenaWorld()** - Custom PvP world with flat generation
2. **buildSimpleHouse()** - Automated structure building
3. **createMagicZone()** - Circular area with special blocks
4. **demolishStructure()** - Batch block breaking
5. **createParkourCourse()** - Sequential platform generation
6. **demonstrateWorldControl()** - Time and weather manipulation

**Key Features Demonstrated:**
- World creation and configuration
- Seed and spawn point setting
- World generation settings
- Game rule configuration
- Block placement with flags:
  - Physics simulation
  - Particle effects
  - Update notifications
  - Neighbor connections
- Block rotation
- Batch operations
- Time and weather control

---

## 🔗 Interaction Examples

**File:** `features/InteractionExamples.kt`

### Interaction Chains
1. **Mining** - Progressive block damage with animation
2. **Spell Casting** - Charging mechanic with power scaling
3. **Cooking** - Success/failure paths with burn chance
4. **Fishing** - Random wait time with catch window
5. **Door** - Key check with lock-picking attempt
6. **Crafting Table** - Simple UI opening
7. **Magic Altar** - Multi-interaction configuration

**Key Features Demonstrated:**
- Interaction cooldowns
- Sequential actions with delays
- Forking based on conditions
- Context variable storage
- Chain repetition
- Interaction cancellation
- Multiple interaction types:
  - Activate
  - Right-click
  - Attack
  - Use item
- Distance and priority settings
- Required items

---

## 📦 Inventory Examples

**File:** `features/InventoryExamples.kt`

### Inventory Operations
1. **setupStarterInventory()** - Configure hotbar, storage, and armor
2. **organizeInventory()** - Sort and clean inventory
3. **createShopInventory()** - Custom vendor inventory
4. **performBatchOperations()** - Transaction-based changes
5. **checkInventoryContents()** - Item presence checking
6. **createBackpack()** - Nested container item
7. **fillChestWithLoot()** - Loot table implementation
8. **equipArmorSet()** - Full armor equipping

**Key Features Demonstrated:**
- Hotbar slot assignment
- Storage management
- Armor configuration
- Inventory transactions:
  - Move operations
  - Swap operations
  - Clear operations
  - Atomic commits
- Item checking and removal
- Custom container items
- Inventory sorting

---

## ⏰ Scheduler Examples

**File:** `features/ScheduledTaskExamples.kt`

### Task Types
1. **Delayed Messages** - Simple one-time delay
2. **Repeating Announcements** - Periodic broadcasts
3. **Async Operations** - Database saves off-thread
4. **Countdown Timer** - Dynamic repeating task
5. **Temporary Effects** - Time-limited buffs
6. **Auto-save System** - Regular world saves
7. **Event Sequences** - Chained timed events
8. **Particle Effects** - Visual effect loops
9. **Boss Spawns** - Scheduled encounters
10. **Health Regeneration** - Gradual healing

**Key Features Demonstrated:**
- Delay before execution
- Repeating intervals
- Async execution
- Task cancellation
- Sequential task chains
- Parallel task execution
- Tick-based timing (20 ticks = 1 second)
- Second-based shortcuts

---

## 🎨 UI Examples

**File:** `features/UiExamples.kt`

### UI Types
1. **WelcomeMenuUI** - Simple menu with buttons
2. **ShopUI** - Item display with purchases
3. **QuestLogUI** - Quest list with progress
4. **SettingsUI** - Settings with dropdowns and sliders
5. **CraftingUI** - Recipe display with materials
6. **ConfirmationDialogUI** - Yes/no dialog with callbacks

**Key Features Demonstrated:**
- Custom UI inheritance from `KytaleUI`
- Property setting with infix notation
- Event binding:
  - onActivate (button clicks)
  - onValueChange (input changes)
  - onDismiss (close events)
- Dropdown configuration
- Dynamic content generation
- Data binding
- Event data codecs
- UI lifetime management

---

## 🗂️ File Organization

```
example/
├── ExamplePlugin.kt          # Main plugin class with initialization
├── commands/
│   └── Commands.kt           # All command examples
├── events/
│   └── Events.kt            # All event listener examples
└── features/
    ├── CustomNpcManager.kt         # NPC creation and management
    ├── MagicItemsManager.kt        # Custom item definitions
    ├── WorldManipulation.kt        # World and block operations
    ├── InteractionExamples.kt      # Interaction chains
    ├── InventoryExamples.kt        # Inventory operations
    ├── ScheduledTaskExamples.kt    # Task scheduling
    └── UiExamples.kt              # UI building
```

---

## 🚀 Quick Start Guide

### To use a specific example:

1. **Commands**: Registered automatically in `registerCommands()`
2. **Events**: Registered automatically in `registerEvents()`
3. **NPCs**: Call methods on `CustomNpcManager` instance
4. **Items**: Call methods on `MagicItemsManager` instance
5. **World**: Call static methods on `WorldManipulation`
6. **Interactions**: Use functions from `InteractionExamples`
7. **Inventory**: Use functions from `InventoryExamples`
8. **Tasks**: Use functions from `ScheduledTaskExamples`
9. **UIs**: Instantiate UI classes and show to players

### Example Usage in Plugin:

```kotlin
class ExamplePlugin : JavaPlugin() {
    override fun onEnable() {
        // Commands and events auto-register
        registerCommands()
        registerEvents()

        // Use managers
        npcManager.spawnMerchantNpc(100.0, 64.0, 100.0)

        // Use items
        val sword = itemsManager.createLegendarySword()

        // Schedule tasks
        val task = ScheduledTaskExamples.startAutoSave()
    }
}
```

---

## 📝 Notes

- All examples are **scaffolds** with TODO markers
- Real implementation requires Hytale API integration
- Examples demonstrate DSL usage, not full functionality
- Refer to main documentation for DSL details

---

**Total Examples:** 50+ distinct code examples across 8 categories
**Lines of Code:** ~2000+ lines of example code
**Coverage:** All major Kytale DSL features
