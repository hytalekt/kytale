package io.github.hytalekt.kytale.dsl

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.nulls.shouldNotBeNull

/**
 * Tests for CommandBuilder DSL.
 *
 * CommandBuilder no longer requires JavaPlugin, making these tests straightforward.
 */
class CommandDslTest : FunSpec({

    context("CommandBuilder") {
        test("creates builder with name and description") {
            val builder = CommandBuilder("test", "A test command")

            builder.name shouldBe "test"
            builder.description shouldBe "A test command"
        }

        test("aliases are initially empty") {
            val builder = CommandBuilder("test", "A test command")

            builder.aliases.size shouldBe 0
        }

        test("aliases can be added") {
            val builder = CommandBuilder("test", "A test command")

            builder.aliases("t", "tst", "testing")

            builder.aliases.size shouldBe 3
            builder.aliases shouldBe mutableListOf("t", "tst", "testing")
        }

        test("subcommands can be added") {
            val builder = CommandBuilder("parent", "Parent command")

            builder.subcommand("child", "Child command") {}

            builder.subcommands.size shouldBe 1
            builder.subcommands[0].name shouldBe "child"
            builder.subcommands[0].description shouldBe "Child command"
        }

        test("nested subcommands can be added") {
            val builder = CommandBuilder("parent", "Parent command")

            builder.subcommand("child", "Child command") {
                subcommand("grandchild", "Grandchild command") {}
            }

            builder.subcommands.size shouldBe 1
            builder.subcommands[0].subcommands.size shouldBe 1
            builder.subcommands[0].subcommands[0].name shouldBe "grandchild"
        }

        test("multiple aliases can be added in separate calls") {
            val builder = CommandBuilder("test", "Test")

            builder.aliases("a", "b")
            builder.aliases("c")

            builder.aliases.size shouldBe 3
        }

        test("executor can be set") {
            val builder = CommandBuilder("test", "Test")

            builder.executes { /* do nothing */ }

            builder.executor.shouldNotBeNull()
        }

        test("syncExecutor can be set") {
            val builder = CommandBuilder("test", "Test")

            builder.executesSync { /* do nothing */ }

            builder.syncExecutor.shouldNotBeNull()
        }

        test("futureExecutor can be set") {
            val builder = CommandBuilder("test", "Test")

            builder.executesFuture { null }

            builder.futureExecutor.shouldNotBeNull()
        }

        // NOTE: build() is tested via integration tests in :tests module (CommandDslIntegrationTests)
        // since AbstractCommand requires Hytale server classes.

        test("command DSL style building works") {
            val builder = CommandBuilder("spawn", "Teleport to spawn")
            builder.apply {
                aliases("s", "home")
                subcommand("set", "Set spawn location") {
                    aliases("s")
                }
            }

            builder.aliases shouldBe mutableListOf("s", "home")
            builder.subcommands.size shouldBe 1
            builder.subcommands[0].aliases shouldBe mutableListOf("s")
        }
    }

    context("command execution types") {
        test("only one executor type can be active at a time") {
            val builder = CommandBuilder("test", "Test")

            builder.executes { }
            builder.executor.shouldNotBeNull()

            builder.executesSync { }
            builder.syncExecutor.shouldNotBeNull()

            builder.executesFuture { null }
            builder.futureExecutor.shouldNotBeNull()
        }
    }
})
