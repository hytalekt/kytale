package io.github.hytalekt.kytale.command

import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class KytaleCommandDslTest :
    FunSpec({
        test("DSL creates complete command with all features") {
            val cmd =
                command("test", "Test command") {
                    alias("t", "tst")
                    requirePermission("test.permission")

                    requiredArg("name", "Name", ArgTypes.STRING)
                    optionalArg("value", "Value", ArgTypes.INTEGER)
                    defaultArg("count", "Count", ArgTypes.INTEGER, 1, "1")
                    flagArg("force", "Force")

                    listRequiredArg("items", "Items", ArgTypes.STRING)
                    listOptionalArg("tags", "Tags", ArgTypes.STRING)
                    listDefaultArg("numbers", "Numbers", ArgTypes.INTEGER, emptyList(), "none")

                    subcommand("sub", "Subcommand") {
                        alias("s")
                        requiredArg("arg", "Argument", ArgTypes.STRING)
                    }

                    variant("Variant") {
                        requiredArg("arg", "Argument", ArgTypes.STRING)
                    }

                    executorSync {
                        // Executor body
                    }
                }

            cmd.name shouldBe "test"
            cmd.description shouldBe "Test command"
            cmd.aliases.size shouldBe 2
            cmd.permission shouldBe "test.permission"

            // Validate arguments
            cmd.requiredArguments.size shouldBe 2 // name and items
            cmd.requiredArguments[0].name shouldBe "name"
            cmd.requiredArguments[1].name shouldBe "items"

            // Validate subcommand
            cmd.subCommands.size shouldBe 1
            val sub = cmd.subCommands["sub"].shouldNotBeNull()
            sub.name shouldBe "sub"
            sub.aliases.size shouldBe 1
            sub.requiredArguments.size shouldBe 1

            // Validate executor
            cmd.defaultExecutor.shouldNotBeNull()
        }

        test("DSL supports nested command hierarchies") {
            val cmd =
                command("parent", "Parent") {
                    subcommand("child1", "Child 1") {
                        subcommand("grandchild", "Grandchild") {
                            requiredArg("arg", "Argument", ArgTypes.STRING)
                        }
                    }

                    subcommand("child2", "Child 2") {
                        variant("Variant 1") {}
                        variant("Variant 2") {
                            requiredArg("arg1", "Arg 1", ArgTypes.STRING)
                        }
                    }
                }

            cmd.name shouldBe "parent"
            cmd.subCommands.size shouldBe 2

            // Validate child1 and its grandchild
            cmd.subCommands["child1"]
                .shouldNotBeNull()
                .subCommands.size shouldBe 1
            cmd.subCommands["child1"]
                .shouldNotBeNull()
                .subCommands["grandchild"]
                .shouldNotBeNull()
                .requiredArguments
                .size shouldBe 1

            // Validate child2 exists and has correct structure
            val child2 = cmd.subCommands["child2"].shouldNotBeNull()
            child2.name shouldBe "child2"
            child2.description shouldBe "Child 2"
        }

        test("DSL supports multiple variants with different argument counts") {
            val cmd =
                command("give", "Give items to players") {
                    variant("Give default") {
                        requiredArg("player", "Player name", ArgTypes.STRING)
                    }

                    variant("Give with item") {
                        requiredArg("player", "Player name", ArgTypes.STRING)
                        requiredArg("item", "Item name", ArgTypes.STRING)
                    }

                    variant("Give with amount") {
                        requiredArg("player", "Player name", ArgTypes.STRING)
                        requiredArg("item", "Item name", ArgTypes.STRING)
                        requiredArg("amount", "Amount", ArgTypes.INTEGER)
                    }
                }

            cmd.name shouldBe "give"
            cmd.description shouldBe "Give items to players"
            // Variants are registered successfully if no exception is thrown during command creation
        }
    })
