package aster.amo.kytale.ui.test

import aster.amo.kytale.ui.dsl.*
import com.hypixel.hytale.server.core.Message

/**
 * Interactive UI test pages demonstrating the combined InteractiveUiPage API.
 *
 * These pages replace the manual UiTestPage, UiTestPage2, and UiTestPage3 classes
 * by combining UI structure definition with event handlers in a single DSL.
 *
 * Usage:
 * ```kotlin
 * // Open via registry
 * player.openPage("Kytale/UiTest", playerRef)
 * ```
 */
object InteractiveTestPages {

    /**
     * Register all interactive test pages.
     * Called from KytaleTestUi.registerAll() to include in compileUi task.
     */
    fun registerAll() {
        InteractiveUiRegistry.register("Kytale/UiTest", uiTestPage1)
        InteractiveUiRegistry.register("Kytale/UiTest2", uiTestPage2)
        InteractiveUiRegistry.register("Kytale/UiTest3", uiTestPage3)
    }

    // ==================== SHARED STYLES ====================

    private fun InteractiveGroupBuilder.navButton(id: String, text: String, isActive: Boolean) {
        textButton(id) {
            if (isActive) {
                primaryButton(text)
            } else {
                darkButton(text)
            }
            anchor = UiAnchor(height = 36)
            flexWeight = 1
        }
    }

    private fun InteractiveGroupBuilder.navButtonSpaced(id: String, text: String, isActive: Boolean, onClick: OnActivate) {
        textButton(id) {
            if (isActive) {
                primaryButton(text)
            } else {
                darkButton(text)
            }
            anchor = UiAnchor(height = 36, left = 8)
            flexWeight = 1
            this.onClick = onClick
        }
    }

    private fun InteractiveGroupBuilder.sectionHeader(title: String) {
        label {
            text = title
            style = UiLabelStyle(
                fontSize = 16,
                textColor = "#c4a23a",
                renderBold = true
            )
        }
    }

    // ==================== PAGE 1: INPUT CONTROLS ====================

