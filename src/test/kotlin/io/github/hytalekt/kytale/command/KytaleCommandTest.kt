package io.github.hytalekt.kytale.command

import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class KytaleCommandTest :
    FunSpec({
        // Note: We cannot access the private argument maps from AbstractCommand,
        // so we make sure the returned argument objects are not null

        // Helper to create a mock argument type
        context("command creation") {
            test("creates command with name and description") {
                val cmd = command("test", "Test command") {}

                cmd.name shouldBe "test"
                cmd.description shouldBe "Test command"
            }
        }

        context("aliases") {
            test("adds aliases") {
                val cmd =
                    command("test", "Test") {
                        alias("t", "tst", "test1")
                    }

                cmd.aliases.size shouldBe 3
                cmd.aliases shouldContain "t"
                cmd.aliases shouldContain "tst"
                cmd.aliases shouldContain "test1"

                // Command without aliases
                val noAliasCmd = command("test2", "Test") {}
                noAliasCmd.aliases.shouldBeEmpty()
            }
        }

        context("arguments") {
            test("adds required arguments") {
                val cmd =
                    command("test", "Test") {
                        requiredArg("name", "Name", ArgTypes.STRING)
                        requiredArg("amount", "Amount", ArgTypes.INTEGER)
                    }

                cmd.requiredArguments.size shouldBe 2
                cmd.requiredArguments[0].name shouldBe "name"
                cmd.requiredArguments[1].name shouldBe "amount"
            }

            test("adds optional and default arguments") {
                command("test", "Test") {
                    optionalArg("opt", "Optional", ArgTypes.STRING).shouldNotBeNull()
                    defaultArg("def", "Default", ArgTypes.INTEGER, 1, "1").shouldNotBeNull()
                }
            }

            test("adds flag arguments") {
                command("test", "Test") {
                    flagArg("force", "Force").shouldNotBeNull()
                    flagArg("silent", "Silent").shouldNotBeNull()
                }
            }

            test("adds list arguments") {
                command("test", "Test") {
                    listOptionalArg("tags", "Tags", ArgTypes.STRING).shouldNotBeNull()
                    listDefaultArg("numbers", "Numbers", ArgTypes.INTEGER, emptyList(), "none").shouldNotBeNull()
                }
            }
        }

        context("permissions") {
            test("sets permission") {
                val cmd =
                    command("test", "Test") {
                        requirePermission("admin.test")
                    }

                cmd.permission shouldBe "admin.test"
            }

            test("has no permission by default") {
                val cmd = command("test", "Test") {}
                cmd.permission.shouldBeNull()
            }
        }

        context("subcommands") {
            test("adds subcommands") {
                val cmd =
                    command("test", "Test") {
                        subcommand("add", "Add") {}
                        subcommand("remove", "Remove") {}
                    }

                cmd.subCommands.size shouldBe 2
                cmd.subCommands.keys shouldContain "add"
                cmd.subCommands.keys shouldContain "remove"
            }
        }

        context("KytaleCommand class") {
            test("creates command directly") {
                val cmd = KytaleCommand("test", "Test")

                cmd.name shouldBe "test"
                cmd.description shouldBe "Test"
                cmd.owner.shouldBeNull()
                cmd.defaultExecutor.shouldNotBeNull()
            }
        }
    })
