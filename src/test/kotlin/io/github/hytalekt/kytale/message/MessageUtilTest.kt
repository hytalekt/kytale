package io.github.hytalekt.kytale.message

import com.hypixel.hytale.protocol.MaybeBool
import com.hypixel.hytale.server.core.Message
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import java.awt.Color

class MessageUtilTest :
    FunSpec({
        context("text function") {
            test("creates raw message with text only") {
                val message = text("Hello World")

                message.rawText shouldBe "Hello World"
                message.messageId.shouldBeNull()
            }

            test("creates message with bold style") {
                val message = text("Bold text", bold = true)

                message.rawText shouldBe "Bold text"
                message.formattedMessage.bold shouldBe MaybeBool.True
            }

            test("creates message with italic style") {
                val message = text("Italic text", italic = true)

                message.rawText shouldBe "Italic text"
                message.formattedMessage.italic shouldBe MaybeBool.True
            }

            test("creates message with underlined style") {
                val message = text("Underlined text", underlined = true)

                message.rawText shouldBe "Underlined text"
                message.formattedMessage.underlined shouldBe MaybeBool.True
            }

            test("creates message with monospace style") {
                val message = text("Monospace text", monospace = true)

                message.rawText shouldBe "Monospace text"
                message.formattedMessage.monospace shouldBe MaybeBool.True
            }

            test("creates message with color") {
                val message = text("Colored text", color = Color.RED)

                message.rawText shouldBe "Colored text"
                message.color.shouldNotBeNull()
            }

            test("creates message with multiple styles") {
                val message =
                    text(
                        "Styled text",
                        bold = true,
                        italic = true,
                        underlined = true,
                        monospace = true,
                        color = Color.BLUE,
                    )

                message.rawText shouldBe "Styled text"
                message.formattedMessage.bold shouldBe MaybeBool.True
                message.formattedMessage.italic shouldBe MaybeBool.True
                message.formattedMessage.underlined shouldBe MaybeBool.True
                message.formattedMessage.monospace shouldBe MaybeBool.True
                message.color.shouldNotBeNull()
            }

            test("creates message with children") {
                val child1 = text("Child 1")
                val child2 = text("Child 2")
                val message = text("Parent", children = listOf(child1, child2))

                message.rawText shouldBe "Parent"
                message.children shouldHaveSize 2
            }

            test("creates message with null styles (MaybeBool.Null by default)") {
                val message = text("Plain text")

                message.formattedMessage.bold shouldBe MaybeBool.Null
                message.formattedMessage.italic shouldBe MaybeBool.Null
                message.formattedMessage.underlined shouldBe MaybeBool.Null
                message.formattedMessage.monospace shouldBe MaybeBool.Null
            }

            test("creates message with explicit false styles") {
                val message = text("Not bold", bold = false)

                message.formattedMessage.bold shouldBe MaybeBool.False
            }
        }

        context("i18n function") {
            test("creates translation message with messageId only") {
                val message = i18n("game.message.hello")

                message.messageId shouldBe "game.message.hello"
                message.rawText.shouldBeNull()
            }

            test("creates translation message with bold style") {
                val message = i18n("game.message.warning", bold = true)

                message.messageId shouldBe "game.message.warning"
                message.formattedMessage.bold shouldBe MaybeBool.True
            }

            test("creates translation message with italic style") {
                val message = i18n("game.message.info", italic = true)

                message.messageId shouldBe "game.message.info"
                message.formattedMessage.italic shouldBe MaybeBool.True
            }

            test("creates translation message with multiple styles") {
                val message =
                    i18n(
                        "game.message.error",
                        bold = true,
                        italic = true,
                        underlined = true,
                        monospace = true,
                        color = Color.RED,
                    )

                message.messageId shouldBe "game.message.error"
                message.formattedMessage.bold shouldBe MaybeBool.True
                message.formattedMessage.italic shouldBe MaybeBool.True
                message.formattedMessage.underlined shouldBe MaybeBool.True
                message.formattedMessage.monospace shouldBe MaybeBool.True
                message.color.shouldNotBeNull()
            }

            test("creates translation message with children") {
                val child = text("child")
                val message = i18n("game.message.parent", children = listOf(child))

                message.messageId shouldBe "game.message.parent"
                message.children shouldHaveSize 1
            }
        }

        context("messageOf function") {
            test("joins multiple messages together") {
                val msg1 = text("Hello")
                val msg2 = text("World")
                val msg3 = text("!")

                val joined = messageOf(msg1, msg2, msg3)

                joined.children shouldHaveSize 3
            }

            test("creates empty message when no arguments provided") {
                val joined = messageOf()

                joined.children shouldHaveSize 0
            }

            test("joins single message") {
                val msg = text("Single")
                val joined = messageOf(msg)

                joined.children shouldHaveSize 1
            }
        }

        context("bold extension function") {
            test("applies bold style to message") {
                val message = Message.raw("Test").bold()

                message.formattedMessage.bold shouldBe MaybeBool.True
            }

            test("bold can be chained") {
                val message = Message.raw("Test").bold().italic()

                message.formattedMessage.bold shouldBe MaybeBool.True
                message.formattedMessage.italic shouldBe MaybeBool.True
            }
        }

        context("italic extension function") {
            test("applies italic style to message") {
                val message = Message.raw("Test").italic()

                message.formattedMessage.italic shouldBe MaybeBool.True
            }
        }

        context("monospace extension function") {
            test("applies monospace style to message") {
                val message = Message.raw("Test").monospace()

                message.formattedMessage.monospace shouldBe MaybeBool.True
            }
        }

        context("underlined extension function") {
            test("applies underlined style to message by default") {
                val message = Message.raw("Test").underlined()

                message.formattedMessage.underlined shouldBe MaybeBool.True
            }

            test("applies underlined style with explicit true") {
                val message = Message.raw("Test").underlined(true)

                message.formattedMessage.underlined shouldBe MaybeBool.True
            }

            test("removes underlined style with explicit false") {
                val message = Message.raw("Test").underlined(false)

                message.formattedMessage.underlined shouldBe MaybeBool.False
            }

            test("underlined can be chained with other styles") {
                val message =
                    Message
                        .raw("Test")
                        .bold()
                        .underlined()
                        .italic()

                message.formattedMessage.bold shouldBe MaybeBool.True
                message.formattedMessage.underlined shouldBe MaybeBool.True
                message.formattedMessage.italic shouldBe MaybeBool.True
            }
        }

        context("reset extension function") {
            test("resets all styles to MaybeBool.Null") {
                val message =
                    Message
                        .raw("Test")
                        .bold()
                        .italic()
                        .monospace()
                        .underlined()
                        .reset()

                message.formattedMessage.bold shouldBe MaybeBool.Null
                message.formattedMessage.italic shouldBe MaybeBool.Null
                message.formattedMessage.monospace shouldBe MaybeBool.Null
                message.formattedMessage.underlined shouldBe MaybeBool.Null
            }

            test("reset can be called on unstyled message") {
                val message = Message.raw("Test").reset()

                message.formattedMessage.bold shouldBe MaybeBool.Null
                message.formattedMessage.italic shouldBe MaybeBool.Null
                message.formattedMessage.monospace shouldBe MaybeBool.Null
                message.formattedMessage.underlined shouldBe MaybeBool.Null
            }

            test("styles can be reapplied after reset") {
                val message =
                    Message
                        .raw("Test")
                        .bold()
                        .reset()
                        .italic()

                message.formattedMessage.bold shouldBe MaybeBool.Null
                message.formattedMessage.italic shouldBe MaybeBool.True
            }
        }

        context("plus operator extension") {
            test("adds child message to parent") {
                val parent = Message.raw("Parent")
                val child = Message.raw("Child")

                val result = parent + child

                result.children shouldHaveSize 1
            }

            test("can chain multiple plus operations") {
                val msg1 = Message.raw("First")
                val msg2 = Message.raw("Second")
                val msg3 = Message.raw("Third")

                val result = msg1 + msg2 + msg3

                result.children shouldHaveSize 2
            }
        }

        context("append extension function") {
            test("appends multiple messages") {
                val parent = Message.raw("Parent")
                val child1 = Message.raw("Child1")
                val child2 = Message.raw("Child2")
                val child3 = Message.raw("Child3")

                parent.append(child1, child2, child3)

                parent.children shouldHaveSize 3
            }

            test("append with no arguments does nothing") {
                val parent = Message.raw("Parent")

                parent.append()

                parent.children shouldHaveSize 0
            }

            test("append single message") {
                val parent = Message.raw("Parent")
                val child = Message.raw("Child")

                parent.append(child)

                parent.children shouldHaveSize 1
            }

            test("append can be chained") {
                val parent = Message.raw("Parent")
                val child1 = Message.raw("Child1")
                val child2 = Message.raw("Child2")

                parent.append(child1).append(child2)

                parent.children shouldHaveSize 2
            }
        }

        context("complex message building") {
            test("builds complex styled message with children") {
                val header = text("Error: ", bold = true, color = Color.RED)
                val details = text("Something went wrong", italic = true)
                val code = text("Code: 500", monospace = true)

                val message = messageOf(header, details, code)

                message.children shouldHaveSize 3
                message.children[0].formattedMessage.bold shouldBe MaybeBool.True
                message.children[1].formattedMessage.italic shouldBe MaybeBool.True
                message.children[2].formattedMessage.monospace shouldBe MaybeBool.True
            }

            test("builds message using all utilities together") {
                val message =
                    text("Main")
                        .bold()
                        .append(
                            text("Child 1", italic = true),
                            text("Child 2", monospace = true),
                        )

                message.formattedMessage.bold shouldBe MaybeBool.True
                message.children shouldHaveSize 2
            }

            test("builds nested message hierarchy") {
                val innerChild = text("Inner", underlined = true)
                val child = text("Child", children = listOf(innerChild))
                val parent = text("Parent", children = listOf(child))

                parent.children shouldHaveSize 1
                parent.children[0].children shouldHaveSize 1
            }
        }
    })
