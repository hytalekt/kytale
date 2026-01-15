package aster.amo.kytale.ui.test

import aster.amo.kytale.KotlinPlugin
import aster.amo.kytale.dsl.command
import aster.amo.kytale.ui.dsl.InteractiveUiRegistry
import aster.amo.kytale.ui.dsl.openPage
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.universe.PlayerRef
import java.util.concurrent.CompletableFuture

/**
 * Registers the /uitest command for testing UI elements.
 *
 * This command uses the interactive UI registry to open test pages.
 *
 * Usage in your plugin's setup():
 * ```kotlin
 * registerUiTestCommand()
 * ```
 *
 * Note: The .ui file must be generated first by running `./gradlew compileUi`
 */
fun KotlinPlugin.registerUiTestCommand() {
    command("uitest", "Opens the UI element test page") {
        aliases("testui", "uidemo")

        executesFuture { ctx ->
            val sender = ctx.sender()

            if (sender !is PlayerRef) {
                ctx.sendMessage(Message.raw("This command can only be used by players."))
                return@executesFuture CompletableFuture.completedFuture(null)
            }

            val playerRef = sender
            val ref = playerRef.reference
            val store = ref?.store

            if (ref == null || store == null) {
                ctx.sendMessage(Message.raw("Could not get player reference."))
                return@executesFuture CompletableFuture.completedFuture(null)
            }

            val player = store.getComponent(ref, Player.getComponentType())
            if (player == null) {
                ctx.sendMessage(Message.raw("Could not get player entity."))
                return@executesFuture CompletableFuture.completedFuture(null)
            }


            // Open the UI test page via registry
            if (player.openPage("Kytale/UiTest", playerRef)) {
                ctx.sendMessage(Message.raw("Opening UI test page...").color("55FF55"))
            } else {
                ctx.sendMessage(Message.raw("Failed to open page. Is it registered?").color("FF5555"))
            }

            CompletableFuture.completedFuture(null)
        }

        // Subcommand to open page 2 directly
        subcommand("2", "Open page 2 (Data Management)") {
            executesFuture { ctx ->
                val sender = ctx.sender()
                if (sender !is PlayerRef) {
                    ctx.sendMessage(Message.raw("This command can only be used by players."))
                    return@executesFuture CompletableFuture.completedFuture(null)
                }

                val playerRef = sender
                val ref = playerRef.reference
                val store = ref?.store

                if (ref == null || store == null) {
                    ctx.sendMessage(Message.raw("Could not get player reference."))
                    return@executesFuture CompletableFuture.completedFuture(null)
                }

                val player = store.getComponent(ref, Player.getComponentType())
                if (player == null) {
                    ctx.sendMessage(Message.raw("Could not get player entity."))
                    return@executesFuture CompletableFuture.completedFuture(null)
                }

                if (player.openPage("Kytale/UiTest2", playerRef)) {
                    ctx.sendMessage(Message.raw("Opening page 2 (Data Management)...").color("55FF55"))
                } else {
                    ctx.sendMessage(Message.raw("Failed to open page. Is it registered?").color("FF5555"))
                }

                CompletableFuture.completedFuture(null)
            }
        }

        // Subcommand to open page 3 directly
        subcommand("3", "Open page 3 (Actions)") {
            executesFuture { ctx ->
                val sender = ctx.sender()
                if (sender !is PlayerRef) {
                    ctx.sendMessage(Message.raw("This command can only be used by players."))
                    return@executesFuture CompletableFuture.completedFuture(null)
                }

                val playerRef = sender
                val ref = playerRef.reference
                val store = ref?.store

                if (ref == null || store == null) {
                    ctx.sendMessage(Message.raw("Could not get player reference."))
                    return@executesFuture CompletableFuture.completedFuture(null)
                }

                val player = store.getComponent(ref, Player.getComponentType())
                if (player == null) {
                    ctx.sendMessage(Message.raw("Could not get player entity."))
                    return@executesFuture CompletableFuture.completedFuture(null)
                }

                if (player.openPage("Kytale/UiTest3", playerRef)) {
                    ctx.sendMessage(Message.raw("Opening page 3 (Actions)...").color("55FF55"))
                } else {
                    ctx.sendMessage(Message.raw("Failed to open page. Is it registered?").color("FF5555"))
                }

                CompletableFuture.completedFuture(null)
            }
        }

        // Subcommand to list registered pages
        subcommand("list", "List all registered interactive UI pages") {
            executes { ctx ->
                val pages = InteractiveUiRegistry.getPaths()
                ctx.sendMessage(Message.raw("=== Registered Interactive Pages ===").color("FFD700"))
                if (pages.isEmpty()) {
                    ctx.sendMessage(Message.raw("No pages registered.").color("AAAAAA"))
                } else {
                    pages.forEach { path ->
                        ctx.sendMessage(Message.raw("- $path"))
                    }
                }
                ctx.sendMessage(Message.raw("Total: ${pages.size} pages").color("55FF55"))
            }
        }

        // Subcommand to show available elements
        subcommand("elements", "List all available UI elements") {
            executes { ctx ->
                ctx.sendMessage(Message.raw("=== Available Interactive UI DSL Elements ===").color("FFD700"))
                ctx.sendMessage(Message.raw("Interactive Elements:").color("C4A23A"))
                ctx.sendMessage(Message.raw("- textButton    Clickable button with text + onClick"))
                ctx.sendMessage(Message.raw("- button        Icon button + onClick"))
                ctx.sendMessage(Message.raw("- slider        Integer slider + onValueChange"))
                ctx.sendMessage(Message.raw("- floatSlider   Float slider + onValueChange"))
                ctx.sendMessage(Message.raw("- checkBox      Toggle checkbox + onValueChange"))
                ctx.sendMessage(Message.raw("- textField     Text input + onValueChange/onSubmit"))
                ctx.sendMessage(Message.raw("- numberField   Number input + onValueChange"))
                ctx.sendMessage(Message.raw("- dropdownBox   Dropdown + onSelectionChange"))
                ctx.sendMessage(Message.raw("- colorPicker   Color picker + onColorChange"))
                ctx.sendMessage(Message.raw(""))
                ctx.sendMessage(Message.raw("Display Elements:").color("C4A23A"))
                ctx.sendMessage(Message.raw("- group         Container for other elements"))
                ctx.sendMessage(Message.raw("- label         Text display"))
                ctx.sendMessage(Message.raw("- sprite        Image from sprite sheet"))
                ctx.sendMessage(Message.raw("- progressBar   Progress indicator"))
                ctx.sendMessage(Message.raw(""))
                ctx.sendMessage(Message.raw("Button Presets:").color("C4A23A"))
                ctx.sendMessage(Message.raw("- darkButton()     Dark themed"))
                ctx.sendMessage(Message.raw("- primaryButton()  Gold/primary"))
                ctx.sendMessage(Message.raw("- dangerButton()   Red/danger"))
                ctx.sendMessage(Message.raw("- successButton()  Green/success"))
                ctx.sendMessage(Message.raw("- warningButton()  Orange/warning"))
                ctx.sendMessage(Message.raw("- infoButton()     Blue/info"))
                ctx.sendMessage(Message.raw(""))
                ctx.sendMessage(Message.raw("Use /uitest to see them in action!").color("55FF55"))
            }
        }
    }
}
