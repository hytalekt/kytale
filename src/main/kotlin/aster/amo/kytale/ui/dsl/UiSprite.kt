package aster.amo.kytale.ui.dsl

/**
 * Sprite element - animated sprite with frame support.
 */
class UiSprite : UiElement() {
    var texturePath: String? = null
    var frameWidth: Int? = null
    var frameHeight: Int? = null
    var framesPerRow: Int? = null
    var frameCount: Int? = null
    var framesPerSecond: Int? = null

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}Sprite$idPart {")
        append(serializeProperties(indent + 1))
        texturePath?.let { appendLine("${indent(indent + 1)}TexturePath: \"$it\";") }

        val hasFrame = frameWidth != null || frameHeight != null || framesPerRow != null || frameCount != null
        if (hasFrame) {
            val frameParts = mutableListOf<String>()
            frameWidth?.let { frameParts.add("Width: $it") }
            frameHeight?.let { frameParts.add("Height: $it") }
            framesPerRow?.let { frameParts.add("PerRow: $it") }
            frameCount?.let { frameParts.add("Count: $it") }
            appendLine("${indent(indent + 1)}Frame: (${frameParts.joinToString(", ")});")
        }
        framesPerSecond?.let { appendLine("${indent(indent + 1)}FramesPerSecond: $it;") }
        appendLine("${indent(indent)}}")
    }
}

/**
 * AssetImage element - dynamic asset image display.
 */
class UiAssetImage : UiElement() {
    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}AssetImage$idPart {")
        append(serializeProperties(indent + 1))
        appendLine("${indent(indent)}}")
    }
}

/**
 * ProgressBar element - progress indicator.
 */
class UiProgressBar : UiElement() {
    var barTexturePath: String? = null
    var effectTexturePath: String? = null
    var effectWidth: Int? = null
    var effectHeight: Int? = null
    var effectOffset: Int? = null

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}ProgressBar$idPart {")
        append(serializeProperties(indent + 1))
        barTexturePath?.let { appendLine("${indent(indent + 1)}BarTexturePath: \"$it\";") }
        effectTexturePath?.let { appendLine("${indent(indent + 1)}EffectTexturePath: \"$it\";") }
        effectWidth?.let { appendLine("${indent(indent + 1)}EffectWidth: $it;") }
        effectHeight?.let { appendLine("${indent(indent + 1)}EffectHeight: $it;") }
        effectOffset?.let { appendLine("${indent(indent + 1)}EffectOffset: $it;") }
        appendLine("${indent(indent)}}")
    }
}

/**
 * TimerLabel element - countdown timer display.
 */
class UiTimerLabel : UiElement() {
    var seconds: Int? = null
    var style: UiLabelStyle? = null

    override fun serialize(indent: Int): String = buildString {
        val idPart = id?.let { " #$it" } ?: ""
        appendLine("${indent(indent)}TimerLabel$idPart {")
        append(serializeProperties(indent + 1))
        style?.let { appendLine("${indent(indent + 1)}Style: ${it.serialize()};") }
        seconds?.let { appendLine("${indent(indent + 1)}Seconds: $it;") }
        appendLine("${indent(indent)}}")
    }
}
