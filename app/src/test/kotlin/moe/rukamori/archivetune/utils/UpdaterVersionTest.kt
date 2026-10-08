package moe.rukamori.archivetune.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdaterVersionTest {
    @Test
    fun comparesLargeVersionComponentsWithoutOfferingDowngrades() {
        assertTrue(Updater.isUpdateAvailable("2147483649.0.0", "2147483648.0.0"))
        assertFalse(Updater.isUpdateAvailable("2147483648.0.0", "2147483649.0.0"))
        assertFalse(Updater.isUpdateAvailable("1.2147483648.0", "1.2147483649.0"))
        assertFalse(Updater.isUpdateAvailable("1.0.2147483648", "1.0.2147483649"))
        assertTrue(Updater.isSameVersion("v2147483648.0.0", "2147483648.0.0"))
    }

    @Test
    fun comparesNumericPrereleasesBeyondLongRange() {
        assertTrue(
            Updater.isUpdateAvailable(
                "1.0.0-9223372036854775808",
                "1.0.0-9223372036854775807",
            ),
        )
        assertFalse(
            Updater.isUpdateAvailable(
                "1.0.0-9223372036854775807",
                "1.0.0-9223372036854775808",
            ),
        )
        assertTrue(
            Updater.isSameVersion(
                "v1.0.0-9223372036854775808",
                "1.0.0-9223372036854775808",
            ),
        )
    }

    @Test
    fun retainsSemanticVersionPrecedence() {
        assertTrue(Updater.isUpdateAvailable("1.0.0", "1.0.0-999999999999999999999999999999"))
        assertTrue(Updater.isUpdateAvailable("1.0.0-beta", "1.0.0-999999999999999999999999999999"))
        assertTrue(Updater.isUpdateAvailable("1.0.0-beta.10", "1.0.0-beta.2"))
        assertFalse(Updater.isUpdateAvailable("1.0.0-beta.2", "1.0.0-beta.10"))
    }
}
