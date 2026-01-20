package io.github.hytalekt.kytale.tests.test

import io.github.hytalekt.kytale.dsl.command
import com.hypixel.hytale.server.core.command.system.CommandManager

/**
 * Integration tests for CommandBuilder DSL.
 *
 * Tests that commands created via the DSL are correctly registered
 * and configured in the Hytale command system.
 */
fun IntegrationTestRunner.commandDslTests() = suite("CommandBuilder DSL") {

    test("command function registers command with correct name") {
        val testName = "cmdtest-name-${System.nanoTime()}"

        plugin.command(testName, "Test command") {
            executesSync { }
        }

        val cmd = assertNotNull(
            CommandManager.get().commandRegistration[testName],
            "Command '$testName' should be registered"
        )
        assertEquals(testName, cmd.name)
    }

    test("command has correct description") {
        val testName = "cmdtest-desc-${System.nanoTime()}"
        val testDesc = "A descriptive command"

        plugin.command(testName, testDesc) {
            executesSync { }
        }

        val cmd = assertNotNull(CommandManager.get().commandRegistration[testName])
        assertEquals(testDesc, cmd.description)
    }

    test("aliases are registered with command") {
        val testName = "cmdtest-alias-${System.nanoTime()}"
        val alias1 = "cta1-${System.nanoTime()}"
        val alias2 = "cta2-${System.nanoTime()}"

        plugin.command(testName, "Alias test") {
            aliases(alias1, alias2)
            executesSync { }
        }

        val cmd = assertNotNull(CommandManager.get().commandRegistration[testName])
        assertEquals(2, cmd.aliases.size)
        assertContains(cmd.aliases.toList(), alias1)
        assertContains(cmd.aliases.toList(), alias2)
    }

    test("multiple alias calls accumulate") {
        val testName = "cmdtest-multialias-${System.nanoTime()}"

        plugin.command(testName, "Multi-alias test") {
            aliases("ma1")
            aliases("ma2", "ma3")
            executesSync { }
        }

        val cmd = assertNotNull(CommandManager.get().commandRegistration[testName])
        assertEquals(3, cmd.aliases.size)
    }

    test("subcommands are registered") {
        val testName = "cmdtest-sub-${System.nanoTime()}"

        plugin.command(testName, "Subcommand test") {
            subcommand("sub1", "First subcommand") {
                executesSync { }
            }
            subcommand("sub2", "Second subcommand") {
                executesSync { }
            }
        }

        val cmd = assertNotNull(CommandManager.get().commandRegistration[testName])
        assertEquals(2, cmd.subCommands.size)
        assertNotNull(cmd.subCommands["sub1"])
        assertNotNull(cmd.subCommands["sub2"])
    }

    test("nested subcommands are registered") {
        val testName = "cmdtest-nested-${System.nanoTime()}"

        plugin.command(testName, "Nested test") {
            subcommand("level1", "First level") {
                subcommand("level2", "Second level") {
                    executesSync { }
                }
            }
        }

        val cmd = assertNotNull(CommandManager.get().commandRegistration[testName])
        val level1 = assertNotNull(cmd.subCommands["level1"])
        val level2 = assertNotNull(level1.subCommands["level2"])
        assertEquals("level2", level2.name)
    }

    test("subcommand has correct description") {
        val testName = "cmdtest-subdesc-${System.nanoTime()}"

        plugin.command(testName, "Parent") {
            subcommand("child", "Child description") {
                executesSync { }
            }
        }

        val cmd = assertNotNull(CommandManager.get().commandRegistration[testName])
        val child = assertNotNull(cmd.subCommands["child"])
        assertEquals("Child description", child.description)
    }

    test("subcommand aliases are registered") {
        val testName = "cmdtest-subalias-${System.nanoTime()}"

        plugin.command(testName, "Parent") {
            subcommand("child", "Child") {
                aliases("c", "ch")
                executesSync { }
            }
        }

        val cmd = assertNotNull(CommandManager.get().commandRegistration[testName])
        val child = assertNotNull(cmd.subCommands["child"])
        assertEquals(2, child.aliases.size)
        assertContains(child.aliases.toList(), "c")
    }

    test("command with async executes compiles and registers") {
        val testName = "cmdtest-async-${System.nanoTime()}"

        plugin.command(testName, "Async test") {
            executes { ctx ->
                // Async handler
            }
        }

        assertNotNull(
            CommandManager.get().commandRegistration[testName],
            "Async command should be registered"
        )
    }

    test("command with future executes compiles and registers") {
        val testName = "cmdtest-future-${System.nanoTime()}"

        plugin.command(testName, "Future test") {
            executesFuture { null }
        }

        assertNotNull(
            CommandManager.get().commandRegistration[testName],
            "Future command should be registered"
        )
    }

    test("command with sync executes compiles and registers") {
        val testName = "cmdtest-sync-${System.nanoTime()}"

        plugin.command(testName, "Sync test") {
            executesSync { }
        }

        assertNotNull(
            CommandManager.get().commandRegistration[testName],
            "Sync command should be registered"
        )
    }
}