    val uiTestPage1 = interactivePage("UiTest") {
        width = 550
        height = 650

        title {
            label {
                text = "UI Test - Page 1"
                style = UiLabelStyle(
                    fontSize = 24,
                    textColor = "#ffffff",
                    renderBold = true,
                    horizontalAlignment = HorizontalAlignment.Center
                )
            }
        }

        content {
            // Navigation Bar
            group("NavBar") {
                layoutMode = LayoutMode.Left
                padding = UiPadding(horizontal = 16, vertical = 12)

                textButton("NavPage1") {
                    primaryButton("1: Inputs")
                    anchor = UiAnchor(height = 36)
                    flexWeight = 1
                    onClick = {
                        player.sendMessage(Message.raw("Already on page 1").color("AAAAAA"))
                    }
                }

                textButton("NavPage2") {
                    darkButton("2: Data")
                    anchor = UiAnchor(height = 36, left = 8)
                    flexWeight = 1
                    onClick = {
                        player.sendMessage(Message.raw("Opening page 2...").color("55FF55"))
                        player.openPage("Kytale/UiTest2", playerRef)
                    }
                }

                textButton("NavPage3") {
                    darkButton("3: Actions")
                    anchor = UiAnchor(height = 36, left = 8)
                    flexWeight = 1
                    onClick = {
                        player.sendMessage(Message.raw("Opening page 3...").color("55FF55"))
                        player.openPage("Kytale/UiTest3", playerRef)
                    }
                }
            }

            // Page Title
            group("PageTitle") {
                padding = UiPadding(horizontal = 16, vertical = 8)
                label {
                    text = "Input Controls"
                    style = UiLabelStyle(fontSize = 24, textColor = "#ffffff", renderBold = true)
                }
            }

            // Text Fields Section
            group("TextFieldSection") {
                layoutMode = LayoutMode.Top
                padding = UiPadding(vertical = 12, horizontal = 16)

                sectionHeader("Text Input")

                group {
                    layoutMode = LayoutMode.Left
                    padding = UiPadding(top = 10)

                    label {
                        text = "Username:"
                        anchor = UiAnchor(width = 90)
                        style = UiLabelStyle(fontSize = 14, textColor = "#7c8b99", verticalAlignment = VerticalAlignment.Center)
                    }

                    textField("UsernameInput") {
                        anchor = UiAnchor(width = 200, height = 34)
                        placeholderText = "Enter username..."
                        onChange = {
                            player.sendMessage(Message.raw("Username: ${stringValue ?: "empty"}").color("55FFFF"))
                        }
                    }
                }

                group {
                    layoutMode = LayoutMode.Left
                    padding = UiPadding(top = 8)

                    label {
                        text = "Search:"
                        anchor = UiAnchor(width = 90)
                        style = UiLabelStyle(fontSize = 14, textColor = "#7c8b99", verticalAlignment = VerticalAlignment.Center)
                    }

                    textField("SearchInput") {
                        anchor = UiAnchor(width = 150, height = 34)
                        placeholderText = "Search..."
                    }

                    textButton("SearchButton") {
                        primaryButton("Search")
                        anchor = UiAnchor(width = 70, height = 34, left = 8)
                        onClick = {
                            player.sendMessage(Message.raw("Searching...").color("55FFFF"))
                        }
                    }
                }
            }

            // Sliders Section
            group("SliderSection") {
                layoutMode = LayoutMode.Top
                padding = UiPadding(vertical = 12, horizontal = 16)

                sectionHeader("Sliders")

                group {
                    layoutMode = LayoutMode.Left
                    padding = UiPadding(top = 10)

                    label {
                        text = "Volume:"
                        anchor = UiAnchor(width = 90)
                        style = UiLabelStyle(fontSize = 14, textColor = "#7c8b99", verticalAlignment = VerticalAlignment.Center)
                    }

                    slider("VolumeSlider") {
                        anchor = UiAnchor(width = 180, height = 20)
                        min = 0
                        max = 100
                        value = 75
                        onChange = {
                            player.sendMessage(Message.raw("Volume: ${intValue ?: value}%").color("C4A23A"))
                        }
                    }

                    label("VolumeValue") {
                        text = "75%"
                        anchor = UiAnchor(width = 50, left = 10)
                        style = UiLabelStyle(fontSize = 14, textColor = "#ffffff", verticalAlignment = VerticalAlignment.Center)
                    }
                }

                group {
                    layoutMode = LayoutMode.Left
                    padding = UiPadding(top = 8)

                    label {
                        text = "Brightness:"
                        anchor = UiAnchor(width = 90)
                        style = UiLabelStyle(fontSize = 14, textColor = "#7c8b99", verticalAlignment = VerticalAlignment.Center)
                    }

                    slider("BrightnessSlider") {
                        anchor = UiAnchor(width = 180, height = 20)
                        min = 0
                        max = 100
                        value = 50
                        onChange = {
                            player.sendMessage(Message.raw("Brightness: ${intValue ?: value}%").color("C4A23A"))
                        }
                    }

                    label("BrightnessValue") {
                        text = "50%"
                        anchor = UiAnchor(width = 50, left = 10)
                        style = UiLabelStyle(fontSize = 14, textColor = "#ffffff", verticalAlignment = VerticalAlignment.Center)
                    }
                }
            }

            // Checkboxes Section
            group("CheckboxSection") {
                layoutMode = LayoutMode.Top
                padding = UiPadding(vertical = 12, horizontal = 16)

                sectionHeader("Options")

                group {
                    layoutMode = LayoutMode.Left
                    padding = UiPadding(top = 10)

                    group {
                        layoutMode = LayoutMode.Left
                        anchor = UiAnchor(width = 150)

                        checkBox("EnableSounds") {
                            value = true
                            onChange = {
                                val enabled = boolValue ?: false
                                player.sendMessage(Message.raw("Enable Sounds: ${if (enabled) "ON" else "OFF"}").color("4ECDC4"))
                            }
                        }
                        label {
                            text = "Enable Sounds"
                            anchor = UiAnchor(left = 8)
                            style = UiLabelStyle(fontSize = 13, textColor = "#ffffff", verticalAlignment = VerticalAlignment.Center)
                        }
                    }

                    group {
                        layoutMode = LayoutMode.Left
                        anchor = UiAnchor(width = 150, left = 20)

                        checkBox("AutoSave") {
                            value = false
                            onChange = {
                                val enabled = boolValue ?: false
                                player.sendMessage(Message.raw("Auto-Save: ${if (enabled) "ON" else "OFF"}").color("4ECDC4"))
                            }
                        }
                        label {
                            text = "Auto-Save"
                            anchor = UiAnchor(left = 8)
                            style = UiLabelStyle(fontSize = 13, textColor = "#ffffff", verticalAlignment = VerticalAlignment.Center)
                        }
                    }
                }

                group {
                    layoutMode = LayoutMode.Left
                    padding = UiPadding(top = 8)

                    group {
                        layoutMode = LayoutMode.Left
                        anchor = UiAnchor(width = 150)

                        checkBox("ShowHints") {
                            value = true
                            onChange = {
                                val enabled = boolValue ?: false
                                player.sendMessage(Message.raw("Show Hints: ${if (enabled) "ON" else "OFF"}").color("4ECDC4"))
                            }
                        }
                        label {
                            text = "Show Hints"
                            anchor = UiAnchor(left = 8)
                            style = UiLabelStyle(fontSize = 13, textColor = "#ffffff", verticalAlignment = VerticalAlignment.Center)
                        }
                    }

                    group {
                        layoutMode = LayoutMode.Left
                        anchor = UiAnchor(width = 150, left = 20)

                        checkBox("DarkMode") {
                            value = false
                            onChange = {
                                val enabled = boolValue ?: false
                                player.sendMessage(Message.raw("Dark Mode: ${if (enabled) "ON" else "OFF"}").color("4ECDC4"))
                            }
                        }
                        label {
                            text = "Dark Mode"
                            anchor = UiAnchor(left = 8)
                            style = UiLabelStyle(fontSize = 13, textColor = "#ffffff", verticalAlignment = VerticalAlignment.Center)
                        }
                    }
                }
            }

            // Dropdown Section
            group("DropdownSection") {
                layoutMode = LayoutMode.Top
                padding = UiPadding(vertical = 12, horizontal = 16)

                sectionHeader("Dropdown")

                group {
                    layoutMode = LayoutMode.Left
                    padding = UiPadding(top = 10)

                    label {
                        text = "Category:"
                        anchor = UiAnchor(width = 90)
                        style = UiLabelStyle(fontSize = 14, textColor = "#7c8b99", verticalAlignment = VerticalAlignment.Center)
                    }

                    dropdownBox("CategoryDropdown") {
                        anchor = UiAnchor(width = 200, height = 34)
                        noItemsText = "Select category..."
                        onChange = {
                            player.sendMessage(Message.raw("Category: index ${intValue ?: value}").color("55FFFF"))
                        }
                    }
                }
            }
        }
    }

