package io.github.hytalekt.kytale.tests.test

import io.github.hytalekt.kytale.hexweave.enableHexweave
import com.hypixel.hytale.server.core.command.system.CommandManager

/**
 * Integration tests for Hexweave framework.
 *
 * Tests that Hexweave initializes correctly and command/event DSLs work
 * with real Hytale APIs.
 */
fun IntegrationTestRunner.hexweaveTests() = suite("Hexweave Framework") {

    test("enableHexweave initializes without error") {
        var initialized = false

        plugin.enableHexweave {
            initialized = true
        }

        assertTrue(initialized)
    }

    test("commands block can register commands") {
        val testName = "hex-test-cmd-${System.nanoTime()}"

        plugin.enableHexweave {
            commands {
                literal(testName, "Hexweave test command") {
                    executesPlayer {
                        // Test handler
                    }
                }
            }
        }

        assertNotNull(
            CommandManager.get().commandRegistration[testName],
            "Command '$testName' should be registered via Hexweave"
        )
    }

    test("commands can have aliases") {
        val testName = "hex-alias-cmd-${System.nanoTime()}"
        val alias1 = "ha1-${System.nanoTime()}"

        plugin.enableHexweave {
            commands {
                literal(testName, "Alias test") {
                    aliases(alias1)
                    executesPlayer { }
                }
            }
        }

        val cmd = assertNotNull(CommandManager.get().commandRegistration[testName])
        assertContains(cmd.aliases.toList(), alias1)
    }

    test("commands can have subcommands") {
        val testName = "hex-sub-cmd-${System.nanoTime()}"

        plugin.enableHexweave {
            commands {
                literal(testName, "Subcommand test") {
                    subcommand("sub1", "First sub") {
                        executesPlayer { }
                    }
                    subcommand("sub2", "Second sub") {
                        executesPlayer { }
                    }
                }
            }
        }

        val cmd = assertNotNull(CommandManager.get().commandRegistration[testName])
        assertEquals(2, cmd.subCommands.size)
        assertNotNull(cmd.subCommands["sub1"])
        assertNotNull(cmd.subCommands["sub2"])
    }

    test("nested subcommands work") {
        val testName = "hex-nested-cmd-${System.nanoTime()}"

        plugin.enableHexweave {
            commands {
                literal(testName, "Nested test") {
                    subcommand("level1", "First level") {
                        subcommand("level2", "Second level") {
                            executesPlayer { }
                        }
                    }
                }
            }
        }

        val cmd = assertNotNull(CommandManager.get().commandRegistration[testName])
        val level1 = assertNotNull(cmd.subCommands["level1"])
        assertNotNull(level1.subCommands["level2"])
    }

    test("players block compiles without error") {
        var playersBlockRan = false

        plugin.enableHexweave {
            players {
                playersBlockRan = true
                // Can't actually test onJoin/onLeave without a player
            }
        }

        assertTrue(playersBlockRan)
    }

    test("events block compiles without error") {
        var eventsBlockRan = false

        plugin.enableHexweave {
            events {
                eventsBlockRan = true
                // Event registration happens but we can't trigger events in tests
            }
        }

        assertTrue(eventsBlockRan)
    }

    test("systems block compiles without error") {
        var systemsBlockRan = false

        plugin.enableHexweave {
            systems {
                systemsBlockRan = true
                // System registration happens but requires world context
            }
        }

        assertTrue(systemsBlockRan)
    }

    test("multiple blocks can be combined") {
        var commandsRan = false
        var playersRan = false
        var eventsRan = false

        plugin.enableHexweave {
            commands {
                commandsRan = true
            }
            players {
                playersRan = true
            }
            events {
                eventsRan = true
            }
        }

        assertTrue(commandsRan)
        assertTrue(playersRan)
        assertTrue(eventsRan)
    }

    test("enableHexweave can be called multiple times") {
        var count = 0

        plugin.enableHexweave { count++ }
        plugin.enableHexweave { count++ }

        assertEquals(2, count)
    }
}
