package io.github.hytalekt.kytale.codec

import com.hypixel.hytale.codec.Codec
import com.hypixel.hytale.codec.EmptyExtraInfo
import com.hypixel.hytale.codec.KeyedCodec
import com.hypixel.hytale.codec.builder.BuilderCodec
import com.hypixel.hytale.codec.schema.SchemaContext
import com.hypixel.hytale.codec.schema.config.Schema
import com.hypixel.hytale.codec.validation.ValidationResults
import com.hypixel.hytale.codec.validation.Validator
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.bson.BsonDocument
import org.bson.BsonInt32
import org.bson.BsonString

private data class Foo(
    var name: String = "",
    var value: Int = 0,
)

class CodecBuilderTest :
    FunSpec({
        test("DSL builds equivalent codec to Java builder") {
            val dslCodec =
                buildCodec(::Foo) {
                    addField("Name", Codec.STRING) {
                        setter { name = it }
                        getter { _ -> name }
                    }
                    addField("Value", Codec.INTEGER) {
                        setter { value = it }
                        getter { _ -> value }
                    }
                }

            val javaCodec =
                BuilderCodec
                    .builder(Foo::class.java, ::Foo)
                    .append(KeyedCodec("Name", Codec.STRING), { f, n -> f.name = n }, { it.name })
                    .add()
                    .append(KeyedCodec("Value", Codec.INTEGER), { f, v -> f.value = v }, { it.value })
                    .add()
                    .build()

            val testFoo = Foo("test", 42)
            dslCodec.encode(testFoo, EmptyExtraInfo.EMPTY) shouldBe javaCodec.encode(testFoo, EmptyExtraInfo.EMPTY)
        }

        test("ExtraInfo is passed to setter and getter") {
            var setterVersion = -1
            var getterVersion = -1

            val codec =
                buildCodec(::Foo) {
                    versioned()
                    codecVersion(5, 0)
                    addField("Name", Codec.STRING) {
                        setter { name = it }
                        getter { _ -> name }
                    }
                    addField("Value", Codec.INTEGER) {
                        setter { v, extra -> setterVersion = extra.version; value = v }
                        getter { extra -> getterVersion = extra.version; value }
                    }
                }

            codec.encode(Foo("test", 99), EmptyExtraInfo.EMPTY)
            getterVersion shouldBe 5

            val doc = BsonDocument().apply {
                put("Version", BsonInt32(3))
                put("Name", BsonString("test"))
                put("Value", BsonInt32(42))
            }
            codec.decode(doc, EmptyExtraInfo.EMPTY)
            setterVersion shouldBe 3
        }

        test("afterDecode callback is invoked") {
            var called = false
            val codec =
                buildCodec(::Foo) {
                    addField("Name", Codec.STRING) {
                        setter { name = it }
                        getter { _ -> name }
                    }
                    afterDecode { called = true }
                }

            codec.decode(BsonDocument().apply { put("Name", BsonString("test")) }, EmptyExtraInfo.EMPTY)
            called shouldBe true
        }

        test("addField with KeyedCodec and field documentation") {
            val codec =
                buildCodec(::Foo) {
                    documentation = "Codec-level documentation"
                    addField(KeyedCodec("Name", Codec.STRING)) {
                        documentation = "Field-level documentation"
                        setter { name = it }
                        getter { _ -> name }
                    }
                }

            val decoded = codec.decode(BsonDocument().apply { put("Name", BsonString("test")) }, EmptyExtraInfo.EMPTY)!!
            decoded.name shouldBe "test"
        }

        test("inherit function copies value from parent") {
            var inheritCalled = false
            val codec =
                buildCodec(::Foo) {
                    addField("Name", Codec.STRING) {
                        setter { name = it }
                        getter { _ -> name }
                        inherit { parent, _ ->
                            inheritCalled = true
                            name = parent.name
                        }
                    }
                    addField("Value", Codec.INTEGER) {
                        setter { value = it }
                        getter { _ -> value }
                    }
                }

            val parent = Foo("inherited", 100)
            val child = codec.decodeAndInherit(BsonDocument().apply { put("Value", BsonInt32(50)) }, parent, EmptyExtraInfo.EMPTY)!!

            inheritCalled shouldBe true
            child.name shouldBe "inherited"
            child.value shouldBe 50
        }

        test("addValidator is invoked during decode") {
            var validatorCalled = false
            val codec =
                buildCodec(::Foo) {
                    addField("Name", Codec.STRING) {
                        setter { name = it }
                        getter { _ -> name }
                        addValidator(object : Validator<String> {
                            override fun accept(value: String, results: ValidationResults) {
                                validatorCalled = true
                            }
                            override fun updateSchema(ctx: SchemaContext, schema: Schema) {}
                        })
                    }
                }

            codec.decode(BsonDocument().apply { put("Name", BsonString("test")) }, EmptyExtraInfo.EMPTY)
            validatorCalled shouldBe true
        }
    })
