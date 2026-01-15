package aster.amo.kytale.extension

import aster.amo.kytale.coroutines.HytaleDispatchers
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.Universe
import kotlinx.coroutines.withContext
import java.util.UUID

/**
 * Extension functions for Hytale Universe operations.
 *
 * A Universe represents the entire server state including all Worlds,
 * connected players, and server-wide data.
 */

/**
 * Gets a player reference by UUID.
 *
 * @param uuid the player's UUID
 * @return the PlayerRef, or null if not found
 */
fun Universe.getPlayerOrNull(uuid: UUID): PlayerRef? {
    return try {
        getPlayer(uuid)
    } catch (e: Exception) {
        null
    }
}

/**
 * Gets all online player references.
 *
 * @return a list of all online PlayerRefs
 */
fun Universe.getOnlinePlayers(): List<PlayerRef> {
    return players.toList()
}

/**
 * Executes an action for each online player.
 *
 * @param action the action to perform
 */
inline fun Universe.forEachPlayer(action: (PlayerRef) -> Unit) {
    players.forEach(action)
}

/**
 * Filters online players based on a predicate.
 *
 * @param predicate the filter condition
 * @return a list of matching PlayerRefs
 */
inline fun Universe.filterPlayers(predicate: (PlayerRef) -> Boolean): List<PlayerRef> {
    return players.filter(predicate)
}

/**
 * Finds the first player matching a predicate.
 *
 * @param predicate the match condition
 * @return the first matching PlayerRef, or null
 */
inline fun Universe.findPlayer(predicate: (PlayerRef) -> Boolean): PlayerRef? {
    return players.firstOrNull(predicate)
}

/**
 * Gets the online player count.
 *
 * @return the number of online players
 */
fun Universe.playerCount(): Int {
    return players.size
}

/**
 * Checks if a player with the given UUID is online.
 *
 * @param uuid the player's UUID
 * @return true if the player is online
 */
fun Universe.isPlayerOnline(uuid: UUID): Boolean {
    return getPlayerOrNull(uuid) != null
}

/**
 * Executes a block on the main server thread within this Universe context.
 *
 * @param block the code to execute
 * @return the result of the block
 */
suspend fun <T> Universe.onMainThread(block: suspend () -> T): T {
    return withContext(HytaleDispatchers.Main) { block() }
}