    // ==================== PAGE 2: DATA MANAGEMENT ====================

    val uiTestPage2 = interactivePage("UiTest2") {
        width = 550
        height = 650

        title {
            label {
                text = "UI Test - Page 2"
                style = UiLabelStyle(
                    fontSize = 24,
                    textColor = "#ffffff",
                    renderBold = true,
                    horizontalAlignment = HorizontalAlignment.Center
                )
            }
        }

        content {
            // Navigation Bar
            group("NavBar") {
                layoutMode = LayoutMode.Left
                padding = UiPadding(horizontal = 16, vertical = 12)

                textButton("NavPage1") {
                    darkButton("1: Inputs")
                    anchor = UiAnchor(height = 36)
                    flexWeight = 1
                    onClick = {
                        player.sendMessage(Message.raw("Opening page 1...").color("55FF55"))
                        player.openPage("Kytale/UiTest", playerRef)
                    }
                }

                textButton("NavPage2") {
                    primaryButton("2: Data")
                    anchor = UiAnchor(height = 36, left = 8)
                    flexWeight = 1
                    onClick = {
                        player.sendMessage(Message.raw("Already on page 2").color("AAAAAA"))
                    }
                }

                textButton("NavPage3") {
                    darkButton("3: Actions")
                    anchor = UiAnchor(height = 36, left = 8)
                    flexWeight = 1
                    onClick = {
                        player.sendMessage(Message.raw("Opening page 3...").color("55FF55"))
                        player.openPage("Kytale/UiTest3", playerRef)
                    }
                }
            }

            // Page Title
            group("PageTitle") {
                padding = UiPadding(horizontal = 16, vertical = 8)
                label {
                    text = "Data Management"
                    style = UiLabelStyle(fontSize = 24, textColor = "#ffffff", renderBold = true)
                }
            }

            // Pagination Section
            group("PaginationSection") {
                layoutMode = LayoutMode.Top
                padding = UiPadding(vertical = 12, horizontal = 16)

                sectionHeader("Pagination")

                group {
                    layoutMode = LayoutMode.Left
                    padding = UiPadding(top = 10)

                    textButton("FirstPage") {
                        darkButton("<<")
                        anchor = UiAnchor(width = 40, height = 32)
                        onClick = { player.sendMessage(Message.raw("Navigated to first page").color("55FFFF")) }
                    }
                    textButton("PrevPage") {
                        darkButton("<")
                        anchor = UiAnchor(width = 40, height = 32, left = 4)
                        onClick = { player.sendMessage(Message.raw("Navigated to previous page").color("55FFFF")) }
                    }

                    textButton("Page1") {
                        primaryButton("1")
                        anchor = UiAnchor(width = 36, height = 32, left = 8)
                        onClick = { player.sendMessage(Message.raw("Navigated to page 1").color("55FFFF")) }
                    }
                    textButton("Page2") {
                        darkButton("2")
                        anchor = UiAnchor(width = 36, height = 32, left = 4)
                        onClick = { player.sendMessage(Message.raw("Navigated to page 2").color("55FFFF")) }
                    }
                    textButton("Page3") {
                        darkButton("3")
                        anchor = UiAnchor(width = 36, height = 32, left = 4)
                        onClick = { player.sendMessage(Message.raw("Navigated to page 3").color("55FFFF")) }
                    }
                    textButton("Page4") {
                        darkButton("4")
                        anchor = UiAnchor(width = 36, height = 32, left = 4)
                        onClick = { player.sendMessage(Message.raw("Navigated to page 4").color("55FFFF")) }
                    }
                    textButton("Page5") {
                        darkButton("5")
                        anchor = UiAnchor(width = 36, height = 32, left = 4)
                        onClick = { player.sendMessage(Message.raw("Navigated to page 5").color("55FFFF")) }
                    }

                    textButton("NextPage") {
                        darkButton(">")
                        anchor = UiAnchor(width = 40, height = 32, left = 8)
                        onClick = { player.sendMessage(Message.raw("Navigated to next page").color("55FFFF")) }
                    }
                    textButton("LastPage") {
                        darkButton(">>")
                        anchor = UiAnchor(width = 40, height = 32, left = 4)
                        onClick = { player.sendMessage(Message.raw("Navigated to last page").color("55FFFF")) }
                    }
                }
            }

            // Sorting Section
            group("SortingSection") {
                layoutMode = LayoutMode.Top
                padding = UiPadding(vertical = 12, horizontal = 16)

                sectionHeader("Sorting")

                group {
                    layoutMode = LayoutMode.Left
                    padding = UiPadding(top = 10)

                    label {
                        text = "Sort by:"
                        anchor = UiAnchor(width = 60)
                        style = UiLabelStyle(fontSize = 14, textColor = "#7c8b99", verticalAlignment = VerticalAlignment.Center)
                    }

                    textButton("SortName") {
                        primaryButton("Name")
                        anchor = UiAnchor(width = 70, height = 30)
                        onClick = { player.sendMessage(Message.raw("Sorting by: name").color("FFAA00")) }
                    }
                    textButton("SortDate") {
                        darkButton("Date")
                        anchor = UiAnchor(width = 70, height = 30, left = 6)
                        onClick = { player.sendMessage(Message.raw("Sorting by: date").color("FFAA00")) }
                    }
                    textButton("SortSize") {
                        darkButton("Size")
                        anchor = UiAnchor(width = 70, height = 30, left = 6)
                        onClick = { player.sendMessage(Message.raw("Sorting by: size").color("FFAA00")) }
                    }
                    textButton("SortType") {
                        darkButton("Type")
                        anchor = UiAnchor(width = 70, height = 30, left = 6)
                        onClick = { player.sendMessage(Message.raw("Sorting by: type").color("FFAA00")) }
                    }
                }

                group {
                    layoutMode = LayoutMode.Left
                    padding = UiPadding(top = 8)

                    label {
                        text = "Order:"
                        anchor = UiAnchor(width = 60)
                        style = UiLabelStyle(fontSize = 14, textColor = "#7c8b99", verticalAlignment = VerticalAlignment.Center)
                    }

                    textButton("SortAsc") {
                        successButton("Ascending")
                        anchor = UiAnchor(width = 100, height = 30)
                        onClick = { player.sendMessage(Message.raw("Sort order: ascending").color("FFAA00")) }
                    }
                    textButton("SortDesc") {
                        darkButton("Descending")
                        anchor = UiAnchor(width = 100, height = 30, left = 6)
                        onClick = { player.sendMessage(Message.raw("Sort order: descending").color("FFAA00")) }
                    }
                }
            }

            // Filter Section
            group("FilterSection") {
                layoutMode = LayoutMode.Top
                padding = UiPadding(vertical = 12, horizontal = 16)

                sectionHeader("Filters")

                group {
                    layoutMode = LayoutMode.Left
                    padding = UiPadding(top = 10)

                    textButton("FilterAll") {
                        primaryButton("All")
                        anchor = UiAnchor(width = 60, height = 30)
                        onClick = { player.sendMessage(Message.raw("Filter: all").color("4ECDC4")) }
                    }
                    textButton("FilterActive") {
                        darkButton("Active")
                        anchor = UiAnchor(width = 70, height = 30, left = 6)
                        onClick = { player.sendMessage(Message.raw("Filter: active").color("4ECDC4")) }
                    }
                    textButton("FilterPending") {
                        darkButton("Pending")
                        anchor = UiAnchor(width = 80, height = 30, left = 6)
                        onClick = { player.sendMessage(Message.raw("Filter: pending").color("4ECDC4")) }
                    }
                    textButton("FilterCompleted") {
                        darkButton("Done")
                        anchor = UiAnchor(width = 60, height = 30, left = 6)
                        onClick = { player.sendMessage(Message.raw("Filter: completed").color("4ECDC4")) }
                    }
                    textButton("FilterArchived") {
                        darkButton("Archived")
                        anchor = UiAnchor(width = 80, height = 30, left = 6)
                        onClick = { player.sendMessage(Message.raw("Filter: archived").color("4ECDC4")) }
                    }
                }
            }

            // Status Section
            group("StatusSection") {
                layoutMode = LayoutMode.Top
                padding = UiPadding(vertical = 12, horizontal = 16)

                sectionHeader("Status")

                group {
                    layoutMode = LayoutMode.Left
                    padding = UiPadding(top = 10)

                    group {
                        layoutMode = LayoutMode.Top
                        anchor = UiAnchor(width = 140)
                        label { text = "Total Items"; style = UiLabelStyle(fontSize = 12, textColor = "#7c8b99") }
                        label("TotalItems") { text = "1,247"; style = UiLabelStyle(fontSize = 32, textColor = "#ffffff", renderBold = true) }
                    }

                    group {
                        layoutMode = LayoutMode.Top
                        anchor = UiAnchor(width = 140, left = 20)
                        label { text = "Showing"; style = UiLabelStyle(fontSize = 12, textColor = "#7c8b99") }
                        label("ShowingRange") { text = "1 - 25"; style = UiLabelStyle(fontSize = 32, textColor = "#c4a23a", renderBold = true) }
                    }

                    group {
                        layoutMode = LayoutMode.Top
                        anchor = UiAnchor(width = 140, left = 20)
                        label { text = "Selected"; style = UiLabelStyle(fontSize = 12, textColor = "#7c8b99") }
                        label("SelectedCount") { text = "3"; style = UiLabelStyle(fontSize = 32, textColor = "#4ecdc4", renderBold = true) }
                    }
                }
            }
        }
    }

