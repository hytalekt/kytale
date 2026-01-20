package io.github.hytalekt.kytale.extension

import com.hypixel.hytale.math.vector.Transform
import com.hypixel.hytale.math.vector.Vector3d
import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.nulls.shouldBeNull
import io.mockk.every
import io.mockk.mockk
import com.hypixel.hytale.server.core.universe.PlayerRef
import java.util.UUID

/**
 * Tests for entity extension functions.
 *
 * NOTE: These tests are currently ignored because they require mocking Hytale classes
 * (PlayerRef, Transform, etc.) which trigger static class initialization failures
 * in the test environment.
 *
 * TODO: Create interfaces for Hytale dependencies to enable proper unit testing.
 */
@Ignored
class EntityExtensionsTest : FunSpec({

    context("PlayerRef.position") {
        test("returns position from transform") {
            val mockTransform = mockk<Transform>()
            val expectedPosition = Vector3d(10.0, 64.0, 20.0)
            every { mockTransform.position } returns expectedPosition

            val playerRef = mockk<PlayerRef>()
            every { playerRef.transform } returns mockTransform

            playerRef.position shouldBe expectedPosition
        }

        test("returns null when exception is thrown") {
            val playerRef = mockk<PlayerRef>()
            every { playerRef.transform } throws RuntimeException("Test exception")

            playerRef.position.shouldBeNull()
        }
    }

    context("PlayerRef.currentWorldUuid") {
        test("returns world UUID") {
            val expectedUuid = UUID.randomUUID()
            val playerRef = mockk<PlayerRef>()
            every { playerRef.worldUuid } returns expectedUuid

            playerRef.currentWorldUuid shouldBe expectedUuid
        }

        test("returns null when not in a world") {
            val playerRef = mockk<PlayerRef>()
            every { playerRef.worldUuid } returns null

            playerRef.currentWorldUuid.shouldBeNull()
        }
    }

    context("PlayerRef.isInSameWorld") {
        test("returns true when players are in the same world") {
            val worldUuid = UUID.randomUUID()

            val player1 = mockk<PlayerRef>()
            val player2 = mockk<PlayerRef>()
            every { player1.worldUuid } returns worldUuid
            every { player2.worldUuid } returns worldUuid

            player1.isInSameWorld(player2) shouldBe true
        }

        test("returns false when players are in different worlds") {
            val player1 = mockk<PlayerRef>()
            val player2 = mockk<PlayerRef>()
            every { player1.worldUuid } returns UUID.randomUUID()
            every { player2.worldUuid } returns UUID.randomUUID()

            player1.isInSameWorld(player2) shouldBe false
        }

        test("returns false when first player has no world") {
            val player1 = mockk<PlayerRef>()
            val player2 = mockk<PlayerRef>()
            every { player1.worldUuid } returns null
            every { player2.worldUuid } returns UUID.randomUUID()

            player1.isInSameWorld(player2) shouldBe false
        }

        test("returns false when second player has no world") {
            val player1 = mockk<PlayerRef>()
            val player2 = mockk<PlayerRef>()
            every { player1.worldUuid } returns UUID.randomUUID()
            every { player2.worldUuid } returns null

            player1.isInSameWorld(player2) shouldBe false
        }
    }

    context("PlayerRef.distanceTo") {
        test("calculates distance between two players") {
            val transform1 = mockk<Transform>()
            val transform2 = mockk<Transform>()
            every { transform1.position } returns Vector3d(0.0, 0.0, 0.0)
            every { transform2.position } returns Vector3d(3.0, 4.0, 0.0)

            val player1 = mockk<PlayerRef>()
            val player2 = mockk<PlayerRef>()
            every { player1.transform } returns transform1
            every { player2.transform } returns transform2

            player1.distanceTo(player2) shouldBe 5.0
        }

        test("returns null when first player throws exception") {
            val player1 = mockk<PlayerRef>()
            val player2 = mockk<PlayerRef>()
            every { player1.transform } throws RuntimeException("No transform")

            val transform2 = mockk<Transform>()
            every { transform2.position } returns Vector3d(3.0, 4.0, 0.0)
            every { player2.transform } returns transform2

            player1.distanceTo(player2).shouldBeNull()
        }

        test("returns null when second player throws exception") {
            val transform1 = mockk<Transform>()
            every { transform1.position } returns Vector3d(0.0, 0.0, 0.0)

            val player1 = mockk<PlayerRef>()
            val player2 = mockk<PlayerRef>()
            every { player1.transform } returns transform1
            every { player2.transform } throws RuntimeException("No transform")

            player1.distanceTo(player2).shouldBeNull()
        }
    }

    context("PlayerRef.isWithinRange") {
        test("returns true when within range") {
            val transform1 = mockk<Transform>()
            val transform2 = mockk<Transform>()
            every { transform1.position } returns Vector3d(0.0, 0.0, 0.0)
            every { transform2.position } returns Vector3d(3.0, 4.0, 0.0)

            val player1 = mockk<PlayerRef>()
            val player2 = mockk<PlayerRef>()
            every { player1.transform } returns transform1
            every { player2.transform } returns transform2

            player1.isWithinRange(player2, 10.0) shouldBe true
        }

        test("returns true when exactly at range") {
            val transform1 = mockk<Transform>()
            val transform2 = mockk<Transform>()
            every { transform1.position } returns Vector3d(0.0, 0.0, 0.0)
            every { transform2.position } returns Vector3d(3.0, 4.0, 0.0)

            val player1 = mockk<PlayerRef>()
            val player2 = mockk<PlayerRef>()
            every { player1.transform } returns transform1
            every { player2.transform } returns transform2

            player1.isWithinRange(player2, 5.0) shouldBe true
        }

        test("returns false when outside range") {
            val transform1 = mockk<Transform>()
            val transform2 = mockk<Transform>()
            every { transform1.position } returns Vector3d(0.0, 0.0, 0.0)
            every { transform2.position } returns Vector3d(3.0, 4.0, 0.0)

            val player1 = mockk<PlayerRef>()
            val player2 = mockk<PlayerRef>()
            every { player1.transform } returns transform1
            every { player2.transform } returns transform2

            player1.isWithinRange(player2, 4.0) shouldBe false
        }

        test("returns false when positions unavailable") {
            val player1 = mockk<PlayerRef>()
            val player2 = mockk<PlayerRef>()
            every { player1.transform } throws RuntimeException("No transform")
            every { player2.transform } throws RuntimeException("No transform")

            player1.isWithinRange(player2, 10.0) shouldBe false
        }
    }

    context("PlayerRef.safeGet") {
        test("returns value on success") {
            val playerRef = mockk<PlayerRef>()
            val expectedUuid = UUID.randomUUID()
            every { playerRef.worldUuid } returns expectedUuid

            val result = playerRef.safeGet { worldUuid }

            result shouldBe expectedUuid
        }

        test("returns null on exception") {
            val playerRef = mockk<PlayerRef>()
            every { playerRef.worldUuid } throws RuntimeException("Test")

            val result = playerRef.safeGet { worldUuid }

            result.shouldBeNull()
        }
    }
})
