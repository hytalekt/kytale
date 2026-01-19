package io.github.hytalekt.kytale.ui.dsl

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf

class UiDslTest :
    FunSpec({
        context("uiPage function") {
            test("creates page with name") {
                val page = uiPage("TestPage") {}

                page.name shouldBe "TestPage"
            }

            test("creates page with default dimensions") {
                val page = uiPage("TestPage") {}

                page.width shouldBe 500
                page.height shouldBe 400
            }

            test("creates page with custom dimensions") {
                val page = uiPage("TestPage") {
                    width = 800
                    height = 600
                }

                page.width shouldBe 800
                page.height shouldBe 600
            }

            test("creates page with title") {
                val page = uiPage("TestPage") {
                    title = "My Page Title"
                }

                page.title shouldBe "My Page Title"
            }

            test("creates page with back button by default") {
                val page = uiPage("TestPage") {}

                page.includeBackButton shouldBe true
            }

            test("creates page without back button when disabled") {
                val page = uiPage("TestPage") {
                    includeBackButton = false
                }

                page.includeBackButton shouldBe false
            }
        }

        context("content block") {
            test("creates content group with default Top layout") {
                val page = uiPage("TestPage") {
                    content {}
                }
                val serialized = page.serialize()

                serialized shouldContain "#Content"
                serialized shouldContain "LayoutMode: Top"
            }

            test("content can contain child elements") {
                val page = uiPage("TestPage") {
                    content {
                        label { text = "Hello" }
                        label { text = "World" }
                    }
                }
                val serialized = page.serialize()

                serialized shouldContain "Label"
                serialized shouldContain "\"Hello\""
                serialized shouldContain "\"World\""
            }
        }

        context("nested groups") {
            test("creates nested group without id") {
                val page = uiPage("TestPage") {
                    content {
                        group {
                            label { text = "Inside group" }
                        }
                    }
                }
                val serialized = page.serialize()

                serialized shouldContain "Group {"
                serialized shouldContain "\"Inside group\""
            }

            test("creates nested group with id") {
                val page = uiPage("TestPage") {
                    content {
                        group(id = "MyGroup") {
                            label { text = "Inside" }
                        }
                    }
                }
                val serialized = page.serialize()

                serialized shouldContain "Group #MyGroup {"
            }

            test("groups can have layout mode") {
                val page = uiPage("TestPage") {
                    content {
                        group {
                            layoutMode = LayoutMode.Left
                            label { text = "Left aligned" }
                        }
                    }
                }
                val serialized = page.serialize()

                serialized shouldContain "LayoutMode: Left"
            }

            test("deeply nested groups") {
                val page = uiPage("TestPage") {
                    content {
                        group(id = "Level1") {
                            group(id = "Level2") {
                                group(id = "Level3") {
                                    label { text = "Deep" }
                                }
                            }
                        }
                    }
                }
                val serialized = page.serialize()

                serialized shouldContain "#Level1"
                serialized shouldContain "#Level2"
                serialized shouldContain "#Level3"
            }
        }

        context("serialize function") {
            test("includes Common.ui import") {
                val page = uiPage("TestPage") {}
                val serialized = page.serialize()

                serialized shouldContain "\$C = \"../../Common.ui\";"
            }

            test("includes PageOverlay wrapper") {
                val page = uiPage("TestPage") {}
                val serialized = page.serialize()

                serialized shouldContain "\$C.@PageOverlay {"
            }

            test("includes Container with dimensions") {
                val page = uiPage("TestPage") {
                    width = 640
                    height = 480
                }
                val serialized = page.serialize()

                serialized shouldContain "\$C.@Container {"
                serialized shouldContain "Anchor: (Width: 640, Height: 480)"
            }

            test("includes Title section") {
                val page = uiPage("TestPage") {
                    title = "Test Title"
                }
                val serialized = page.serialize()

                serialized shouldContain "#Title"
                serialized shouldContain "@Text = \"Test Title\""
            }

            test("includes BackButton when enabled") {
                val page = uiPage("TestPage") {
                    includeBackButton = true
                }
                val serialized = page.serialize()

                serialized shouldContain "\$C.@BackButton {}"
            }

            test("excludes BackButton when disabled") {
                val page = uiPage("TestPage") {
                    includeBackButton = false
                }
                val serialized = page.serialize()

                serialized shouldContain "\$C.@PageOverlay"
                serialized.contains("\$C.@BackButton") shouldBe false
            }
        }

        context("UiGroup") {
            test("creates empty group") {
                val group = UiGroup()

                group.children.shouldBeEmpty()
            }

            test("group children list is mutable") {
                val group = UiGroup()
                group.children.add(UiLabel())

                group.children shouldHaveSize 1
            }

            test("group has configurable layout modes") {
                val group = UiGroup()

                group.layoutMode = LayoutMode.TopScrolling
                group.layoutMode shouldBe LayoutMode.TopScrolling

                group.layoutMode = LayoutMode.LeftCenterWrap
                group.layoutMode shouldBe LayoutMode.LeftCenterWrap
            }

            test("group serializes scrollbar style") {
                val group = UiGroup().apply {
                    scrollbarStyle = "\$C.@DefaultScrollbar"
                }
                val serialized = group.serialize()

                serialized shouldContain "ScrollbarStyle: \$C.@DefaultScrollbar"
            }

            test("group serializes clip children") {
                val group = UiGroup().apply {
                    clipChildren = true
                }
                val serialized = group.serialize()

                serialized shouldContain "ClipChildren: true"
            }
        }

        context("multiple element types in DSL") {
            test("creates page with various element types") {
                val page = uiPage("ComplexPage") {
                    content {
                        label { text = "Header" }
                        textButton("SubmitBtn") { text = "Submit" }
                        textField { placeholderText = "Enter text..." }
                        slider { min = 0; max = 100 }
                        checkBox { value = true }
                    }
                }
                val serialized = page.serialize()

                serialized shouldContain "Label"
                serialized shouldContain "\$C.@TextButton #SubmitBtn"
                serialized shouldContain "\$C.@TextField"
                serialized shouldContain "Slider"
                serialized shouldContain "\$C.@CheckBox"
            }
        }
    })