    // ==================== PAGE 3: ACTIONS ====================

    val uiTestPage3 = interactivePage("UiTest3") {
        width = 550
        height = 650

        title {
            label {
                text = "UI Test - Page 3"
                style = UiLabelStyle(
                    fontSize = 24,
                    textColor = "#ffffff",
                    renderBold = true,
                    horizontalAlignment = HorizontalAlignment.Center
                )
            }
        }

        content {
            // Navigation Bar
            group("NavBar") {
                layoutMode = LayoutMode.Left
                padding = UiPadding(horizontal = 16, vertical = 12)

                textButton("NavPage1") {
                    darkButton("1: Inputs")
                    anchor = UiAnchor(height = 36)
                    flexWeight = 1
                    onClick = {
                        player.sendMessage(Message.raw("Opening page 1...").color("55FF55"))
                        player.openPage("Kytale/UiTest", playerRef)
                    }
                }

                textButton("NavPage2") {
                    darkButton("2: Data")
                    anchor = UiAnchor(height = 36, left = 8)
                    flexWeight = 1
                    onClick = {
                        player.sendMessage(Message.raw("Opening page 2...").color("55FF55"))
                        player.openPage("Kytale/UiTest2", playerRef)
                    }
                }

                textButton("NavPage3") {
                    primaryButton("3: Actions")
                    anchor = UiAnchor(height = 36, left = 8)
                    flexWeight = 1
                    onClick = {
                        player.sendMessage(Message.raw("Already on page 3").color("AAAAAA"))
                    }
                }
            }

            // Page Title
            group("PageTitle") {
                padding = UiPadding(horizontal = 16, vertical = 8)
                label {
                    text = "Actions & Buttons"
                    style = UiLabelStyle(fontSize = 24, textColor = "#ffffff", renderBold = true)
                }
            }

            // Button Variants Section
            group("ButtonSection") {
                layoutMode = LayoutMode.Top
                padding = UiPadding(vertical = 12, horizontal = 16)

                sectionHeader("Button Variants")

                group {
                    layoutMode = LayoutMode.Left
                    padding = UiPadding(top = 10)

                    textButton("PrimaryBtn") {
                        primaryButton("Primary")
                        anchor = UiAnchor(width = 90, height = 36)
                        onClick = { player.sendMessage(Message.raw("Primary button clicked!").color("FFD700")) }
                    }
                    textButton("SecondaryBtn") {
                        darkButton("Secondary")
                        anchor = UiAnchor(width = 90, height = 36, left = 8)
                        onClick = { player.sendMessage(Message.raw("Secondary button clicked!").color("AAAAAA")) }
                    }
                    textButton("DangerBtn") {
                        dangerButton("Danger")
                        anchor = UiAnchor(width = 90, height = 36, left = 8)
                        onClick = { player.sendMessage(Message.raw("Danger button clicked!").color("FF5555")) }
                    }
                }

                group {
                    layoutMode = LayoutMode.Left
                    padding = UiPadding(top = 8)

                    textButton("SuccessBtn") {
                        successButton("Success")
                        anchor = UiAnchor(width = 90, height = 36)
                        onClick = { player.sendMessage(Message.raw("Success button clicked!").color("55FF55")) }
                    }
                    textButton("WarningBtn") {
                        warningButton("Warning")
                        anchor = UiAnchor(width = 90, height = 36, left = 8)
                        onClick = { player.sendMessage(Message.raw("Warning button clicked!").color("FFAA00")) }
                    }
                    textButton("InfoBtn") {
                        infoButton("Info")
                        anchor = UiAnchor(width = 90, height = 36, left = 8)
                        onClick = { player.sendMessage(Message.raw("Info button clicked!").color("55AAFF")) }
                    }
                }
            }

            // Quick Actions Section
            group("ActionsSection") {
                layoutMode = LayoutMode.Top
                padding = UiPadding(vertical = 12, horizontal = 16)

                sectionHeader("Quick Actions")

                group {
                    layoutMode = LayoutMode.Left
                    padding = UiPadding(top = 10)

                    textButton("ActionNew") {
                        primaryButton("+ New")
                        anchor = UiAnchor(width = 80, height = 36)
                        onClick = { player.sendMessage(Message.raw("Creating new item...").color("55FF55")) }
                    }
                    textButton("ActionImport") {
                        darkButton("Import")
                        anchor = UiAnchor(width = 80, height = 36, left = 8)
                        onClick = { player.sendMessage(Message.raw("Importing data...").color("55FFFF")) }
                    }
                    textButton("ActionExport") {
                        darkButton("Export")
                        anchor = UiAnchor(width = 80, height = 36, left = 8)
                        onClick = { player.sendMessage(Message.raw("Exporting data...").color("55FFFF")) }
                    }
                    textButton("ActionRefresh") {
                        darkButton("Refresh")
                        anchor = UiAnchor(width = 80, height = 36, left = 8)
                        onClick = { player.sendMessage(Message.raw("Refreshing...").color("AAAAAA")) }
                    }
                }
            }

            // Confirmation Section
            group("ConfirmSection") {
                layoutMode = LayoutMode.Top
                padding = UiPadding(vertical = 12, horizontal = 16)

                sectionHeader("Confirmation")

                group {
                    layoutMode = LayoutMode.Top
                    padding = UiPadding(top = 10)

                    label {
                        text = "Are you sure you want to delete this item?"
                        style = UiLabelStyle(fontSize = 14, textColor = "#ffffff")
                    }

                    group {
                        layoutMode = LayoutMode.Left
                        padding = UiPadding(top = 10)

                        textButton("ConfirmYes") {
                            dangerButton("Yes, Delete")
                            anchor = UiAnchor(width = 110, height = 36)
                            onClick = { player.sendMessage(Message.raw("Item deleted!").color("FF5555")) }
                        }
                        textButton("ConfirmNo") {
                            darkButton("Cancel")
                            anchor = UiAnchor(width = 90, height = 36, left = 8)
                            onClick = { player.sendMessage(Message.raw("Action cancelled.").color("AAAAAA")) }
                        }
                    }
                }
            }

            // Footer Actions Section
            group("FooterSection") {
                layoutMode = LayoutMode.Top
                padding = UiPadding(vertical = 12, horizontal = 16)

                sectionHeader("Form Actions")

                group {
                    layoutMode = LayoutMode.Left
                    padding = UiPadding(top = 10)

                    textButton("ResetButton") {
                        darkButton("Reset")
                        anchor = UiAnchor(height = 40)
                        flexWeight = 1
                        onClick = { player.sendMessage(Message.raw("Settings reset to defaults").color("AAAAAA")) }
                    }
                    textButton("ApplyButton") {
                        successButton("Apply")
                        anchor = UiAnchor(height = 40, left = 8)
                        flexWeight = 1
                        onClick = { player.sendMessage(Message.raw("Changes applied!").color("55FF55")) }
                    }
                    textButton("SaveButton") {
                        primaryButton("Save")
                        anchor = UiAnchor(height = 40, left = 8)
                        flexWeight = 2
                        onClick = { player.sendMessage(Message.raw("Changes saved successfully!").color("FFD700")) }
                    }
                }
            }
        }
    }
}
