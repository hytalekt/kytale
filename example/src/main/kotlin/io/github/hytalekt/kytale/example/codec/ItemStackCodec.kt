package io.github.hytalekt.kytale.example.codec

import com.hypixel.hytale.codec.Codec
import com.hypixel.hytale.codec.schema.SchemaContext
import com.hypixel.hytale.codec.schema.config.Schema
import com.hypixel.hytale.codec.validation.ValidationResults
import com.hypixel.hytale.codec.validation.Validator
import io.github.hytalekt.kytale.codec.buildCodec

data class ItemStack(
    var itemId: String = "",
    var count: Int = 1,
    var damage: Int = 0,
    var customName: String = "",
)

private object ItemCountValidator : Validator<Int> {
    override fun accept(
        value: Int,
        results: ValidationResults,
    ) {
        if (value < 1) {
            results.fail("Item count must be at least 1, got $value")
        }
        if (value > 64) {
            results.warn("Item count exceeds typical max of 64, got $value")
        }
    }

    override fun updateSchema(
        ctx: SchemaContext,
        schema: Schema,
    ) {}
}

val ItemStackCodec =
    buildCodec(::ItemStack) {
        documentation = "Represents a stack of items in an inventory"
        versioned()
        codecVersion(version = 3, minVersion = 1)

        addField("ItemId", Codec.STRING) {
            documentation = "The item type identifier"
            setter { itemId = it }
            getter { _ -> itemId }
        }

        addField("Count", Codec.INTEGER) {
            documentation = "Number of items in the stack (1-64)"
            setter { count = it }
            getter { _ -> count }
            addValidator(ItemCountValidator)
        }

        addField("Damage", Codec.INTEGER) {
            documentation = "Damage/durability value (added in v2)"
            setter { damage, extraInfo ->
                // Only set damage if version >= 2, otherwise use default
                if (extraInfo.version >= 2) {
                    this.damage = damage
                }
            }
            getter { _ -> damage }
        }

        addField("CustomName", Codec.STRING) {
            documentation = "Custom display name, empty if not set (added in v3)"
            setter { name, extraInfo ->
                if (extraInfo.version >= 3) {
                    customName = name
                }
            }
            getter { _ -> customName }
        }
    }
